package com.example.cooplas.activities.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooplas.R;
import com.example.cooplas.adapters.HomeCreatePostImagesAdapter;
import com.example.cooplas.events.home.HomeRefreshFeedEvent;
import com.example.cooplas.models.home.MediaTypeModel;
import com.example.cooplas.models.home.createPost.CreatePostModel;
import com.example.cooplas.models.home.singlePost.Medium;
import com.example.cooplas.models.home.singlePost.Post;
import com.example.cooplas.models.home.singlePost.SinglePostMainModel;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.gson.Gson;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPostActivity extends AppCompatActivity {


    private HomeCreatePostImagesAdapter adapter;
    private String descriptionVal;
    private AlertDialog alertDialog;
    private ArrayList<MediaTypeModel> listMedia = new ArrayList<>();
    String postID;
    private EditText description_et;
    public static StringBuilder stringBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        stringBuilder = new StringBuilder();

        Intent intent = getIntent();
        postID = intent.getStringExtra("postID");


        RelativeLayout back_img = findViewById(R.id.rl_back);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TextView username = findViewById(R.id.textView);
        description_et = findViewById(R.id.description_et);
        ImageView profile_image = findViewById(R.id.profile_image);
        username.setText(FunctionsKt.getUserName(getApplicationContext()));

        Picasso.get().load(FunctionsKt.getImage(getApplicationContext())).fit().centerCrop().into(profile_image);


        EditText description_et = findViewById(R.id.description_et);

        TextView post_tv = findViewById(R.id.post_tv);
        post_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                descriptionVal = description_et.getText().toString().trim();

                if (descriptionVal.isEmpty()) {
                    Toast.makeText(EditPostActivity.this, "Please enter description", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (topicVal.isEmpty()) {
//                    Toast.makeText(CreateProfilePost.this, "Please enter Topic", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                uploadPost();
            }
        });


        TextView selectImages = findViewById(R.id.textView2);
        selectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaSelectionAlert();

            }
        });


        getPosts();
    }

    private void getPosts() {

        KProgressHUD progressHUD = KProgressHUD.create(EditPostActivity.this).show();
        progressHUD.show();

        String accessToken = FunctionsKt.getAccessToken(getApplicationContext());
        APIInterface apiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);
        Call<SinglePostMainModel> call = apiInterface.getSinglePost("Bearer " + accessToken, postID);
        call.enqueue(new Callback<SinglePostMainModel>() {
            @Override
            public void onResponse(Call<SinglePostMainModel> call, Response<SinglePostMainModel> response) {
                Log.d("getSinglePost", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    SinglePostMainModel model = response.body();


                    Post singlePost = model.getPost();
                    List<Medium> listMedium = singlePost.getMedia();


                    description_et.setText(singlePost.getBody());

                    if (listMedium.size() > 0) {


                        for (int i = 0; i < listMedium.size(); i++) {

                            int id = listMedium.get(i).getId();
                            String path = listMedium.get(i).getPath();
                            String type = listMedium.get(i).getType();
                            String isDel = "false";


                            MediaTypeModel model1 = new MediaTypeModel();
                            model1.setId(String.valueOf(id));
                            model1.setPath(path);
                            model1.setType(type);

                            listMedia.add(model1);

                        }
                    }
                    populateRecyclerView();
                }


            }


            @Override
            public void onFailure(Call<SinglePostMainModel> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }

    private void populateRecyclerView() {

        RecyclerView imagePickerRecyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        imagePickerRecyclerView.setLayoutManager(layoutManager);

        adapter = new HomeCreatePostImagesAdapter(EditPostActivity.this, listMedia, 0);
        imagePickerRecyclerView.setAdapter(adapter);

    }

    private void MediaSelectionAlert() {


        AlertDialog.Builder builder = new AlertDialog.Builder(EditPostActivity.this);
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
        new net.alhazmy13.mediapicker.Image.ImagePicker.Builder(EditPostActivity.this)
                .mode(net.alhazmy13.mediapicker.Image.ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(net.alhazmy13.mediapicker.Image.ImagePicker.ComperesLevel.NONE)
                .directory(net.alhazmy13.mediapicker.Image.ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.JPG)
                .scale(500, 500)
                .allowMultipleImages(true)
                .enableDebuggingMode(true)
                .build();
    }

    private void PickVideo() {

        new VideoPicker.Builder(EditPostActivity.this)
                .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                .directory(VideoPicker.Directory.DEFAULT)
                .extension(VideoPicker.Extension.MP4)
                .enableDebuggingMode(true)
                .build();
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private void uploadPost() {

        KProgressHUD progressHUD = KProgressHUD.create(EditPostActivity.this).show();
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(EditPostActivity.this);
        APIInterface apiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);


        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);


        for (int i = 0; i < listMedia.size(); i++) {

            if (listMedia.get(i).getType().equals("vid")) {
                File file = new File(listMedia.get(i).getPath());
                String mimeType = getMimeType(listMedia.get(i).getPath());
                RequestBody requestImage = RequestBody.create(MediaType.parse(mimeType), file);
                builder.addFormDataPart("new_media[]", file.getName(), RequestBody.create(MediaType.parse(mimeType), file));
            } else {

                if (listMedia.get(i).getPath().contains("http")) {


                } else {
                    String mimeType = getMimeType(listMedia.get(i).getPath());
                    File file = new File(listMedia.get(i).getPath());
                    RequestBody requestImage = RequestBody.create(MediaType.parse(mimeType), file);
//            builder.addFormDataPart("files[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                    builder.addFormDataPart("new_media[]", file.getName(), RequestBody.create(MediaType.parse(mimeType), file));
                }


            }
        }
        builder.addFormDataPart("body", descriptionVal);

        if (stringBuilder.length() > 0) {

            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            builder.addFormDataPart("delete_media_ids", String.valueOf(stringBuilder));
        }


        MultipartBody requestBody = builder.build();
        Call<CreatePostModel> call = apiInterface.updatePost("Bearer " + accessToken, postID, requestBody);
        call.enqueue(new Callback<CreatePostModel>() {
            @Override
            public void onResponse(Call<CreatePostModel> call, Response<CreatePostModel> response) {
                Log.d("postUpload", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(EditPostActivity.this, "Profile Updated successfully", Toast.LENGTH_SHORT).show();

                    stringBuilder.delete(0, stringBuilder.length());

                    // send noti
                    EventBus.getDefault().post(new HomeRefreshFeedEvent());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CreatePostModel> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == net.alhazmy13.mediapicker.Image.ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
//            if (listMedia.size() > 0) {
//                listMedia.clear();
//            }
            List<String> mPaths = data.getStringArrayListExtra(net.alhazmy13.mediapicker.Image.ImagePicker.EXTRA_IMAGE_PATH);
            for (int i = 0; i < mPaths.size(); i++) {
                Log.d("imagePaths", "" + mPaths.get(i));
                String path = mPaths.get(i);

                MediaTypeModel model = new MediaTypeModel();
                model.setPath(path);
                model.setType("img");


                listMedia.add(model);
            }
            adapter.notifyDataSetChanged();
        }

        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            if (listMedia.size() > 0) {
                listMedia.clear();
            }
            List<String> mPaths = data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            String path = mPaths.get(0);
            MediaTypeModel model = new MediaTypeModel();
            model.setPath(path);
            model.setType("vid");
            listMedia.add(model);

            adapter.notifyDataSetChanged();
        }


    }
}