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

import java.util.ArrayList;
import java.util.List;

public class FoodCartActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FoodCartActivity";
    private Context context=FoodCartActivity.this;
    private Activity activity=FoodCartActivity.this;

    private RecyclerView rv_cart;
    private LinearLayoutManager layoutManager;
    private List<String> cartList=new ArrayList<>();
    private FoodCartAdapter foodCartAdapter;

    private TextView tv_title;
    private Resources resources;
    private Button btn_checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_cart);

        initComponents();

        cartList.add("d");
        cartList.add("d");
        cartList.add("d");
        cartList.add("d");
        cartList.add("d");

        btn_checkout.setOnClickListener(this);
    }

    private void initComponents() {
        resources=getResources();
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(resources.getString(R.string.cart));

        rv_cart=findViewById(R.id.rv_cart);

        layoutManager=new LinearLayoutManager(context);
        rv_cart.setLayoutManager(layoutManager);

        foodCartAdapter=new FoodCartAdapter(cartList,context);
        rv_cart.setAdapter(foodCartAdapter);
        rv_cart.setFocusable(false);

        btn_checkout=findViewById(R.id.btn_checkout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_checkout:
                startActivity(new Intent(context,CheckoutActivity.class));
                break;
        }
    }
}