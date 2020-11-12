package com.example.cooplas.activities.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cooplas.R;
import com.example.cooplas.events.VerifyEvent;
import com.example.cooplas.utils.CircleTransform;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileScreen extends AppCompatActivity {


    private EditText et_first_name, et_last_name, et_user_name;
    private String firstName, lastName, userName;
    private ImageView iv_camera, iv_profile;
    private File imageFile;

    private boolean imageSelected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);


        iv_profile = findViewById(R.id.iv_profile);

        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_user_name = findViewById(R.id.et_user_name);

        et_first_name.setText(FunctionsKt.getFirstName(getApplicationContext()));
        et_last_name.setText(FunctionsKt.getLastName(getApplicationContext()));
        et_user_name.setText(FunctionsKt.getUserName(getApplicationContext()));

        TextView tv_female = findViewById(R.id.tv_female);
        TextView tv_male = findViewById(R.id.tv_male);


        Picasso.get().load(FunctionsKt.getImage(getApplicationContext())).transform(new CircleTransform()).fit().centerCrop().into(iv_profile);

        RelativeLayout rl_female = findViewById(R.id.rl_female);
        RelativeLayout rl_male = findViewById(R.id.rl_male);


        iv_camera = findViewById(R.id.iv_camera);

        String gender = FunctionsKt.getGener(getApplicationContext());
        if (gender.equalsIgnoreCase("male")) {
            rl_male.setBackground(getResources().getDrawable(R.drawable.round_orange));
            rl_female.setBackground(getResources().getDrawable(R.drawable.round_grey));

            tv_male.setTextColor(getResources().getColor(R.color.black));
            tv_female.setTextColor(getResources().getColor(R.color.black));
        } else {
            rl_female.setBackground(getResources().getDrawable(R.drawable.round_orange));
            rl_male.setBackground(getResources().getDrawable(R.drawable.round_grey));
            tv_female.setTextColor(getResources().getColor(R.color.black));
            tv_male.setTextColor(getResources().getColor(R.color.black));
        }


        rl_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rl_female.setBackground(getResources().getDrawable(R.drawable.round_orange));
                rl_male.setBackground(getResources().getDrawable(R.drawable.round_grey));
                tv_female.setTextColor(getResources().getColor(R.color.black));
                tv_male.setTextColor(getResources().getColor(R.color.black));

            }
        });
        rl_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_male.setBackground(getResources().getDrawable(R.drawable.round_orange));
                rl_female.setBackground(getResources().getDrawable(R.drawable.round_grey));

                tv_male.setTextColor(getResources().getColor(R.color.black));
                tv_female.setTextColor(getResources().getColor(R.color.black));

            }
        });

        RelativeLayout rl_save = findViewById(R.id.rl_save);
        rl_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();

            }
        });


        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_camera.callOnClick();
            }
        });
        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(EditProfileScreen.this)
                        .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

                            ImagePicker.Companion.with(EditProfileScreen.this)
//                                    .crop()	    			//Crop image(Optional), Check Customization for more option
                                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                                    .maxResultSize(600, 600)    //Final image resolution will be less than 1080 x 1080(Optional)
                                    .start();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        token.continuePermissionRequest();


                    }
                }).check();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK


            Uri fileUris = data.getData();
            iv_profile.setImageURI(fileUris);
            imageFile = new File(fileUris.getPath());

            imageSelected = true;
            Log.d("TAG", "onActivityResult: ");

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }


    }


    private void validateData() {

        firstName = et_first_name.getText().toString().trim();
        lastName = et_last_name.getText().toString().trim();
        userName = et_user_name.getText().toString().trim();


        if (firstName.isEmpty()) {
            Toast.makeText(this, "Please Enter First Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (lastName.isEmpty()) {
            Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please Enter User Name", Toast.LENGTH_SHORT).show();
            return;
        }

        updateProfile();
    }

    private void updateProfile() {

        KProgressHUD progressHUD = KProgressHUD.create(EditProfileScreen.this).show();
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(EditProfileScreen.this);
        APIInterface apiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);


        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);


        builder.addFormDataPart("first_name", firstName);
        builder.addFormDataPart("last_name", lastName);
        builder.addFormDataPart("username", userName);


        if (imageSelected == true) {


            String mimeType = getMimeType(imageFile.getPath());
            File file = new File(imageFile.getPath());
            RequestBody requestImage = RequestBody.create(MediaType.parse(mimeType), file);
//            builder.addFormDataPart("files[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            builder.addFormDataPart("profile_pic", file.getName(), RequestBody.create(MediaType.parse(mimeType), file));

        }


        MultipartBody requestBody = builder.build();
        Call<JsonObject> call = apiInterface.updateProfile("Bearer " + accessToken, requestBody);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("postUpload", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileScreen.this, "Your Profile Is Updated Successfully", Toast.LENGTH_SHORT).show();
                }

                EventBus.getDefault().post(new VerifyEvent());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
