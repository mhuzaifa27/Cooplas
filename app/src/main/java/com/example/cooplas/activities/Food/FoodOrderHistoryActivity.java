package com.example.cooplas.activities.Food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Food.FoodCartAdapter;
import com.example.cooplas.adapters.Food.FoodOrderHistoryAdapter;
import com.example.cooplas.utils.CheckConnectivity;
import com.example.cooplas.utils.CheckInternetEvent;
import com.example.cooplas.utils.ShowDialogues;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FoodOrderHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FoodOrderHistoryActivity";
    private Context context=FoodOrderHistoryActivity.this;
    private Activity activity=FoodOrderHistoryActivity.this;

    private EventBus eventBus = EventBus.getDefault();
    private BroadcastReceiver mNetworkReceiver;
    private View parentLayout;


    private RecyclerView rv_order_history;
    private LinearLayoutManager layoutManager;
    private List<String> orderHistoryList =new ArrayList<>();
    private FoodOrderHistoryAdapter foodOrderHistoryAdapter;

    private TextView tv_title;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order_history);

        initComponents();

        orderHistoryList.add("d");
        orderHistoryList.add("d");
        orderHistoryList.add("d");
        orderHistoryList.add("d");
        orderHistoryList.add("d");

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initComponents() {
        eventBus.register(this);
        mNetworkReceiver = new CheckConnectivity();
        registerNetworkBroadcastForNougat();
        parentLayout = findViewById(android.R.id.content);

        resources=getResources();
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(resources.getString(R.string.order_history));

        rv_order_history =findViewById(R.id.rv_order_history);

        layoutManager=new LinearLayoutManager(context);
        rv_order_history.setLayoutManager(layoutManager);

        foodOrderHistoryAdapter =new FoodOrderHistoryAdapter(orderHistoryList,context);
        rv_order_history.setAdapter(foodOrderHistoryAdapter);
        rv_order_history.setFocusable(false);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

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