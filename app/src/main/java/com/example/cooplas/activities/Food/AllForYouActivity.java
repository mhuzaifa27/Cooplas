package com.example.cooplas.activities.Food;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Food.FoodForYouAdapter;
import com.example.cooplas.models.Food.Callbacks.CallbackGetFoodForYou;
import com.example.cooplas.models.Food.ForYou;
import com.example.cooplas.utils.CheckConnectivity;
import com.example.cooplas.utils.CheckInternetEvent;
import com.example.cooplas.utils.ShowDialogues;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllForYouActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AllForYouActivity";
    private Context context = AllForYouActivity.this;
    private Activity activity = AllForYouActivity.this;

    private EventBus eventBus = EventBus.getDefault();
    private BroadcastReceiver mNetworkReceiver;
    private View parentLayout;

    private RecyclerView rv_for_you;
    private LinearLayoutManager layoutManagerForYou;

    private List<ForYou> forYouList = new ArrayList<>();

    private FoodForYouAdapter foodForYouAdapter;

    private TextView tv_title;
    private ImageView img_cart,img_back;

    private KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_for_you);

        initComponents();
        getFoodData();

        img_back.setOnClickListener(this);
    }

    private void getFoodData() {
          /*if (swipeRefreshCheck == true) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            progressHUD.show();
        }*/
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<CallbackGetFoodForYou> call = apiInterface.getFoodForYou("Bearer " + accessToken);
        call.enqueue(new Callback<CallbackGetFoodForYou>() {
            @Override
            public void onResponse(Call<CallbackGetFoodForYou> call, Response<CallbackGetFoodForYou> response) {
                CallbackGetFoodForYou responseGetPopularRestaurants = response.body();
                Log.d(TAG, "onResponse: " + response);
                if (responseGetPopularRestaurants != null) {
                    if (responseGetPopularRestaurants.getSuccess()) {
                        if (responseGetPopularRestaurants.getFor_you().size() > 0)
                            forYouList = responseGetPopularRestaurants.getFor_you();
                        setData();
                    } else {
                        progressHUD.dismiss();
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressHUD.dismiss();
                    ShowDialogues.SHOW_SERVER_ERROR_DIALOG(context);
                }
            }

            @Override
            public void onFailure(Call<CallbackGetFoodForYou> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    progressHUD.dismiss();
                }
            }
        });
    }
    private void setData() {
        foodForYouAdapter.addAll(forYouList);
        progressHUD.dismiss();
    }

    private void initComponents() {
        progressHUD = KProgressHUD.create(activity);
        eventBus.register(this);
        mNetworkReceiver = new CheckConnectivity();
        registerNetworkBroadcastForNougat();
        parentLayout = findViewById(android.R.id.content);

        img_cart = findViewById(R.id.img_cart);
        img_cart.setVisibility(View.VISIBLE);
        img_back=findViewById(R.id.img_back);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.for_you);

        rv_for_you = findViewById(R.id.rv_for_you);

        layoutManagerForYou = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rv_for_you.setLayoutManager(layoutManagerForYou);

        foodForYouAdapter=new FoodForYouAdapter(forYouList,context);
        rv_for_you.setAdapter(foodForYouAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
        unregisterNetworkChanges();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CheckInternetEvent event) {
        Log.d("SsS", "checkInternetAvailability: called");
        if (event.isIS_INTERNET_AVAILABLE()) {
            ShowDialogues.SHOW_SNACK_BAR(parentLayout, activity, getString(R.string.snackbar_internet_available));

        } else {
            ShowDialogues.SHOW_SNACK_BAR(parentLayout, activity, getString(R.string.snackbar_check_internet));
        }
    }
}