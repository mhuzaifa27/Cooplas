package com.example.cooplas.activities.Travel.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cooplas.R;

public class AddAddressActivity extends AppCompatActivity {
    private static final String TAG = "AddAddressActivity";
    private Context context=AddAddressActivity.this;
    private Activity activity=AddAddressActivity.this;

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        initComponents();
    }
    private void initComponents() {
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText("Add Address");
    }
}