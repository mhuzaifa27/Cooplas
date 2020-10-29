package com.example.cooplas.activities.Food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Food.RestaurantMenuAdapter;
import com.example.cooplas.adapters.Food.RestaurantReviewsAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RestaurantDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RestaurantDetailActivit";
    private Context context=RestaurantDetailActivity.this;
    private Activity activity=RestaurantDetailActivity.this;

    private RecyclerView rv_menu,rv_reviews;
    private List<String> listMenu=new ArrayList<>();
    private List<String> listReviews=new ArrayList<>();
    private LinearLayoutManager menuLayoutManager,reviewsLayoutManager;

    private RestaurantMenuAdapter restaurantMenuAdapter;
    private RestaurantReviewsAdapter restaurantReviewsAdapter;

    private TextView tv_about_us,tv_about_us_text,tv_reviews;
    private CircleImageView img_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        initComponents();

        listMenu.add("2");
        listMenu.add("2");
        listMenu.add("2");
        listMenu.add("2");

        listReviews.add("2");
        listReviews.add("2");
        listReviews.add("2");
        listReviews.add("2");
        listReviews.add("2");

        tv_reviews.setOnClickListener(this);
        tv_about_us.setOnClickListener(this);

    }

    private void initComponents() {
        rv_menu=findViewById(R.id.rv_menu);
        rv_reviews=findViewById(R.id.rv_reviews);

        menuLayoutManager=new LinearLayoutManager(context);
        reviewsLayoutManager=new LinearLayoutManager(context);

        restaurantMenuAdapter=new RestaurantMenuAdapter(listMenu,context);
        restaurantReviewsAdapter=new RestaurantReviewsAdapter(listReviews,context);

        rv_menu.setLayoutManager(menuLayoutManager);
        rv_reviews.setLayoutManager(reviewsLayoutManager);

        rv_menu.setAdapter(restaurantMenuAdapter);
        rv_reviews.setAdapter(restaurantReviewsAdapter);

        tv_about_us=findViewById(R.id.tv_about_us);
        tv_about_us_text=findViewById(R.id.tv_about_us_text);
        tv_reviews=findViewById(R.id.tv_reviews);

        img_user=findViewById(R.id.img_user);
        img_user.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_about_us:
                setAboutUsView();
                break;
            case R.id.tv_reviews:
                setReviewView();
                break;
        }
    }

    private void setAboutUsView() {
        rv_reviews.setVisibility(View.GONE);
        tv_about_us_text.setVisibility(View.VISIBLE);

        tv_about_us.setTextColor(getResources().getColor(R.color.colorSecond));
        tv_reviews.setTextColor(getResources().getColor(R.color.grey));
    }
    private void setReviewView() {
        rv_reviews.setVisibility(View.VISIBLE);
        tv_about_us_text.setVisibility(View.GONE);

        tv_about_us.setTextColor(getResources().getColor(R.color.grey));
        tv_reviews.setTextColor(getResources().getColor(R.color.colorSecond));
    }
}