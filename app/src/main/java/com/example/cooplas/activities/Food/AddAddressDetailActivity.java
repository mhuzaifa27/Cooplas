package com.example.cooplas.activities.Food;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cooplas.R;

public class AddAddressDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FaqDetailActivity";
    private Context context=AddAddressDetailActivity.this;
    private Activity activity=AddAddressDetailActivity.this;

    private TextView tv_title;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address_detail);

        initComponents();

        img_back.setOnClickListener(this);
    }
    private void initComponents() {
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(R.string.describe);

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