package com.example.cooplas.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cooplas.R;
import com.example.cooplas.activities.Travel.Customer.FaqActivity;

public class ReportBugActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ReportBugActivity";
    private Context context= ReportBugActivity.this;
    private Activity activity= ReportBugActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_bug);

        initComponents();

    }

    private void initComponents() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}