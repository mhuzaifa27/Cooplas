package com.example.cooplas.activities.Food;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Food.FoodCategoriesAdapter;
import com.example.cooplas.adapters.Food.FoodForYouAdapter;
import com.example.cooplas.adapters.Food.FoodNearYouAdapter;
import com.example.cooplas.adapters.Food.FoodPopularRestaurantAdapter;

import java.util.ArrayList;
import java.util.List;

public class AllCategoriesActivity extends AppCompatActivity {

    private static final String TAG = "AllCategoriesActivity";
    private Context context= AllCategoriesActivity.this;
    private Activity activity= AllCategoriesActivity.this;

    private RecyclerView rv_category;
    private GridLayoutManager layoutManagerCategory;

    private List<String> categoriesList=new ArrayList<>();

    private FoodCategoriesAdapter foodCategoriesAdapter;

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_all_categories);

        initComponents();
        getFoodData();
    }

    private void getFoodData() {
        categoriesList.add("1");
        categoriesList.add("1");
        categoriesList.add("1");
        categoriesList.add("1");
        categoriesList.add("1");
        categoriesList.add("1");
        categoriesList.add("1");
        categoriesList.add("1");
        categoriesList.add("1");
        categoriesList.add("1");
        categoriesList.add("1");
        categoriesList.add("1");
        categoriesList.add("1");
        categoriesList.add("1");

    }
    private void initComponents() {
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText("Categories");

        rv_category=findViewById(R.id.rv_category);

        layoutManagerCategory=new GridLayoutManager(context,3);
        rv_category.setLayoutManager(layoutManagerCategory);

        foodCategoriesAdapter=new FoodCategoriesAdapter(categoriesList,context);
        rv_category.setAdapter(foodCategoriesAdapter);

    }
}