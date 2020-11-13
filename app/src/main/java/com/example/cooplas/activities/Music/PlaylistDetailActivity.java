package com.example.cooplas.activities.Music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Music.MusicPlaylistSongsAdapter;
import com.example.cooplas.utils.CheckConnectivity;
import com.example.cooplas.utils.CheckInternetEvent;
import com.example.cooplas.utils.ShowDialogues;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PlaylistDetailActivity";
    private Context context = PlaylistDetailActivity.this;
    private Activity activity = PlaylistDetailActivity.this;

    private EventBus eventBus = EventBus.getDefault();
    private BroadcastReceiver mNetworkReceiver;
    private View parentLayout;

    private RecyclerView rv_songs;
    private LinearLayoutManager layoutManagerSongs;
    private MusicPlaylistSongsAdapter playlistSongsAdapter;

    private List<String> songsList = new ArrayList<>();

    private TextView tv_title;
    private ImageView img_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);

        initComponents();
        getDetails();

        img_back.setOnClickListener(this::onClick);
    }

    private void getDetails() {
        songsList.add("");
        songsList.add("");
        songsList.add("");
        songsList.add("");
        songsList.add("");
        songsList.add("");
        songsList.add("");
        songsList.add("");
        songsList.add("");
        songsList.add("");
        songsList.add("");

    }

    private void initComponents() {
        eventBus.register(this);
        mNetworkReceiver = new CheckConnectivity();
        registerNetworkBroadcastForNougat();
        parentLayout = findViewById(android.R.id.content);

        rv_songs =findViewById(R.id.rv_songs);
        playlistSongsAdapter =new MusicPlaylistSongsAdapter(songsList,context);
        layoutManagerSongs =new LinearLayoutManager(context);
        rv_songs.setLayoutManager(layoutManagerSongs);
        rv_songs.setAdapter(playlistSongsAdapter);

        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(R.string.album);

        img_back=findViewById(R.id.img_back);
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
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}
