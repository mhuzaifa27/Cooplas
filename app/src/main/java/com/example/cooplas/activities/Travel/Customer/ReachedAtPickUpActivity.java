package com.example.cooplas.activities.Travel.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cooplas.R;
import com.example.cooplas.models.Travel.Customer.CallbackAcceptRide.CallbackAcceptRide;
import com.example.cooplas.models.Travel.Customer.CallbackDriverReached.CallbackDriverReached;
import com.example.cooplas.utils.Constants;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReachedAtPickUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ReachedAtPickUpActivity";
    private Context context = ReachedAtPickUpActivity.this;
    private Activity activity = ReachedAtPickUpActivity.this;

    private TextView tv_title;
    private RelativeLayout rl_reached;
    private KProgressHUD progressHUD;
    private String rideId;
    private TextView tv_vehicle_maker, tv_vehicle_reg_num, tv_countdown, tv_driver_name;
    private CircleImageView img_driver;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reached_at_pick_up);

        getIntentData();
        initComponents();
        driverReached();

        rl_reached.setOnClickListener(this::onClick);
    }

    private void getIntentData() {
        rideId = getIntent().getStringExtra(Constants.RIDE_ID);
    }

    private void driverReached() {
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<CallbackDriverReached> call = apiInterface.driverReached("Bearer EZ8OOnBW4JAvsCffaZBJKX9sygyMSH9V4xYAXvDQKj6A6sqXzBC3BbVD0mrH", rideId);
        call.enqueue(new Callback<CallbackDriverReached>() {
            @Override
            public void onResponse(Call<CallbackDriverReached> call, Response<CallbackDriverReached> response) {
                CallbackDriverReached responseAcceptRide = response.body();
                if (responseAcceptRide != null) {
                    setData(responseAcceptRide);
                }
            }

            @Override
            public void onFailure(Call<CallbackDriverReached> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    progressHUD.dismiss();
                }
            }
        });
    }

    private void setData(CallbackDriverReached response) {
        countDownTimer= new CountDownTimer(600000, 1000) {
            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                tv_countdown.setText(text);
            }

            public void onFinish() {
                setResult(RESULT_CANCELED);
                finish();
            }
        }.start();
        tv_driver_name.setText(response.getRide().getDriver().getFirstName() + " " + response.getRide().getDriver().getLastName());
        tv_vehicle_maker.setText(response.getRide().getVehicle().getMaker() + "-" + response.getRide().getVehicle().getModel());
        tv_vehicle_reg_num.setText(response.getRide().getVehicle().getRegistrationNumber());

        if (response.getRide().getDriver().getProfilePic() != null) {
            Glide
                    .with(context)
                    .load(response.getRide().getDriver().getProfilePic())
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_place_holder_image)
                    .into(img_driver);
        }
        progressHUD.dismiss();
    }

    private void initComponents() {
        progressHUD = KProgressHUD.create(activity);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.vehicle_is_waiting_for_you);
        tv_vehicle_maker = findViewById(R.id.tv_vehicle_maker);
        tv_vehicle_reg_num = findViewById(R.id.tv_vehicle_reg_num);
        tv_driver_name = findViewById(R.id.tv_driver_name);
        tv_countdown = findViewById(R.id.tv_countdown);

        img_driver=findViewById(R.id.img_driver);

        rl_reached = findViewById(R.id.rl_reached);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_reached:
                countDownTimer.cancel();
                setResult(RESULT_OK);
                finish();
                //startActivity(new Intent(context, RideCompleteActivity.class));
                break;
        }
    }
}