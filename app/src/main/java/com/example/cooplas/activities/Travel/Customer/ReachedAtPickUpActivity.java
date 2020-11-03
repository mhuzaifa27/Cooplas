package com.example.cooplas.activities.Travel.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cooplas.R;

public class ReachedAtPickUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ReachedAtPickUpActivity";
    private Context context=ReachedAtPickUpActivity.this;
    private Activity activity=ReachedAtPickUpActivity.this;

    private TextView tv_title;
    private RelativeLayout rl_reached;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reached_at_pick_up);

        initComponents();

        rl_reached.setOnClickListener(this::onClick);
    }

    private void initComponents() {
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(R.string.vehicle_is_waiting_for_you);

        rl_reached=findViewById(R.id.rl_reached);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_reached:
                startActivity(new Intent(context,RideCompleteActivity.class));
                break;
        }
    }
}