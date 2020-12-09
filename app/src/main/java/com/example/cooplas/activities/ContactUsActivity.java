package com.example.cooplas.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cooplas.Firebase.ChangeEventListener;
import com.example.cooplas.Firebase.Services.UserService;
import com.example.cooplas.R;
import com.google.firebase.database.DatabaseError;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SupportTicketsActivity";
    private Context context= ContactUsActivity.this;
    private Activity activity= ContactUsActivity.this;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        initComponents();

    }

    private void initComponents() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }
}