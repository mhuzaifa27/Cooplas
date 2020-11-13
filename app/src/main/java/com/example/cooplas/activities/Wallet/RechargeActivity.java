package com.example.cooplas.activities.Wallet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.cooplas.R;

public class RechargeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RechargeActivity";
    private Context context=RechargeActivity.this;
    private Activity activity=RechargeActivity.this;

    private Button btn_confirm;
    private CardView card_recharge;
    private LinearLayout ll_successful;
    private TextView tv_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        initComponents();

        btn_confirm.setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initComponents() {
        btn_confirm=findViewById(R.id.btn_confirm);

        card_recharge=findViewById(R.id.card_recharge);

        ll_successful=findViewById(R.id.ll_successful);

        tv_title=findViewById(R.id.tv_title);
        tv_title.setText("Recharge");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_confirm:
                setTransactionSuccessView();
                break;
        }
    }

    private void setTransactionSuccessView() {
        btn_confirm.setText("Back to Wallet");
        card_recharge.setVisibility(View.GONE);
        ll_successful.setVisibility(View.VISIBLE);
    }
}