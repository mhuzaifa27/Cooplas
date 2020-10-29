package com.example.cooplas.activities.Food;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.cooplas.R;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CheckoutActivity";
    private Context context=CheckoutActivity.this;
    private Activity activity=CheckoutActivity.this;
    private Resources resources;

    private TextView tv_title;
    private Button btn_checkout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initComponents();

        btn_checkout.setOnClickListener(this);
    }

    private void initComponents() {
        resources=getResources();

        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(resources.getString(R.string.cart));

        btn_checkout=findViewById(R.id.btn_checkout);
    }

    private void ShowOrderCompletedDialog(){
        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialogue_food_order_placed);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_checkout:
                ShowOrderCompletedDialog();
                break;
        }
    }
}