package com.example.cooplas.activities.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cooplas.Firebase.AppState;
import com.example.cooplas.Firebase.ChangeEventListener;
import com.example.cooplas.Firebase.Models.User;
import com.example.cooplas.Firebase.Services.UserFollowersService;
import com.example.cooplas.Firebase.Services.UserService;
import com.example.cooplas.R;
import com.example.cooplas.activities.Food.FoodActivity;
import com.example.cooplas.adapters.Chat.SelectUsersForGroupChatAdapter;
import com.example.cooplas.adapters.Food.FoodForYouAdapter;
import com.example.cooplas.models.Food.ForYou;
import com.example.cooplas.utils.CheckConnectivity;
import com.example.cooplas.utils.CheckInternetEvent;
import com.example.cooplas.utils.Constants;
import com.example.cooplas.utils.ShowDialogues;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SelectUsersForGroupChatActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SelectUsers";
    private Context context = SelectUsersForGroupChatActivity.this;
    private Activity activity = SelectUsersForGroupChatActivity.this;

    private EventBus eventBus = EventBus.getDefault();
    private BroadcastReceiver mNetworkReceiver;
    private View parentLayout;
    private TextView tv_title;
    private ImageView img_back;

    private RecyclerView rv_users;
    private LinearLayoutManager layoutManagerUsers;

    private List<User> usersList = new ArrayList<>();

    private SelectUsersForGroupChatAdapter selectUsersForGroupChatAdapter;
    private UserFollowersService userFollowersService;
    private UserService userService;
    private KProgressHUD progressHUD;
    private ArrayList<User> selectedUser=new ArrayList<>();
    private RelativeLayout rl_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_users_for_group_chat);

        initComponents();
        getUsers();

        img_back.setOnClickListener(this);
        rl_next.setOnClickListener(this);
    }

    private void getUsers() {
        progressHUD.show();
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
        userFollowersService = new UserFollowersService();
        userFollowersService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {

            }

            @Override
            public void onDataChanged() {
                usersList.clear();
                if (userFollowersService.getCount() > 0) {
                    DataSnapshot snapshot = userFollowersService.snapshotForKey(AppState.currentFireUser.getUid());
                    if (snapshot.getChildrenCount() > 0) {
                        for (DataSnapshot sp : snapshot.getChildren()) {
                            usersList.add(userService.getUserById(sp.getValue().toString()));
                    }
                        selectUsersForGroupChatAdapter.addAll(usersList);
                        progressHUD.dismiss();
                }
            }
        }
        @Override
        public void onCancelled (DatabaseError error){

        }
    });
}

    private void initComponents() {
        progressHUD = KProgressHUD.create(activity);
        eventBus.register(this);
        mNetworkReceiver = new CheckConnectivity();
        registerNetworkBroadcastForNougat();
        parentLayout = findViewById(android.R.id.content);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.group_chat);

        img_back = findViewById(R.id.img_back);

        rl_next=findViewById(R.id.rl_next);

        layoutManagerUsers = new LinearLayoutManager(context);
        selectUsersForGroupChatAdapter = new SelectUsersForGroupChatAdapter(usersList, context);

        rv_users = findViewById(R.id.rv_users);
        rv_users.setLayoutManager(layoutManagerUsers);
        rv_users.setAdapter(selectUsersForGroupChatAdapter);

        selectUsersForGroupChatAdapter.setOnCheckedChangeListener(new SelectUsersForGroupChatAdapter.IClicks() {
            @Override
            public void onCheckChangeListener(boolean value, User user) {
                if(value){
                    if(!selectedUser.contains(user))
                        selectedUser.add(user);
                }
                else {
                    if(selectedUser.contains(user))
                        selectedUser.remove(user);
                }
            }
        });
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
        unregisterNetworkChanges();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CheckInternetEvent event) {
        Log.d("SsS", "checkInternetAvailability: called");
        if (event.isIS_INTERNET_AVAILABLE()) {
            ShowDialogues.SHOW_SNACK_BAR(parentLayout, activity, getString(R.string.snackbar_internet_available));

        } else {
            ShowDialogues.SHOW_SNACK_BAR(parentLayout, activity, getString(R.string.snackbar_check_internet));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.rl_next:
                if(selectedUser.size()>0){
                    Gson gson = new Gson();
                    String jsonUsers = gson.toJson(selectedUser);
                    Intent intent=new Intent(context,CreateGroupActivity.class);
                    intent.putExtra(Constants.CREATE_RIDE_OBJ, jsonUsers);
                    startActivity(intent);
                }
                break;
        }
    }
}