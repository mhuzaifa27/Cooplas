package com.example.cooplas.activities.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cooplas.Firebase.AppState;
import com.example.cooplas.Firebase.ChangeEventListener;
import com.example.cooplas.Firebase.Models.Group;
import com.example.cooplas.Firebase.Models.MediaModel;
import com.example.cooplas.Firebase.Models.Message;
import com.example.cooplas.Firebase.Models.User;
import com.example.cooplas.Firebase.Services.GroupMessagingService;
import com.example.cooplas.Firebase.Services.GroupService;
import com.example.cooplas.Firebase.Services.InboxService;
import com.example.cooplas.R;
import com.example.cooplas.adapters.Chat.GroupMediaAdapter;
import com.example.cooplas.adapters.Chat.GroupModeratorsAdapter;
import com.example.cooplas.adapters.Chat.GroupParticipantsAdapter;
import com.example.cooplas.models.Chat.InboxItem;
import com.example.cooplas.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class GroupInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GroupInfoActivity";
    private Context context = GroupInfoActivity.this;
    private Activity activity = GroupInfoActivity.this;

    private GroupService groupService;
    private String groupId, name;
    private TextView tv_group_name;

    private RecyclerView rv_media, rv_participants, rv_moderators;
    private LinearLayoutManager layoutManagerMedia, layoutManagerParticipants, layoutManagerModerators;

    private List<MediaModel> mediaList = new ArrayList<>();
    private List<String> moderatorsList = new ArrayList<>();
    private List<String> participantsList = new ArrayList<>();
    private TextView tv_title;

    private GroupParticipantsAdapter participantsAdapter;
    private GroupMediaAdapter mediaAdapter;
    private GroupModeratorsAdapter moderatorsAdapter;
    private InboxService inboxService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        getIntentData();
        initComponents();

        groupService = new GroupService();
        groupService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {
            }

            @Override
            public void onDataChanged() {
                mediaList.clear();
                moderatorsList.clear();
                participantsList.clear();
                Group group = groupService.getGroupById(groupId);

                if (group.getMembersList() != null) {
                    if (group.getMembersList().size() > 0) {
                        participantsList = group.getMembersList();
                        participantsAdapter.addAll(participantsList);
                    }
                }
                if (group.getModerators() != null) {
                    if (group.getModerators().size() > 0) {
                        moderatorsList = group.getMembersList();
                        moderatorsAdapter.addAll(moderatorsList);
                    }
                }
                inboxService = new InboxService();
                inboxService.setOnChangedListener(new ChangeEventListener() {
                    @Override
                    public void onChildChanged(EventType type, int index, int oldIndex) {

                    }

                    @Override
                    public void onDataChanged() {
                        if (inboxService.getCount() > 0) {
                            for (int i = 0; i < inboxService.getCount(); i++) {
                                DataSnapshot sp = inboxService.getItem(i);
                                if (sp.getChildrenCount() > 0) {
                                    String map = sp.getKey();
                                    if (map.equalsIgnoreCase(groupId)) {
                                        for (DataSnapshot snapshot : sp.getChildren()) {
                                            Message message = snapshot.getValue(Message.class);
                                            if (message.getType().equalsIgnoreCase(Constants.TYPE_IMAGE) ||
                                                    message.getType().equalsIgnoreCase(Constants.TYPE_VIDEO)) {
                                                mediaList.add(new MediaModel(message.getMessage(), message.getType()));
                                            }
                                        }
                                    }
                                    mediaAdapter.addAll(mediaList);
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
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void initComponents() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(name);
        tv_group_name = findViewById(R.id.tv_group_name);
        tv_group_name.setText(name);

        rv_media = findViewById(R.id.rv_media);
        rv_moderators = findViewById(R.id.rv_moderators);
        rv_participants = findViewById(R.id.rv_participants);

        layoutManagerMedia = new GridLayoutManager(context, 4);
        layoutManagerModerators = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        layoutManagerParticipants = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);

        rv_participants.setLayoutManager(layoutManagerParticipants);
        rv_media.setLayoutManager(layoutManagerMedia);
        rv_moderators.setLayoutManager(layoutManagerModerators);

        participantsAdapter = new GroupParticipantsAdapter(participantsList, context);
        mediaAdapter = new GroupMediaAdapter(mediaList, context);
        moderatorsAdapter = new GroupModeratorsAdapter(moderatorsList, context);

        rv_participants.setAdapter(participantsAdapter);
        rv_media.setAdapter(mediaAdapter);
        rv_moderators.setAdapter(moderatorsAdapter);

    }

    private void getIntentData() {
        groupId = getIntent().getStringExtra(Constants.ID);
        name = getIntent().getStringExtra(Constants.START_NAME);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}