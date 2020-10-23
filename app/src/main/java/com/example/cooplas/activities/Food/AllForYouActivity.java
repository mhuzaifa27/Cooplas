package com.example.cooplas.activities.Food;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Food.FoodForYouAdapter;

import java.util.ArrayList;
import java.util.List;

public class AllForYouActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AllForYouActivity";
    private Context context= AllForYouActivity.this;
    private Activity activity= AllForYouActivity.this;

    private RecyclerView rv_for_you;
    private LinearLayoutManager layoutManagerForYou;

    private List<String> forYouList=new ArrayList<>();

    private FoodForYouAdapter foodForYouAdapter;

    private TextView tv_title;
    private ImageView img_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_for_you);

        initComponents();
        getFoodData();

    }

    private void getFoodData() {

        forYouList.add("1");
        forYouList.add("1");
        forYouList.add("1");
        forYouList.add("1");
        forYouList.add("1");
        forYouList.add("1");
        forYouList.add("1");
        forYouList.add("1");
        forYouList.add("1");
        forYouList.add("1");

    }
    private void initComponents() {
        img_cart=findViewById(R.id.img_cart);
        img_cart.setVisibility(View.VISIBLE);

        tv_title =findViewById(R.id.tv_title);
        tv_title.setText("For you");

        rv_for_you=findViewById(R.id.rv_for_you);

        layoutManagerForYou=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        rv_for_you.setLayoutManager(layoutManagerForYou);

        foodForYouAdapter=new FoodForYouAdapter(forYouList,context);
        rv_for_you.setAdapter(foodForYouAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}