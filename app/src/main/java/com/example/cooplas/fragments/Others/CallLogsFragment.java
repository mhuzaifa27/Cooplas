package com.example.cooplas.fragments.Others;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cooplas.R;
import com.example.cooplas.adapters.ChatCallLogAdapter;
import com.example.cooplas.utils.ViewAnimation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class CallLogsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String TAG = "ChatFragment";
    private Context context;
    private Activity activity;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_no;

    private RecyclerView rv_call_logs;
    private ChatCallLogAdapter callLogAdapter;
    private LinearLayoutManager layoutManager;
    private List<String> callLogList =new ArrayList<>();

    private FloatingActionButton fab_add,fab_message,fab_camera;
    private boolean isRotate=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_logs, container, false);

        initComponents(view);

        callLogList.add("d");
        callLogList.add("d");
        callLogList.add("d");
        callLogList.add("d");
        callLogList.add("d");
        callLogList.add("d");
        callLogList.add("d");
        callLogList.add("d");
        callLogList.add("d");
        callLogList.add("d");
        callLogList.add("d");
        callLogList.add("d");
        callLogList.add("d");

        swipeRefreshLayout.setOnRefreshListener(this);
        fab_add.setOnClickListener(this);

        ViewAnimation.init(fab_camera);
        ViewAnimation.init(fab_message);

        return view;
    }

    private void initComponents(View view) {
        context=getContext();
        activity=getActivity();

        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout);

        rv_call_logs =view.findViewById(R.id.rv_call_logs);

        layoutManager=new LinearLayoutManager(context);
        callLogAdapter =new ChatCallLogAdapter(callLogList,context);

        rv_call_logs.setLayoutManager(layoutManager);
        rv_call_logs.setAdapter(callLogAdapter);

        fab_add=view.findViewById(R.id.fab_add);
        fab_message=view.findViewById(R.id.fab_message);
        fab_camera=view.findViewById(R.id.fab_camera);

        //tv_no=view.findViewById(R.id.tv_no);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_add:
                isRotate= ViewAnimation.rotateFab(view,!isRotate);
                if(isRotate){
                   /* if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                        fab_add.setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorSecond)));
                    } else {
                        ViewCompat.setBackgroundTintList(fab_add, ColorStateList.valueOf(getResources().getColor(R.color.colorSecond)));
                    }
                    fab_add.setBackgroundColor(getResources().getColor(R.color.colorSecond));*/
                    ViewAnimation.showIn(fab_camera);
                    ViewAnimation.showIn(fab_message);
                }else{
                    /*if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                        fab_add.setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    } else {
                        ViewCompat.setBackgroundTintList(fab_add, ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    }
                    fab_add.setBackgroundColor(getResources().getColor(R.color.colorSecond));*/
                    ViewAnimation.showOut(fab_message);
                    ViewAnimation.showOut(fab_camera);
                }
                break;
        }
    }
}
