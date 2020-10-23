package com.example.cooplas.activities.Food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class AllNearYouActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AllNearYouActivity";
    private Context context= AllNearYouActivity.this;
    private Activity activity= AllNearYouActivity.this;

    private RecyclerView rv_near_you;
    private GridLayoutManager layoutManagerNearYou;

    private List<String> nearYouList=new ArrayList<>();

    private FoodNearYouAdapter foodNearYouAdapter;

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_near_you);

        initComponents();
        getFoodData();

    }

    private void getFoodData() {
        nearYouList.add("1");
        nearYouList.add("1");
        nearYouList.add("1");
        nearYouList.add("1");
        nearYouList.add("1");
        nearYouList.add("1");
        nearYouList.add("1");
        nearYouList.add("1");
        nearYouList.add("1");
        nearYouList.add("1");
        nearYouList.add("1");
        nearYouList.add("1");
        nearYouList.add("1");
    }
    private void initComponents() {
        tv_title =findViewById(R.id.tv_title);
        tv_title.setText("Near You");

        rv_near_you=findViewById(R.id.rv_near_you);

        layoutManagerNearYou=new GridLayoutManager(context,2);
        rv_near_you.setLayoutManager(layoutManagerNearYou);

        foodNearYouAdapter=new FoodNearYouAdapter(nearYouList,context);
        rv_near_you.setAdapter(foodNearYouAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_see_all_categories:
                startActivity(new Intent(context,AllCategoriesActivity.class));
                break;
        }
    }
}