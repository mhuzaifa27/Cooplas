package com.example.cooplas.activities.Food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Food.OrderedListAdapter;
import com.example.cooplas.adapters.Food.RestaurantMenuAdapter;
import com.example.cooplas.adapters.Food.RestaurantReviewsAdapter;
import com.example.cooplas.utils.CheckConnectivity;
import com.example.cooplas.utils.CheckInternetEvent;
import com.example.cooplas.utils.ShowDialogues;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FoodOrderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FoodOrderDetailActivity";
    private Context context= FoodOrderDetailActivity.this;
    private Activity activity= FoodOrderDetailActivity.this;

    private EventBus eventBus = EventBus.getDefault();
    private BroadcastReceiver mNetworkReceiver;
    private View parentLayout;


    private RecyclerView rv_order_list;
    private List<String> orderList =new ArrayList<>();
    private LinearLayoutManager menuLayoutManager;

    private OrderedListAdapter orderedListAdapter;

    private TextView tv_title;
    private RelativeLayout rl_give_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order_detail);

        initComponents();

        orderList.add("2");
        orderList.add("2");
        orderList.add("2");
        orderList.add("2");

        rl_give_review.setOnClickListener(this::onClick);
    }

    private void initComponents() {
        eventBus.register(this);
        mNetworkReceiver = new CheckConnectivity();
        registerNetworkBroadcastForNougat();
        parentLayout = findViewById(android.R.id.content);

        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(R.string.order_details);

        rv_order_list=findViewById(R.id.rv_order_list);
        menuLayoutManager=new LinearLayoutManager(context);
        orderedListAdapter=new OrderedListAdapter(orderList,context);
        rv_order_list.setLayoutManager(menuLayoutManager);
        rv_order_list.setAdapter(orderedListAdapter);

        rl_give_review=findViewById(R.id.rl_give_review);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_give_review:
                startActivity(new Intent(context,ReviewRestaurantsActivity.class));
                break;
        }
    }
}