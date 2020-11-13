package com.example.cooplas.activities.Travel.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cooplas.R;

public class PromoCodeActivity extends AppCompatActivity {

    private static final String TAG = "PromoCodeActivity";
    private Context context=PromoCodeActivity.this;
    private Activity activity=PromoCodeActivity.this;

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code);

        initComponents();
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initComponents() {
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(R.string.promo_codes);
    }
}