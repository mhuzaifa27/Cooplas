package com.example.cooplas.activities.Travel.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cooplas.R;
import com.example.cooplas.activities.ContactUsActivity;
import com.example.cooplas.activities.SupportTicketsActivity;
import com.example.cooplas.adapters.SupportTicketsAdapter;

public class SupportActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SupportActivity";
    private Context context=SupportActivity.this;
    private Activity activity=SupportActivity.this;

    private TextView tv_FAQ,tv_support_tickets,tv_contact_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        initComponents();

        tv_FAQ.setOnClickListener(this::onClick);
        tv_support_tickets.setOnClickListener(this::onClick);
        tv_contact_us.setOnClickListener(this::onClick);
    }

    private void initComponents() {
        tv_FAQ=findViewById(R.id.tv_FAQ);
        tv_support_tickets=findViewById(R.id.tv_support_tickets);
        tv_contact_us=findViewById(R.id.tv_contact_us);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_FAQ:
                startActivity(new Intent(context,FaqActivity.class));
                break;
            case R.id.tv_support_tickets:
                startActivity(new Intent(context, SupportTicketsActivity.class));
                break;
            case R.id.tv_contact_us:
                startActivity(new Intent(context, ContactUsActivity.class));
                break;
        }
    }
}