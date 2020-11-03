package com.example.cooplas.activities.Travel.Customer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cooplas.R;

public class InviteFriendsActivity extends AppCompatActivity {

    private static final String TAG = "InviteFriendsActivity";
    private Context context=InviteFriendsActivity.this;
    private Activity activity=InviteFriendsActivity.this;

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        initComponents();
    }

    private void initComponents() {
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(R.string.invite_friends);
    }
}