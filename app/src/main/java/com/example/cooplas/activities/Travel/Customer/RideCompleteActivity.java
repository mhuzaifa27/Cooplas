package com.example.cooplas.activities.Travel.Customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cooplas.R;
import com.example.cooplas.models.Travel.Customer.CallbackAcceptRide.CallbackAcceptRide;
import com.example.cooplas.models.Travel.Customer.CallbackSearchForVehicle.CallbackSearchForVehicle;
import com.example.cooplas.utils.Constants;
import com.example.cooplas.utils.ShowDialogues;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideCompleteActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RideCompleteActivity";
    private Context context=RideCompleteActivity.this;
    private Activity activity=RideCompleteActivity.this;

    private RelativeLayout rl_submit;
    private CallbackAcceptRide callbackAcceptRide;
    private CircleImageView img_driver;
    private TextView tv_driver_name;
    private ScaleRatingBar srb_driver_rating;
    private EditText et_comments;
    private KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_complete);

        initComponents();
        getIntentData();

        rl_submit.setOnClickListener(this::onClick);
    }

    private void getIntentData() {
        callbackAcceptRide=(CallbackAcceptRide) getIntent().getSerializableExtra(Constants.CREATE_RIDE_OBJ);

        tv_driver_name.setText(callbackAcceptRide.getDriver().getFirstName()+" "+callbackAcceptRide.getDriver().getLastName());
        if (callbackAcceptRide.getDriver().getProfilePic() != null) {
            Glide
                    .with(context)
                    .load(callbackAcceptRide.getDriver().getProfilePic())
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_dummy_user)
                    .into(img_driver);
        }
    }

    private void initComponents() {
        progressHUD = KProgressHUD.create(activity);
        rl_submit=findViewById(R.id.rl_submit);

        tv_driver_name=findViewById(R.id.tv_driver_name);

        img_driver=findViewById(R.id.img_driver);

        srb_driver_rating=findViewById(R.id.srb_driver_rating);

        et_comments=findViewById(R.id.et_comments);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_submit:
                if(et_comments.getText().toString().isEmpty())
                    et_comments.setError("field required!");
                else if(srb_driver_rating.getRating()==0)
                    Toast.makeText(context, "Please rate driver!", Toast.LENGTH_SHORT).show();
                else{
                    Map<String,String> params=new HashMap<>();
                    params.put(Constants.RIDE_ID,callbackAcceptRide.getRide().getId().toString());
                    params.put(Constants.USER_COMMENTS,et_comments.getText().toString());
                    params.put(Constants.USER_STARS,String.valueOf(srb_driver_rating.getRating()));
                    submitDriverReview(params);
                }
                //startActivity(new Intent(context,RidesHistoryActivity.class));
                break;
        }
    }

    private void submitDriverReview(Map<String, String> params) {
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<ResponseBody> call = apiInterface.rateDriver("Bearer " + accessToken, params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseUpdateCarType = response.body();
                if (responseUpdateCarType != null) {
                    progressHUD.dismiss();
                    setResult(RESULT_OK);
                    onBackPressed();
                }
                else {
                    Log.d(TAG, "onResponse: error");
                    progressHUD.dismiss();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    progressHUD.dismiss();
                    ShowDialogues.SHOW_SERVER_ERROR_DIALOG(context);
                }
            }
        });
    }
}