package com.example.cooplas.activities.Food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Food.FoodCartAdapter;
import com.example.cooplas.adapters.Food.FoodCategoriesAdapter;
import com.example.cooplas.adapters.Food.FoodForYouAdapter;
import com.example.cooplas.adapters.Food.FoodNearYouAdapter;
import com.example.cooplas.adapters.Food.FoodPopularRestaurantAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FoodActivity";
    private Context context=FoodActivity.this;
    private Activity activity=FoodActivity.this;
    private DrawerLayout drawer_food;

    private RecyclerView rv_category,rv_for_you,rv_near_you,rv_popular_restaurants;
    private LinearLayoutManager layoutManagerCategory,layoutManagerForYou,layoutManagerNearYou,layoutManagerPopularRestaurant;

    private List<String> categoriesList=new ArrayList<>();
    private List<String> forYouList=new ArrayList<>();
    private List<String> nearYouList=new ArrayList<>();
    private List<String> popularRestaurantList=new ArrayList<>();

    private FoodCategoriesAdapter foodCategoriesAdapter;
    private FoodForYouAdapter foodForYouAdapter;
    private FoodNearYouAdapter foodNearYouAdapter;
    private FoodPopularRestaurantAdapter foodPopularRestaurantAdapter;

    private TextView tv_see_all_categories,tv_see_for_you,tv_see_near_you,tv_see_popular_restaurant;
    private ImageView img_filters,img_cart,img_discover,img_back;
    private CircleImageView img_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        initComponents();
        getFoodData();

        tv_see_all_categories.setOnClickListener(this);
        tv_see_for_you.setOnClickListener(this);
        tv_see_near_you.setOnClickListener(this);
        tv_see_popular_restaurant.setOnClickListener(this);
        img_filters.setOnClickListener(this);
        img_cart.setOnClickListener(this);
        img_user.setOnClickListener(this);
    }

    private void getFoodData() {
        categoriesList.add("1");
        categoriesList.add("1");
        categoriesList.add("1");
        categoriesList.add("1");

        forYouList.add("1");
        forYouList.add("1");
        forYouList.add("1");
        forYouList.add("1");

        nearYouList.add("1");
        nearYouList.add("1");
        nearYouList.add("1");
        nearYouList.add("1");

        popularRestaurantList.add("1");
        popularRestaurantList.add("1");
        popularRestaurantList.add("1");
        popularRestaurantList.add("1");
    }
    private void initComponents() {
        img_filters=findViewById(R.id.img_filters);
        img_cart=findViewById(R.id.img_cart);
        img_discover=findViewById(R.id.img_discover);
        img_discover.setVisibility(View.VISIBLE);
        img_back=findViewById(R.id.img_back);
        img_back.setVisibility(View.GONE);
        img_user=findViewById(R.id.img_user);

        tv_see_all_categories=findViewById(R.id.tv_see_all_categories);
        tv_see_for_you=findViewById(R.id.tv_see_for_you);
        tv_see_near_you=findViewById(R.id.tv_see_near_you);
        tv_see_popular_restaurant=findViewById(R.id.tv_see_popular_restaurant);

        rv_category=findViewById(R.id.rv_category);
        rv_for_you=findViewById(R.id.rv_for_you);
        rv_near_you=findViewById(R.id.rv_near_you);
        rv_popular_restaurants=findViewById(R.id.rv_popular_restaurants);

        layoutManagerCategory=new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        rv_category.setLayoutManager(layoutManagerCategory);
        layoutManagerForYou=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        rv_for_you.setLayoutManager(layoutManagerForYou);
        layoutManagerNearYou=new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        rv_near_you.setLayoutManager(layoutManagerNearYou);
        layoutManagerPopularRestaurant=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        rv_popular_restaurants.setLayoutManager(layoutManagerPopularRestaurant);

        foodCategoriesAdapter=new FoodCategoriesAdapter(categoriesList,context);
        rv_category.setAdapter(foodCategoriesAdapter);
        foodForYouAdapter=new FoodForYouAdapter(forYouList,context);
        rv_for_you.setAdapter(foodForYouAdapter);
        foodNearYouAdapter=new FoodNearYouAdapter(nearYouList,context);
        rv_near_you.setAdapter(foodNearYouAdapter);
        foodPopularRestaurantAdapter=new FoodPopularRestaurantAdapter(popularRestaurantList,context);
        rv_popular_restaurants.setAdapter(foodPopularRestaurantAdapter);

        drawer_food=findViewById(R.id.drawer_food);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_see_all_categories:
                startActivity(new Intent(context,AllCategoriesActivity.class));
                break;
            case R.id.tv_see_for_you:
                startActivity(new Intent(context,AllForYouActivity.class));
                break;
            case R.id.tv_see_near_you:
                startActivity(new Intent(context,AllNearYouActivity.class));
                break;
            case R.id.tv_see_popular_restaurant:
                startActivity(new Intent(context,AllPopularRestaurantActivity.class));
                break;
            case R.id.img_filters:
                startActivity(new Intent(context,FoodFilterActivity.class));
                break;
            case R.id.img_cart:
                startActivity(new Intent(context, FoodCartActivity.class));
                break;
            case R.id.img_user:
                drawer_food.openDrawer(GravityCompat.START,true);
                break;
        }
    }
}