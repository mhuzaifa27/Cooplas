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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooplas.Firebase.ChangeEventListener;
import com.example.cooplas.Firebase.Models.User;
import com.example.cooplas.Firebase.Services.GroupService;
import com.example.cooplas.R;
import com.example.cooplas.adapters.Chat.ParticipantsAdapter;
import com.example.cooplas.utils.CheckConnectivity;
import com.example.cooplas.utils.CheckInternetEvent;
import com.example.cooplas.utils.Common;
import com.example.cooplas.utils.Constants;
import com.example.cooplas.utils.ShowDialogues;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CreateGroupActivity";
    private Context context = CreateGroupActivity.this;
    private Activity activity = CreateGroupActivity.this;

    private EventBus eventBus = EventBus.getDefault();
    private BroadcastReceiver mNetworkReceiver;
    private View parentLayout;
    private TextView tv_title;
    private ImageView img_back;

    private RecyclerView rv_participants;
    private LinearLayoutManager layoutManagerParticipants;

    private List<User> participantsList = new ArrayList<>();
    private EditText et_group_name;
    private GroupService groupService;


    private ParticipantsAdapter participantsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        getIntentData();
        initComponents();

        img_back.setOnClickListener(this);
    }

    private void getIntentData() {
        String carListAsString = getIntent().getStringExtra(Constants.CREATE_RIDE_OBJ);
        Gson gson = new Gson();
        Type type = new TypeToken<List<User>>() {
        }.getType();
        participantsList = gson.fromJson(carListAsString, type);
    }

    private void initComponents() {
        eventBus.register(this);
        mNetworkReceiver = new CheckConnectivity();
        registerNetworkBroadcastForNougat();
        parentLayout = findViewById(android.R.id.content);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.group_chat);

        et_group_name = findViewById(R.id.et_group_name);

        img_back = findViewById(R.id.img_back);

        layoutManagerParticipants = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        participantsAdapter = new ParticipantsAdapter(participantsList, context);

        rv_participants = findViewById(R.id.rv_participants);
        rv_participants.setLayoutManager(layoutManagerParticipants);
        rv_participants.setAdapter(participantsAdapter);
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
            case R.id.rl_create_group:
                if (!et_group_name.getText().toString().isEmpty()
                        && participantsList.size() > 0) {
                    List<String> members=new ArrayList<>();
                    for (int i = 0; i <participantsList.size() ; i++) {
                        members.add(participantsList.get(i).getUserId());
                    }
                    createGroup(et_group_name.getText().toString(),members);
                }
                break;
        }
    }

    private void createGroup(String name,List<String> members) {
        groupService=new GroupService();
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
        groupService.createGroup(name, members, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(context, "Group Created Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context,GroupMessagingActivity.class);
                intent.putExtra(Constants.ID, Common.ID);
                intent.putExtra(Constants.EXTRAS, "NEW");
                intent.putExtra(Constants.START_NAME, name);
                startActivity(intent);
            }
        });

        //Toast.makeText(context, "Group created successfully!", Toast.LENGTH_SHORT).show();
    }
}