package com.example.cooplas.activities.Food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Food.FoodCartAdapter;
import com.example.cooplas.adapters.Food.FoodOrderHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class FoodOrderHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FoodOrderHistoryActivity";
    private Context context=FoodOrderHistoryActivity.this;
    private Activity activity=FoodOrderHistoryActivity.this;

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

    }

    private void initComponents() {
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
}