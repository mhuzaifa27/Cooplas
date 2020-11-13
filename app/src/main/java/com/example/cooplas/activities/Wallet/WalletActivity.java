package com.example.cooplas.activities.Wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cooplas.R;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "WalletActivity";
    private Context context=WalletActivity.this;
    private Activity activity=WalletActivity.this;

    private Button btn_recharge;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        initComponents();

        btn_recharge.setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initComponents() {
        btn_recharge =findViewById(R.id.btn_recharge);

        tv_title=findViewById(R.id.tv_title);
        tv_title.setText("Wallet");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_recharge:
                startActivity(new Intent(context,RechargeActivity.class));
                break;
        }
    }
}