package com.example.cooplas.fragments.Others;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cooplas.Firebase.AppState;
import com.example.cooplas.Firebase.ChangeEventListener;
import com.example.cooplas.Firebase.Models.Group;
import com.example.cooplas.Firebase.Models.Message;
import com.example.cooplas.Firebase.Models.User;
import com.example.cooplas.Firebase.Services.GroupService;
import com.example.cooplas.Firebase.Services.InboxService;
import com.example.cooplas.Firebase.Services.UserService;
import com.example.cooplas.R;
import com.example.cooplas.activities.Chat.SelectUsersForGroupChatActivity;
import com.example.cooplas.adapters.Chat.ChatInboxAdapter;
import com.example.cooplas.models.Chat.InboxItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;


public class InboxFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String TAG = "InboxFragment";
    private Context context;
    private Activity activity;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_no;
    private RecyclerView rv_inbox;
    private ChatInboxAdapter inboxAdapter;
    private LinearLayoutManager layoutManager;
    private List<InboxItem> inboxList = new ArrayList<>();
    private FloatingActionButton fab_create_group;
    private InboxService inboxService;
    private UserService userService;
    private KProgressHUD progressHUD;
    private GroupService groupService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        initComponents(view);

        userService = new UserService();
        userService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {

            }

            @Override
            public void onDataChanged() {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        groupService = new GroupService();
        groupService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {

            }

            @Override
            public void onDataChanged() {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        inboxService = new InboxService();
        inboxService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {

            }

            @Override
            public void onDataChanged() {
                //progressHUD.show();
                inboxList.clear();
                if (inboxService.getCount() > 0) {
                    for (int i = 0; i < inboxService.getCount(); i++) {
                        DataSnapshot sp = inboxService.getItem(i);
                        if (sp.getChildrenCount() > 0) {
                            for (int j = 0; j < sp.getChildrenCount(); j++) {
                                String map = sp.getKey();
                                if (map.contains("___")) {
                                    String userId = map.split("___")[0];
                                    if (userId.equalsIgnoreCase(AppState.currentBpackCustomer.getUserId())) {
                                        User user = userService.getUserById(map.split("___")[1]);
                                        Message message = sp.getValue(Message.class);
                                        inboxList.add(new InboxItem("2", user.getUserName(), user.getImage(), message.getTime(), message.getMessage(), user.getUserId()));
                                        break;
                                    }
                                }
                                /*******WORKING CODE FOR GROUP CHAT********/
                                else {
                                    Group group=groupService.getGroupById(map);
                                    List<String> members=group.getMembersList();
                                    if(members.contains(AppState.currentFireUser.getUid()) || group.getAdmin().equalsIgnoreCase(AppState.currentFireUser.getUid())){
                                        //User user = userService.getUserById(map);
                                        Message message = sp.getValue(Message.class);
                                        inboxList.add(new InboxItem("2", group.getName(), "Group", message.getTime(), message.getMessage(), group.getGroupId()));
                                        break;
                                    }
                                }
                                /*******WORKING CODE FOR GROUP CHAT********/
                            }
                            inboxAdapter.addAll(inboxList);
                            //progressHUD.dismiss();
                        }
                    }
                }
                //progressHUD.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        fab_create_group.setOnClickListener(this);

        return view;
    }

    private void initComponents(View view) {
        context = getContext();
        activity = getActivity();
        progressHUD = KProgressHUD.create(activity);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        rv_inbox = view.findViewById(R.id.rv_inbox);

        layoutManager = new LinearLayoutManager(context);
        inboxAdapter = new ChatInboxAdapter(inboxList, context);

        rv_inbox.setLayoutManager(layoutManager);
        rv_inbox.setAdapter(inboxAdapter);

        fab_create_group = view.findViewById(R.id.fab_create_group);

        //tv_no=view.findViewById(R.id.tv_no);
    }

    @Override
    public void onRefresh() {
        userService = new UserService();
        userService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {

            }

            @Override
            public void onDataChanged() {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        inboxService = new InboxService();
        inboxService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {

            }

            @Override
            public void onDataChanged() {
                inboxList.clear();
                if (inboxService.getCount() > 0) {
                    for (int i = 0; i < inboxService.getCount(); i++) {
                        DataSnapshot sp = inboxService.getItem(i);
                        if (sp.getChildrenCount() > 0) {
                            for (int j = 0; j < sp.getChildrenCount(); j++) {
                                String map = sp.getKey();
                                if (map.contains("___")) {
                                    String userId = map.split("___")[0];
                                    if (userId.equalsIgnoreCase(AppState.currentBpackCustomer.getUserId())) {
                                        User user = userService.getUserById(map.split("___")[1]);
                                        Message message = sp.getValue(Message.class);
                                        inboxList.add(new InboxItem("2", user.getUserName(), user.getImage(), message.getTime(), message.getMessage(), user.getUserId()));
                                        break;
                                    }
                                }
                                /*******WORKING CODE FOR GROUP CHAT********/
                                else {
                                    Group group=groupService.getGroupById(map);
                                    List<String> members=group.getMembersList();
                                    if(members.contains(AppState.currentFireUser.getUid()) || group.getAdmin().equalsIgnoreCase(AppState.currentFireUser.getUid())){
                                        //User user = userService.getUserById(map);
                                        Message message = sp.getValue(Message.class);
                                        inboxList.add(new InboxItem("2", group.getName(), "Group", message.getTime(), message.getMessage(), group.getGroupId()));
                                        break;
                                    }
                                }
                                /*******WORKING CODE FOR GROUP CHAT********/
                            }
                            inboxAdapter.addAll(inboxList);
                            if (swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_create_group:
                startActivity(new Intent(context, SelectUsersForGroupChatActivity.class));
                break;
        }
    }
}
