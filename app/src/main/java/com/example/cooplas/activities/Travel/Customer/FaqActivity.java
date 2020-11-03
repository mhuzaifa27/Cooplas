package com.example.cooplas.activities.Travel.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cooplas.R;

public class FaqActivity extends AppCompatActivity {

    private static final String TAG = "FaqActivity";
    private Context context=FaqActivity.this;
    private Activity activity=FaqActivity.this;

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        initComponents();
    }
    private void initComponents() {
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(R.string.faq);
    }
}