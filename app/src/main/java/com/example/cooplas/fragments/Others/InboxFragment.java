package com.example.cooplas.fragments.Others;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cooplas.R;
import com.example.cooplas.adapters.ChatInboxAdapter;

import java.util.ArrayList;
import java.util.List;


public class InboxFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "InboxFragment";
    private Context context;
    private Activity activity;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_no;

    private RecyclerView rv_inbox;
    private ChatInboxAdapter inboxAdapter;
    private LinearLayoutManager layoutManager;
    private List<String> inboxList=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        initComponents(view);

        inboxList.add("d");
        inboxList.add("d");
        inboxList.add("d");
        inboxList.add("d");
        inboxList.add("d");
        inboxList.add("d");
        inboxList.add("d");
        inboxList.add("d");
        inboxList.add("d");
        inboxList.add("d");
        inboxList.add("d");
        inboxList.add("d");
        inboxList.add("d");

        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    private void initComponents(View view) {
        context=getContext();
        activity=getActivity();

        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout);

        rv_inbox=view.findViewById(R.id.rv_inbox);

        layoutManager=new LinearLayoutManager(context);
        inboxAdapter=new ChatInboxAdapter(inboxList,context);

        rv_inbox.setLayoutManager(layoutManager);
        rv_inbox.setAdapter(inboxAdapter);

        //tv_no=view.findViewById(R.id.tv_no);
    }

    @Override
    public void onRefresh() {

    }
}
