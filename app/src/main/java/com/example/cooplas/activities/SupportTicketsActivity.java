package com.example.cooplas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.SupportTicketsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SupportTicketsActivity extends AppCompatActivity {

    private static final String TAG = "SupportTicketsActivity";
    private Context context=SupportTicketsActivity.this;
    private Activity activity=SupportTicketsActivity.this;

    private RecyclerView  rv_tickets;
    private List<String> ticketsList=new ArrayList<>();
    private SupportTicketsAdapter supportTicketsAdapter;
    private LinearLayoutManager layoutManager;

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_tickets);

        initComponents();
        getData();
    }

    private void getData() {
        ticketsList.add("0");
        ticketsList.add("1");
        ticketsList.add("2");
        ticketsList.add("3");
        ticketsList.add("0");
    }

    private void initComponents() {
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(R.string.support_tickets);

        rv_tickets=findViewById(R.id.rv_tickets);

        layoutManager=new LinearLayoutManager(context);
        supportTicketsAdapter=new SupportTicketsAdapter(ticketsList,context);

        rv_tickets.setLayoutManager(layoutManager);
        rv_tickets.setAdapter(supportTicketsAdapter);
    }
}