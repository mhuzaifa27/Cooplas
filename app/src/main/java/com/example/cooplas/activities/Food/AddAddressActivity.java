package com.example.cooplas.activities.Food;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cooplas.R;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddAddressActivity";
    private Context context=AddAddressActivity.this;
    private Activity activity=AddAddressActivity.this;

    private TextView tv_title;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        initComponents();

        img_back.setOnClickListener(this);
    }
    private void initComponents() {
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText("Add Address");

        img_back=findViewById(R.id.img_back);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}