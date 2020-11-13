package com.example.cooplas.activities.Travel.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Travel.RidesHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class RidesHistoryActivity extends AppCompatActivity {

    private static final String TAG = "RidesHistoryActivity";
    private Context context=RidesHistoryActivity.this;
    private Activity activity=RidesHistoryActivity.this;

    private RecyclerView rv_rides_history;
    private LinearLayoutManager layoutManager;
    private List<String> ridesHistoryList=new ArrayList<>();
    private RidesHistoryAdapter ridesHistoryAdapter;

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides_history);

        initComponents();
        setData();
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setData() {
        ridesHistoryList.add("d");
        ridesHistoryList.add("d");
        ridesHistoryList.add("d");
        ridesHistoryList.add("d");
        ridesHistoryList.add("d");
    }

    private void initComponents() {
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(R.string.rides_history);

        rv_rides_history=findViewById(R.id.rv_rides_history);
        layoutManager=new LinearLayoutManager(context);
        ridesHistoryAdapter=new RidesHistoryAdapter(ridesHistoryList,context);
        rv_rides_history.setLayoutManager(layoutManager);
        rv_rides_history.setAdapter(ridesHistoryAdapter);


    }
}