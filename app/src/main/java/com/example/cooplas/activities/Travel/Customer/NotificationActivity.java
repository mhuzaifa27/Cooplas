package com.example.cooplas.activities.Travel.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Travel.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "NotificationActivity";
    private Context context =NotificationActivity.this;
    private Activity activity=NotificationActivity.this;

    private RecyclerView rv_notification;
    private LinearLayoutManager layoutManager;
    private List<String> notificationList=new ArrayList<>();
    private NotificationAdapter notificationAdapter;

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initComponents();
        setData();
    }

    private void setData() {
        notificationList.add("s");
        notificationList.add("s");
        notificationList.add("s");
        notificationList.add("s");
        notificationList.add("s");
        notificationList.add("s");
        notificationList.add("s");
        notificationList.add("s");
        notificationList.add("s");
    }

    private void initComponents() {
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(R.string.notifications);

        rv_notification=findViewById(R.id.rv_notification);
        layoutManager=new LinearLayoutManager(context);
        notificationAdapter=new NotificationAdapter(notificationList,context);

        rv_notification.setLayoutManager(layoutManager);
        rv_notification.setAdapter(notificationAdapter);

    }
}