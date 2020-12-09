package com.example.cooplas.activities.Chat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cooplas.Firebase.AppState;
import com.example.cooplas.Firebase.ChangeEventListener;
import com.example.cooplas.Firebase.Models.Group;
import com.example.cooplas.Firebase.Models.Message;
import com.example.cooplas.Firebase.Models.User;
import com.example.cooplas.Firebase.Services.GroupLastMessageService;
import com.example.cooplas.Firebase.Services.GroupMessagingService;
import com.example.cooplas.Firebase.Services.GroupService;
import com.example.cooplas.Firebase.Services.SingleMessagingService;
import com.example.cooplas.R;
import com.example.cooplas.adapters.Chat.GroupMessagingAdapter;
import com.example.cooplas.models.Chat.InboxItem;
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

public class GroupMessagingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String TAG = "MessagingActivity";
    private Context context = GroupMessagingActivity.this;
    private Activity activity = GroupMessagingActivity.this;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_no;

    private RecyclerView rv_messaging;
    private GroupMessagingAdapter messagingAdapter;
    private LinearLayoutManager layoutManager;
    private List<Message> messagesList = new ArrayList<>();

    private String groupId, name, receiverPic;
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

    private GroupMessagingService groupMessagingService;
    private GroupLastMessageService groupLastMessageService;
    private ImageView iv_send, img_record_audio, img_cancel_audio, img_camera;
    private TextView tv_title, tv_cancel;
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

    private ImageView img_menu;
    private View header_normal, header_searching;

    private EditText et_search;
    private GroupService groupService;
    private boolean isAdminGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messaging);

        getIntentData();
        initComponents();

        groupLastMessageService = new GroupLastMessageService(groupId);
        groupLastMessageService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {
            }

            @Override
            public void onDataChanged() {
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


        groupService = new GroupService();
        groupService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {

            }

            @Override
            public void onDataChanged() {
                Group group = groupService.getGroupById(groupId);
                if (group != null) {
                    if (group.getAdmin().equalsIgnoreCase(AppState.currentFireUser.getUid())) {
                        isAdminGroup = true;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        groupMessagingService = new GroupMessagingService(groupId);
        groupMessagingService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {
            }

            @Override
            public void onDataChanged() {
                messagesList.clear();
                if (groupMessagingService.getCount() > 0) {
                    for (int i = 0; i < groupMessagingService.getCount(); i++) {
                        DataSnapshot sp = groupMessagingService.getItem(i);
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
        tv_cancel.setOnClickListener(this);
        img_menu.setOnClickListener(this);

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

        /*****Searching for Data******/
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        /*****************************/
    }

    private void filter(String text) {
        List<Message> tempMessages = new ArrayList<>();
        for (Message message : messagesList) {
            if (message.getMessage().toLowerCase().contains(text.toLowerCase())) {
                tempMessages.add(message);
            }
        }
        messagingAdapter.addAll(tempMessages);
    }

    private void getIntentData() {
        groupId = getIntent().getStringExtra(Constants.ID);
        name = getIntent().getStringExtra(Constants.START_NAME);
        String isNew = getIntent().getStringExtra(Constants.EXTRAS);

        if (isNew != null) {
            sendMessage(Constants.TYPE_TEXT, "This group is created by "
                    + FunctionsKt.getFirstName(context)
                    + " " + FunctionsKt.getLastName(context));
        }
    }

    private void initComponents() {
        Firebase.setAndroidContext(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        progressHUD = KProgressHUD.create(activity);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        rv_messaging = findViewById(R.id.rv_messaging);

        layoutManager = new LinearLayoutManager(context);
        messagingAdapter = new GroupMessagingAdapter(messagesList, context, activity);

        rv_messaging.setLayoutManager(layoutManager);
        rv_messaging.setAdapter(messagingAdapter);

        iv_send = findViewById(R.id.iv_send);
        img_cancel_audio = findViewById(R.id.img_cancel_audio);
        img_record_audio = findViewById(R.id.img_record_audio);
        img_camera = findViewById(R.id.img_camera);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(name);

        chronometer_recording = findViewById(R.id.chronometer_recording);

        et_comment = findViewById(R.id.et_comment);
        et_search = findViewById(R.id.et_search);

        ll_recording_audio = findViewById(R.id.ll_recording_audio);

        // Record to the external cache directory for visibility
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audio_" + UUID.randomUUID().toString() + ".3gp";

        img_menu = findViewById(R.id.img_menu);
        header_normal = findViewById(R.id.header_normal);
        header_searching = findViewById(R.id.header_searching);

        tv_cancel = findViewById(R.id.tv_cancel);
    }

    @Override
    public void onRefresh() {
        groupMessagingService = new GroupMessagingService(groupId);
        groupMessagingService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {

            }

            @Override
            public void onDataChanged() {
                messagesList.clear();
                if (groupMessagingService.getCount() > 0) {
                    for (int i = 0; i < groupMessagingService.getCount(); i++) {
                        DataSnapshot sp = groupMessagingService.getItem(i);
                        Message message = sp.getValue(Message.class);
                        if (!message.getUserId().equalsIgnoreCase(AppState.currentBpackCustomer.getUserId()))
                            messagesList.add(message);
                    }
                    messagingAdapter.addAll(messagesList);
                    rv_messaging.scrollToPosition(messagesList.size() - 1);
                    if (swipeRefreshLayout.isRefreshing())
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
            case R.id.tv_cancel:
                header_normal.setVisibility(View.VISIBLE);
                header_searching.setVisibility(View.GONE);
                break;
            case R.id.img_back:
            case R.id.img_back_search:
                onBackPressed();
                break;
            case R.id.img_menu:
                ShowMenu(img_menu);
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
        if (groupMessagingService == null) {
            groupMessagingService = new GroupMessagingService(groupId);
            groupMessagingService.setOnChangedListener(new ChangeEventListener() {
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
            groupMessagingService.sendMessage(message, groupId, type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    et_comment.setText("");
                    groupLastMessageService.updateLastMessage(message, AppState.currentBpackCustomer.getUserId(), type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            //Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            groupMessagingService.sendMessage(message, groupId, type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    et_comment.setText("");
                    groupLastMessageService.updateLastMessage(message, AppState.currentBpackCustomer.getUserId(), type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
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

    private void sendMessage(String type, String message, long recordingElapsedMillis) {
        //String message = et_comment.getText().toString();
        if (groupMessagingService == null) {
            groupMessagingService = new GroupMessagingService(groupId);
            groupMessagingService.setOnChangedListener(new ChangeEventListener() {
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
            groupMessagingService.sendMessage(message, recordingElapsedMillis, groupId, type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    et_comment.setText("");
                    groupLastMessageService.updateLastMessage(message, recordingElapsedMillis, AppState.currentBpackCustomer.getUserId(), type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        }
                    });
                }
            });
        } else {
            groupMessagingService.sendMessage(message, recordingElapsedMillis, groupId, type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    et_comment.setText("");
                    groupLastMessageService.updateLastMessage(message, recordingElapsedMillis, AppState.currentBpackCustomer.getUserId(), type, AppState.currentBpackCustomer.getUserId(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        }
                    });
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
                                sendMessage(Constants.TYPE_AUDIO, planImgLink, recordingElapsedMillis);
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
        new ImagePicker.Builder(activity)
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.NONE)
                .directory(ImagePicker.Directory.DEFAULT)
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
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
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

    @Override
    public void onBackPressed() {
        if (header_searching.getVisibility() == View.VISIBLE) {
            header_searching.setVisibility(View.GONE);
            header_normal.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    private void ShowMenu(View view) {
        PopupMenu popup = new PopupMenu(activity, view);
        if (isAdminGroup)
            popup.getMenuInflater().inflate(R.menu.my_group_chat_menu, popup.getMenu());
        else
            popup.getMenuInflater().inflate(R.menu.group_chat_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_group_info:
                case R.id.menu_shared_media:
                    Intent intent = new Intent(context, GroupInfoActivity.class);
                    intent.putExtra(Constants.ID, groupId);
                    intent.putExtra(Constants.START_NAME, name);
                    startActivity(intent);
                    break;
                case R.id.menu_search:
                    header_normal.setVisibility(View.GONE);
                    header_searching.setVisibility(View.VISIBLE);
                    break;
                case R.id.menu_leave:
                    leaveGroup();
                    break;
                case R.id.menu_delete_group:
                    deleteGroup();
                    break;
            }
            return true;
        });
        popup.show();
    }

    public void deleteGroup() {
        new androidx.appcompat.app.AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(R.string.delete_group)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressHUD.show();
                        groupMessagingService = new GroupMessagingService(groupId);
                        groupMessagingService.setOnChangedListener(new ChangeEventListener() {
                            @Override
                            public void onChildChanged(EventType type, int index, int oldIndex) {

                            }

                            @Override
                            public void onDataChanged() {
                                groupMessagingService.deleteGroup(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        groupLastMessageService = new GroupLastMessageService(groupId);
                                        groupLastMessageService.setOnChangedListener(new ChangeEventListener() {
                                            @Override
                                            public void onChildChanged(EventType type, int index, int oldIndex) {

                                            }

                                            @Override
                                            public void onDataChanged() {
                                                groupLastMessageService.deleteGroup(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                        groupService = new GroupService();
                                                        groupService.setOnChangedListener(new ChangeEventListener() {
                                                            @Override
                                                            public void onChildChanged(EventType type, int index, int oldIndex) {

                                                            }

                                                            @Override
                                                            public void onDataChanged() {
                                                                groupService.deleteGroup(groupId, new DatabaseReference.CompletionListener() {
                                                                    @Override
                                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                        progressHUD.dismiss();
                                                                        finish();
//                                                                        Toast.makeText(GroupMessagingActivity.this, "Group Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError error) {
                                                                progressHUD.dismiss();
                                                            }
                                                        });
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                                progressHUD.dismiss();
                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                progressHUD.dismiss();
                            }
                        });
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void leaveGroup() {
        new androidx.appcompat.app.AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage("Are you sure you want to leave this group?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        groupService = new GroupService();
                        groupService.setOnChangedListener(new ChangeEventListener() {
                            @Override
                            public void onChildChanged(EventType type, int index, int oldIndex) {

                            }

                            @Override
                            public void onDataChanged() {
                                if (groupService.getCount() > 0) {
                                    Group group = groupService.getGroupById(groupId);
                                    if (group != null) {
                                        List<String> members = group.getMembersList();
                                        if (members.size() > 0) {
                                            for (int i = 0; i < members.size(); i++) {
                                                if (members.get(i).equalsIgnoreCase(AppState.currentFireUser.getUid())) {
                                                    members.remove(i);
                                                }
                                            }
                                            for (int i = 0; i < groupService.getCount(); i++) {
                                                DataSnapshot snapshot1 = groupService.getItem(i);
                                                String key = snapshot1.getKey();
                                                if (key.equalsIgnoreCase(groupId)) {
                                                    DataSnapshot snapshot2 = snapshot1.child("membersList");
                                                    snapshot2.getRef().setValue(members);
                                                }
                                            }
                                            finish();
                                        }
                                    }
                                }
                                   /* for (int i = 0; i < groupService.getCount(); i++) {
                                        DataSnapshot snapshot1 = groupService.getItem(i);
                                        String key = snapshot1.getKey();
                                        if (key.equalsIgnoreCase(groupId)) {
                                            DataSnapshot snapshot2 = snapshot1.child("membersList");
                                            int count = 0;
                                            int deletedIndex = -1;
                                            for (DataSnapshot sp : snapshot2.getChildren()) {
                                                String id = sp.getValue(String.class);
                                                if (id.equalsIgnoreCase(AppState.currentFireUser.getUid())) {
                                                    deletedIndex = Integer.parseInt(sp.getRef().getKey());
                                                    sp.getRef().setValue(null);
                                                    count++;
                                                }
                                                if (count > 0 && deletedIndex > -1) {
                                                    if(snapshot2.getChildrenCount()>0){
                                                        for (DataSnapshot sp2 : snapshot2.getChildren()) {
                                                            int nodeKey = Integer.parseInt(sp2.getKey());
                                                            if (nodeKey >= deletedIndex) {

                                                            }
                                                        }
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }*/
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
                    }
                })
                .

                        setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .

                        show();
    }

}