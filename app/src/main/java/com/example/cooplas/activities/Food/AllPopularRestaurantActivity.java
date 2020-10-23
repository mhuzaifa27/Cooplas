package com.example.cooplas.activities.Food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Food.FoodCategoriesAdapter;
import com.example.cooplas.adapters.Food.FoodForYouAdapter;
import com.example.cooplas.adapters.Food.FoodNearYouAdapter;
import com.example.cooplas.adapters.Food.FoodPopularRestaurantAdapter;

import java.util.ArrayList;
import java.util.List;

public class AllPopularRestaurantActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FoodActivity";
    private Context context= AllPopularRestaurantActivity.this;
    private Activity activity= AllPopularRestaurantActivity.this;

    private RecyclerView rv_popular_restaurants;
    private LinearLayoutManager layoutManagerPopularRestaurant;

    private List<String> popularRestaurantList=new ArrayList<>();

    private FoodPopularRestaurantAdapter foodPopularRestaurantAdapter;

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_popular_restaurant);

        initComponents();
        getFoodData();

    }

    private void getFoodData() {
        popularRestaurantList.add("1");
        popularRestaurantList.add("1");
        popularRestaurantList.add("1");
        popularRestaurantList.add("1");
        popularRestaurantList.add("1");
        popularRestaurantList.add("1");
        popularRestaurantList.add("1");
        popularRestaurantList.add("1");
        popularRestaurantList.add("1");
    }
    private void initComponents() {
        tv_title =findViewById(R.id.tv_title);
        tv_title.setText("Popular Restaurant");

        rv_popular_restaurants=findViewById(R.id.rv_popular_restaurants);

        layoutManagerPopularRestaurant=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        rv_popular_restaurants.setLayoutManager(layoutManagerPopularRestaurant);

        foodPopularRestaurantAdapter=new FoodPopularRestaurantAdapter(popularRestaurantList,context);
        rv_popular_restaurants.setAdapter(foodPopularRestaurantAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}