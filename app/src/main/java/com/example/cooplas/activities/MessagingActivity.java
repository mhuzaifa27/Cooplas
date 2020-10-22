package com.example.cooplas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.ChatCallLogAdapter;
import com.example.cooplas.adapters.MessagingAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessagingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "MessagingActivity";
    private Context context = MessagingActivity.this;
    private Activity activity = MessagingActivity.this;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_no;

    private RecyclerView rv_messaging;
    private MessagingAdapter messagingAdapter;
    private LinearLayoutManager layoutManager;
    private List<String> messagesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        initComponents();

        messagesList.add("1");
        messagesList.add("1");
        messagesList.add("0");
        messagesList.add("0");
        messagesList.add("0");
        messagesList.add("1");
        messagesList.add("1");
        messagesList.add("1");
        messagesList.add("1");
        messagesList.add("1");
        messagesList.add("0");
        messagesList.add("1");
        messagesList.add("0");

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initComponents() {

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        rv_messaging = findViewById(R.id.rv_messaging);

        layoutManager = new LinearLayoutManager(context);
        messagingAdapter = new MessagingAdapter(messagesList, context);

        rv_messaging.setLayoutManager(layoutManager);
        rv_messaging.setAdapter(messagingAdapter);

    }

    @Override
    public void onRefresh() {

    }

}