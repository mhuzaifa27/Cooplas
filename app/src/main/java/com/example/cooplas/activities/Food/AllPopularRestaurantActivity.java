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
import com.example.cooplas.adapters.Food.FoodPopularRestaurantAdapter;
import com.example.cooplas.models.Food.PopularRestaurant;
import com.example.cooplas.models.Food.Callbacks.CallbackGetPopularRestaurants;
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

public class AllPopularRestaurantActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FoodActivity";
    private Context context = AllPopularRestaurantActivity.this;
    private Activity activity = AllPopularRestaurantActivity.this;

    private EventBus eventBus = EventBus.getDefault();
    private BroadcastReceiver mNetworkReceiver;
    private View parentLayout;

    private RecyclerView rv_popular_restaurants;
    private LinearLayoutManager layoutManagerPopularRestaurant;

    private List<PopularRestaurant> popularRestaurantList = new ArrayList<>();

    private FoodPopularRestaurantAdapter foodPopularRestaurantAdapter;

    private TextView tv_title;
    private ImageView img_back;

    private KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_popular_restaurant);

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
        Call<CallbackGetPopularRestaurants> call = apiInterface.getPopularRestaurants("Bearer " + accessToken);
        call.enqueue(new Callback<CallbackGetPopularRestaurants>() {
            @Override
            public void onResponse(Call<CallbackGetPopularRestaurants> call, Response<CallbackGetPopularRestaurants> response) {
                CallbackGetPopularRestaurants responseGetPopularRestaurants = response.body();
                Log.d(TAG, "onResponse: " + response);
                if (responseGetPopularRestaurants != null) {
                    if (responseGetPopularRestaurants.getSuccess()) {
                        if (responseGetPopularRestaurants.getPopularRestaurants().size() > 0)
                            popularRestaurantList = responseGetPopularRestaurants.getPopularRestaurants();
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
            public void onFailure(Call<CallbackGetPopularRestaurants> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    progressHUD.dismiss();
                }
            }
        });
    }

    private void setData() {
        foodPopularRestaurantAdapter.addAll(popularRestaurantList);
        progressHUD.dismiss();
    }

    private void initComponents() {
        progressHUD = KProgressHUD.create(activity);

        eventBus.register(this);
        mNetworkReceiver = new CheckConnectivity();
        registerNetworkBroadcastForNougat();
        parentLayout = findViewById(android.R.id.content);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("Popular Restaurant");

        img_back=findViewById(R.id.img_back);

        rv_popular_restaurants = findViewById(R.id.rv_popular_restaurants);

        layoutManagerPopularRestaurant = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rv_popular_restaurants.setLayoutManager(layoutManagerPopularRestaurant);

        foodPopularRestaurantAdapter = new FoodPopularRestaurantAdapter(popularRestaurantList, context);
        rv_popular_restaurants.setAdapter(foodPopularRestaurantAdapter);
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