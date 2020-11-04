package com.example.cooplas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cooplas.R;
import com.example.cooplas.models.GeneralRes;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.gson.Gson;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterNewPassword extends AppCompatActivity {

    private RelativeLayout contine;
    private String pass, confirmPass;
    private EditText et_pass, et_confirm_pass;
    KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_new_password);

        contine = findViewById(R.id.rl_next);


        et_pass = findViewById(R.id.et_pass);
        et_confirm_pass = findViewById(R.id.et_confirm_pass);


        contine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pass = et_pass.getText().toString().trim();
                confirmPass = et_confirm_pass.getText().toString().trim();

                if (pass.isEmpty()) {
                    Toast.makeText(EnterNewPassword.this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (pass.length() < 6) {
                    Toast.makeText(EnterNewPassword.this, "New Password Length should be greater than 6", Toast.LENGTH_SHORT).show();
                    return;

                }


                if (confirmPass.isEmpty()) {
                    Toast.makeText(EnterNewPassword.this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (confirmPass.length() < 6) {
                    Toast.makeText(EnterNewPassword.this, "Confirm Password Length should be greater than 6", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (!confirmPass.equalsIgnoreCase(pass)) {
                    Toast.makeText(EnterNewPassword.this, "New and Confirm Password does not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                resetPass(pass);
            }
        });


    }


    private void resetPass(String pass) {

        progressHUD = KProgressHUD.create(EnterNewPassword.this).show();
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(EnterNewPassword.this);

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<GeneralRes> call = apiInterface.changeForgetPassword("Bearer " + accessToken, String.valueOf(pass));
        call.enqueue(new Callback<GeneralRes>() {
            @Override
            public void onResponse(Call<GeneralRes> call, Response<GeneralRes> response) {
                Log.d("postShare", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    GeneralRes model = response.body();


                    Toast.makeText(EnterNewPassword.this, "" + model.getMessage().toString(), Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(EnterNewPassword.this, SigninSignupScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);


                }
            }

            @Override
            public void onFailure(Call<GeneralRes> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }
}