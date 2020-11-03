package com.example.cooplas.activities.Travel.Customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cooplas.R;

public class RideCompleteActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RideCompleteActivity";
    private Context context=RideCompleteActivity.this;
    private Activity activity=RideCompleteActivity.this;

    private RelativeLayout rl_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_complete);

        initComponents();

        rl_submit.setOnClickListener(this::onClick);
    }

    private void initComponents() {
        rl_submit=findViewById(R.id.rl_submit);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_submit:
                startActivity(new Intent(context,RidesHistoryActivity.class));
                break;
        }
    }
}