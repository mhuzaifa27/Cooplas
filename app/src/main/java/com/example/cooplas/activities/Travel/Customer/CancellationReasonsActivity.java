package com.example.cooplas.activities.Travel.Customer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cooplas.R;

public class CancellationReasonsActivity extends AppCompatActivity {

    private static final String TAG = "CancellationReasonsActivity";
    private Context context=CancellationReasonsActivity.this;
    private Activity activity=CancellationReasonsActivity.this;

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation_reasons);

        initComponents();
    }

    private void initComponents() {
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(R.string.cancellation_reasons);
    }
}