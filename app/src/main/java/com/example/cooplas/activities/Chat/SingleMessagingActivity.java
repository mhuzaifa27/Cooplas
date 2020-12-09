package com.example.cooplas.activities.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooplas.Firebase.AppState;
import com.example.cooplas.Firebase.ChangeEventListener;
import com.example.cooplas.Firebase.Models.Message;
import com.example.cooplas.Firebase.Models.User;
import com.example.cooplas.Firebase.Services.LastMessageService;
import com.example.cooplas.Firebase.Services.SingleMessagingService;
import com.example.cooplas.R;
import com.example.cooplas.activities.home.HomeCreatePostActivity;
import com.example.cooplas.adapters.Chat.MessagingAdapter;
import com.example.cooplas.models.home.MediaTypeModel;
import com.example.cooplas.utils.Constants;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SingleMessagingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String TAG = "MessagingActivity";
    private Context context = SingleMessagingActivity.this;
    private Activity activity = SingleMessagingActivity.this;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_no;

    private RecyclerView rv_messaging;
    private MessagingAdapter messagingAdapter;
    private LinearLayoutManager layoutManager;
    private List<Message> messagesList = new ArrayList<>();

    private String receiverId, name, receiverPic;
    private User user;
    private EditText et_comment;

    Message message = new Message();

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA_q7nAiQ:APA91bFqSjnVDK9pa4WwOj-YofbMyQaCKjmVvhwYVPeczgACsu9k_VxDmEKojtKaz-t7UOgU1WWaTbOnQ_zC3mhy66Kzspq9ThCzgiCYqioviU_gHFRIGBJgoWyh4xtwaMqzIFPr8cJe";
    final private String contentType = "application/json";

    private String NOTIFICATION_TITLE;
    private String NOTIFICATION_MESSAGE;
    private String USER_ID;
    private String TOPIC = "/topics/chat";

    private SingleMessagingService singleMessagingService;
    private LastMessageService lastMessageService;
    private ImageView iv_send, img_record_audio, img_cancel_audio, img_camera;
    private TextView tv_name;
    private Chronometer chronometer_recording;
    private int pauseOffset = 0;
    private LinearLayout ll_recording_audio;
    private boolean isRecordedAudio = false;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private KProgressHUD progressHUD;
    private AlertDialog alertDialog;
    private long recordingElapsedMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_messaging);

        getIntentData();
        initComponents();

        tv_name.setText(name);

        lastMessageService = new LastMessageService(AppState.currentBpackCustomer.getUserId(), receiverId);
        lastMessageService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) { }
            @Override
            public void onDataChanged() {
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        singleMessagingService = new SingleMessagingService(AppState.currentBpackCustomer.getUserId(), receiverId);
        singleMessagingService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {

            }

            @Override
            public void onDataChanged() {
                messagesList.clear();
                if (singleMessagingService.getCount() > 0) {
                    for (int i = 0; i < singleMessagingService.getCount(); i++) {
                        DataSnapshot sp = singleMessagingService.getItem(i);
                        Message message = sp.getValue(Message.class);
                        if (!message.getUserId().equalsIgnoreCase(AppState.currentBpackCustomer.getUserId()))
                            messagesList.add(message);
                    }
                    messagingAdapter.addAll(messagesList);
                    rv_messaging.scrollToPosition(messagesList.size() - 1);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);

        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    img_record_audio.setVisibility(View.GONE);
                    iv_send.setVisibility(View.VISIBLE);
                } else {
                    img_record_audio.setVisibility(View.VISIBLE);
                    iv_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getIntentData() {
        receiverId = getIntent().getStringExtra(Constants.ID);
        name = getIntent().getStringExtra(Constants.START_NAME);
        receiverPic = getIntent().getStringExtra(Constants.IMAGE);
        user=(User)getIntent().getSerializableExtra(Constants.CREATE_RIDE_OBJ);
    }
    private void initComponents() {
        Firebase.setAndroidContext(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        progressHUD = KProgressHUD.create(activity);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        rv_messaging = findViewById(R.id.rv_messaging);

        layoutManager = new LinearLayoutManager(context);
        messagingAdapter = new MessagingAdapter(messagesList, context, activity,user);

        rv_messaging.setLayoutManager(layoutManager);
        rv_messaging.setAdapter(messagingAdapter);

        iv_send = findViewById(R.id.iv_send);
        img_cancel_audio = findViewById(R.id.img_cancel_audio);
        img_record_audio = findViewById(R.id.img_record_audio);
        img_camera = findViewById(R.id.img_camera);

        tv_name = findViewById(R.id.tv_name);

        chronometer_recording = findViewById(R.id.chronometer_recording);

        et_comment = findViewById(R.id.et_comment);

        ll_recording_audio = findViewById(R.id.ll_recording_audio);

        // Record to the external cache directory for visibility
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audio_" + UUID.randomUUID().toString() + ".3gp";

    }

    @Override
    public void onRefresh() {
        singleMessagingService = new SingleMessagingService(AppState.currentBpackCustomer.getUserId(), receiverId);
        singleMessagingService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {

            }

            @Override
            public void onDataChanged() {
                messagesList.clear();
                if (singleMessagingService.getCount() > 0) {
                    for (int i = 0; i < singleMessagingService.getCount(); i++) {
                        DataSnapshot sp = singleMessagingService.getItem(i);
                        Message message = sp.getValue(Message.class);
                        if (!message.getUserId().equalsIgnoreCase(AppState.currentBpackCustomer.getUserId()))
                            messagesList.add(message);
                    }
                    messagingAdapter.addAll(messagesList);
                    rv_messaging.scrollToPosition(messagesList.size() - 1);
                    if(swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.iv_send:
                if (isRecordedAudio) {
                    stopRecordingAndSend();
                } else {
                    if (et_comment.getText().toString().isEmpty()) {
                        Toast.makeText(context, "Enter Text!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        sendMessage(Constants.TYPE_TEXT, et_comment.getText().toString());
                    }
                }
                break;
            case R.id.img_cancel_audio:
                stopRecording();
                break;
            case R.id.img_record_audio:
                requestAudioPermissions();
                break;
            case R.id.img_camera:
                Dexter.withContext(activity)
                        .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            MediaSelectionAlert();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
                break;
        }
    }

    private void stopRecordingAndSend() {
        recorder.stop();
        recorder.release();
        recorder = null;
        resetTextingView();
        recordingElapsedMillis = SystemClock.elapsedRealtime() - chronometer_recording.getBase();
        UploadAudioToStorage();
        chronometer_recording.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        chronometer_recording.stop();
    }

    private void sendMessage(String type, String message) {
        //String message = et_comment.getText().toString();
        if (singleMessagingService == null) {
            singleMessagingService = new SingleMessagingService(AppState.currentBpackCustomer.getUserId(), receiverId);
            singleMessagingService.setOnChangedListener(new ChangeEventListener() {
                @Override
                public void onChildChanged(EventType type, int index, int oldIndex) {

                }

                @Override
                public void onDataChanged() {
               /* messagesList.clear();
                if (chatService.getCount() > 0) {
                    for (int i = 0; i < chatService.getCount(); i++) {
                        DataSnapshot sp = chatService.getItem(i);
                        Chat chat = sp.getValue(Chat.class);
                        if (chat.getUserId().equalsIgnoreCase(AppState.currentBpackCustomer.getUserId())) {
                            messagesList.add(chat);
                        }
                    }
                    if (messagingAdapter != null) {
                        messagingAdapter.addAll(messagesList);
                    } else {

                    }
                }*/
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
            singleMessagingService.sendMessage(message, AppState.currentBpackCustomer.getUserId(), receiverId, type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    et_comment.setText("");
                    lastMessageService.updateLastMessage(message, AppState.currentBpackCustomer.getUserId(), receiverId, type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            //Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            singleMessagingService.sendMessage(message,AppState.currentBpackCustomer.getUserId(), receiverId, type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    et_comment.setText("");
                    lastMessageService.updateLastMessage(message, AppState.currentBpackCustomer.getUserId(), receiverId, type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            //Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendMessage(String type, String message,long recordingElapsedMillis) {
        //String message = et_comment.getText().toString();
        if (singleMessagingService == null) {
            singleMessagingService = new SingleMessagingService(AppState.currentBpackCustomer.getUserId(), receiverId);
            singleMessagingService.setOnChangedListener(new ChangeEventListener() {
                @Override
                public void onChildChanged(EventType type, int index, int oldIndex) {

                }

                @Override
                public void onDataChanged() {
               /* messagesList.clear();
                if (chatService.getCount() > 0) {
                    for (int i = 0; i < chatService.getCount(); i++) {
                        DataSnapshot sp = chatService.getItem(i);
                        Chat chat = sp.getValue(Chat.class);
                        if (chat.getUserId().equalsIgnoreCase(AppState.currentBpackCustomer.getUserId())) {
                            messagesList.add(chat);
                        }
                    }
                    if (messagingAdapter != null) {
                        messagingAdapter.addAll(messagesList);
                    } else {

                    }
                }*/
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
            singleMessagingService.sendMessage(message,recordingElapsedMillis,  AppState.currentBpackCustomer.getUserId(), receiverId, type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    et_comment.setText("");
                    lastMessageService.updateLastMessage(message,recordingElapsedMillis,  AppState.currentBpackCustomer.getUserId(), receiverId, type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            //Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            singleMessagingService.sendMessage(message,recordingElapsedMillis, AppState.currentBpackCustomer.getUserId(), receiverId, type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    et_comment.setText("");
                    lastMessageService.updateLastMessage(message,recordingElapsedMillis,  AppState.currentBpackCustomer.getUserId(), receiverId, type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            et_comment.setText("");

                            //Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /***RECORDING AUDIO CODE***/
    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                //Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_AUDIO_PERMISSION);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        permissions,
                        REQUEST_RECORD_AUDIO_PERMISSION);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
            setRecordingAudioView();
            startRecording();
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        chronometer_recording.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        chronometer_recording.start();
        recorder.start();
    }


    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        resetTextingView();
        chronometer_recording.stop();
        chronometer_recording.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    private void resetTextingView() {
        isRecordedAudio = false;
        ll_recording_audio.setVisibility(View.GONE);
        img_record_audio.setVisibility(View.VISIBLE);
        et_comment.setText("");
        et_comment.setEnabled(true);
        img_camera.setClickable(true);
    }

    private void setRecordingAudioView() {
        isRecordedAudio = true;
        ll_recording_audio.setVisibility(View.VISIBLE);
        img_record_audio.setVisibility(View.GONE);

        et_comment.setText("Recording Audio...");
        et_comment.setEnabled(false);
        img_camera.setClickable(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) resetTextingView();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void UploadAudioToStorage() {
        progressHUD.show();
        final StorageReference ref = storageReference.child("gs://cooplas-be80d.appspot.com/chat_audios/audio_" + UUID.randomUUID().toString());
        Uri uri = Uri.fromFile(new File(fileName));
        ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String planImgLink = uri.toString();
                                sendMessage(Constants.TYPE_AUDIO, planImgLink,recordingElapsedMillis);
                                progressHUD.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Error in getting image link", Toast.LENGTH_SHORT).show();
                                progressHUD.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressHUD.dismiss();
            }
        });
    }

    private void MediaSelectionAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Media:");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.media_selection_dialog, null);
        builder.setView(customLayout);

        ImageView selection_img = customLayout.findViewById(R.id.selection_img);
        ImageView selection_video = customLayout.findViewById(R.id.selection_video);
        TextView image_tv = customLayout.findViewById(R.id.image_tv);
        TextView video_tv = customLayout.findViewById(R.id.video_tv);


        selection_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImages();
                alertDialog.dismiss();

            }
        });
        image_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection_img.callOnClick();
            }
        });

        selection_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickVideo();
                alertDialog.dismiss();
            }
        });
        video_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection_video.callOnClick();
            }
        });
        alertDialog = builder.show();
        alertDialog.show();
    }

    private void PickImages() {
        new net.alhazmy13.mediapicker.Image.ImagePicker.Builder(activity)
                .mode(net.alhazmy13.mediapicker.Image.ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(net.alhazmy13.mediapicker.Image.ImagePicker.ComperesLevel.NONE)
                .directory(net.alhazmy13.mediapicker.Image.ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.JPG)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }

    private void PickVideo() {
        new VideoPicker.Builder(activity)
                .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                .directory(VideoPicker.Directory.DEFAULT)
                .extension(VideoPicker.Extension.MP4)
                .enableDebuggingMode(true)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == net.alhazmy13.mediapicker.Image.ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(net.alhazmy13.mediapicker.Image.ImagePicker.EXTRA_IMAGE_PATH);
            String path = mPaths.get(0);
            progressHUD.show();
            final StorageReference ref = storageReference.child("gs://cooplas-be80d.appspot.com/chat_images/image_" + UUID.randomUUID().toString());
            Uri uri = Uri.fromFile(new File(path));
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String planImgLink = uri.toString();
                                    sendMessage(Constants.TYPE_IMAGE, planImgLink);
                                    progressHUD.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Error in getting image link", Toast.LENGTH_SHORT).show();
                                    progressHUD.dismiss();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressHUD.dismiss();
                }
            });
        }
        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {

            List<String> mPaths = data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            String path = mPaths.get(0);
            progressHUD.show();
            final StorageReference ref = storageReference.child("gs://cooplas-be80d.appspot.com/chat_videos/video_" + UUID.randomUUID().toString());
            Uri uri = Uri.fromFile(new File(path));
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String planImgLink = uri.toString();
                                    sendMessage(Constants.TYPE_VIDEO, planImgLink);
                                    progressHUD.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Error in getting image link", Toast.LENGTH_SHORT).show();
                                    progressHUD.dismiss();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressHUD.dismiss();
                }
            });
        }
    }
}