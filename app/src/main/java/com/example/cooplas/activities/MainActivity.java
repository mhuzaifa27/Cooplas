package com.example.cooplas.activities;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cooplas.R;
import com.example.cooplas.events.VerifyEvent;
import com.example.cooplas.fragments.Main.ChatFragment;
import com.example.cooplas.fragments.Main.DiscoverFragment;
import com.example.cooplas.fragments.Main.HomeFragment;
import com.example.cooplas.fragments.Main.VideosFragment;
import com.example.cooplas.models.profile.Post;
import com.example.cooplas.models.profile.ProfileModel;
import com.example.cooplas.signup_screens.EmailInAppScreeenVerification;
import com.example.cooplas.utils.CheckConnectivity;
import com.example.cooplas.utils.CheckInternetEvent;
import com.example.cooplas.utils.SessionManager;
import com.example.cooplas.utils.ShowDialogues;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout main_frame;

    private HomeFragment homeFragment;
    private ChatFragment chatFragment;
    private VideosFragment videosFragment;
    private DiscoverFragment discoverFragment;
    private BroadcastReceiver mNetworkReceiver;
    private FragmentManager fm = null;
    public static final String TAG_HOME = "tag_home";
    public static final String TAG_VIDEOS = "tag_videos";
    public static final String TAG_CHAT = "tag_chat";
    public static final String TAG_DISCOVER = "tag_discover";
    public static String CURRENT_TAG = TAG_HOME;
    public static int navItemIndex = 0;
    private Handler mHandler;
    private SessionManager sessionManager;
    private EventBus eventBus = EventBus.getDefault();
    private boolean shouldLoadHomeFragOnBackPress = true;
    private View parentLayout;
    private String extras;
    private LinearLayout ll_home, ll_chat, ll_videos, ll_discover;
    private ImageView img_home, img_chat, img_videos, img_discover;
    private TextView tv_home, tv_chat, tv_videos, tv_discover;
    private TextView resend_tv, verify_email_tv;
    RelativeLayout verifyEmailCon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveUserData();
        Log.d("token", "Userdetails: userID:" + FunctionsKt.getUserID(getApplicationContext()) + "  userName:" + FunctionsKt.getUserName(getApplicationContext()) + "  userToken:" + FunctionsKt.getAccessToken(getApplicationContext()));
        verifyEmailCon = findViewById(R.id.verifyEmailCon);
        iniComponents();
        loadHomeFragment();

        ll_home.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
        ll_videos.setOnClickListener(this);
        ll_discover.setOnClickListener(this);

    }

    private void iniComponents() {
        extras = getIntent().getStringExtra("CURRENT_TAG");

        //checkIntentData();

        main_frame = findViewById(R.id.main_frame);
        mNetworkReceiver = new CheckConnectivity();
        fm = getSupportFragmentManager();
        mHandler = new Handler();
        eventBus.register(this);
        registerNetworkBroadcastForNougat();

        homeFragment = new HomeFragment();
        videosFragment = new VideosFragment();
        chatFragment = new ChatFragment();
        discoverFragment = new DiscoverFragment();
        sessionManager = new SessionManager(this);

        ll_home = findViewById(R.id.ll_home);
        ll_chat = findViewById(R.id.ll_chat);
        ll_videos = findViewById(R.id.ll_videos);
        ll_discover = findViewById(R.id.ll_discover);

        img_home = findViewById(R.id.img_home);
        img_chat = findViewById(R.id.img_chat);
        img_videos = findViewById(R.id.img_videos);
        img_discover = findViewById(R.id.img_discover);

        tv_home = findViewById(R.id.tv_home);
        tv_chat = findViewById(R.id.tv_chat);
        tv_videos = findViewById(R.id.tv_videos);
        tv_discover = findViewById(R.id.tv_discover);

        parentLayout = findViewById(android.R.id.content);

        resend_tv = findViewById(R.id.resend_tv);
        verify_email_tv = findViewById(R.id.verify_email_tv);


        resend_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resendCode();
            }
        });

        verify_email_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(), EmailInAppScreeenVerification.class);
                startActivity(intent);

            }
        });


    }





    /*private void checkIntentData() {
        if(extras != null ){
            if (extras.equals("tag_live")) {
                navItemIndex=2;
                CURRENT_TAG=TAG_LIVE;
            }
            else if (extras.equals("tag_settings")) {
                navItemIndex=4;
                CURRENT_TAG=TAG_SETTINGS;
            }
            else if (extras.equals("tag_search")) {
                navItemIndex=1;
                CURRENT_TAG=TAG_SEARCH;
            }
            else if (extras.equals("tag_alert")) {
                navItemIndex=3;
                CURRENT_TAG=TAG_ALERT;

            }
        }
        else{
            navItemIndex=0;
            CURRENT_TAG=TAG_HOME;
        }
    }*/

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

    @Override
    protected void onPause() {
        super.onPause();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void loadHomeFragment() {

        selectNavMenu();
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
               /* fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);*/
                fragmentTransaction.replace(R.id.main_frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
    }

    private void selectNavMenu() {
        switch (navItemIndex) {
            case 0:
                setHome();
                break;
            case 1:
                setChat();
                break;
            case 2:
                setVideos();
                break;
            case 3:
                setDiscover();
                break;
        }
    }

    private void setDiscover() {
        img_home.setColorFilter(ContextCompat.getColor(this, R.color.dark_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_home.setTextColor(getResources().getColor(R.color.dark_grey));
        ll_home.setBackgroundColor(getResources().getColor(R.color.white));

        img_chat.setColorFilter(ContextCompat.getColor(this, R.color.dark_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_chat.setTextColor(getResources().getColor(R.color.dark_grey));
        ll_chat.setBackgroundColor(getResources().getColor(R.color.white));

        img_videos.setColorFilter(ContextCompat.getColor(this, R.color.dark_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_videos.setTextColor(getResources().getColor(R.color.dark_grey));
        ll_videos.setBackgroundColor(getResources().getColor(R.color.white));

        img_discover.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_discover.setTextColor(getResources().getColor(R.color.white));
        ll_discover.setBackgroundColor(getResources().getColor(R.color.colorSecond));
    }

    private void setVideos() {
        img_home.setColorFilter(ContextCompat.getColor(this, R.color.dark_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_home.setTextColor(getResources().getColor(R.color.dark_grey));
        ll_home.setBackgroundColor(getResources().getColor(R.color.white));

        img_chat.setColorFilter(ContextCompat.getColor(this, R.color.dark_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_chat.setTextColor(getResources().getColor(R.color.dark_grey));
        ll_chat.setBackgroundColor(getResources().getColor(R.color.white));

        img_videos.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_videos.setTextColor(getResources().getColor(R.color.white));
        ll_videos.setBackgroundColor(getResources().getColor(R.color.colorSecond));

        img_discover.setColorFilter(ContextCompat.getColor(this, R.color.dark_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_discover.setTextColor(getResources().getColor(R.color.dark_grey));
        ll_discover.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void setChat() {
        img_home.setColorFilter(ContextCompat.getColor(this, R.color.dark_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_home.setTextColor(getResources().getColor(R.color.dark_grey));
        ll_home.setBackgroundColor(getResources().getColor(R.color.white));

        img_chat.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_chat.setTextColor(getResources().getColor(R.color.white));
        ll_chat.setBackgroundColor(getResources().getColor(R.color.colorSecond));

        img_videos.setColorFilter(ContextCompat.getColor(this, R.color.dark_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_videos.setTextColor(getResources().getColor(R.color.dark_grey));
        ll_videos.setBackgroundColor(getResources().getColor(R.color.white));

        img_discover.setColorFilter(ContextCompat.getColor(this, R.color.dark_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_discover.setTextColor(getResources().getColor(R.color.dark_grey));
        ll_discover.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void setHome() {
        img_home.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_home.setTextColor(getResources().getColor(R.color.white));
        ll_home.setBackgroundColor(getResources().getColor(R.color.colorSecond));

        img_chat.setColorFilter(ContextCompat.getColor(this, R.color.dark_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_chat.setTextColor(getResources().getColor(R.color.dark_grey));
        ll_chat.setBackgroundColor(getResources().getColor(R.color.white));

        img_videos.setColorFilter(ContextCompat.getColor(this, R.color.dark_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_videos.setTextColor(getResources().getColor(R.color.dark_grey));
        ll_videos.setBackgroundColor(getResources().getColor(R.color.white));

        img_discover.setColorFilter(ContextCompat.getColor(this, R.color.dark_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_discover.setTextColor(getResources().getColor(R.color.dark_grey));
        ll_discover.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                return homeFragment;
            case 2:
                return videosFragment;
            case 1:
                return chatFragment;
            case 3:
                return discoverFragment;
            default:
                return new HomeFragment();
        }
    }

    @Override
    public void onBackPressed() {
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        String catfrag = sessionManager.getfragmentval("catfrag");
        if (catfrag.equals("10")) {
            sessionManager.setfragmentval("catfrag", "0");
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
            return;
        }
        clickDone();
    }

    public void clickDone() {
        new AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(R.string.close_warning)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    //    /**
//     * EVENTS
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(NotificationReceivedEvent event) {
//        if (event.isIS_NOTIFY()) {
//            Snackbar snackbar = Snackbar.make(parentLayout, event.getRemoteMessage().getNotification().getTitle(), Snackbar.LENGTH_LONG);
//            View customView = getLayoutInflater().inflate(R.layout.snackbar_custom_notify, null);
//            //snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
//            Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
//            snackbarLayout.setPadding(0, 0, 0, 0);
//            TextView tv_subject = customView.findViewById(R.id.tv_subject);
//            TextView tv_body = customView.findViewById(R.id.tv_body);
//
//            tv_subject.setText(event.getRemoteMessage().getNotification().getTitle());
//            tv_body.setText(event.getRemoteMessage().getNotification().getBody());
//
//            snackbarLayout.addView(customView);
//            snackbar.show();
//        }
//    }
//
//    ;
//
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CheckInternetEvent event) {
        Log.d("SsS", "checkInternetAvailability: called");
        if (event.isIS_INTERNET_AVAILABLE()) {
            ShowDialogues.SHOW_SNACK_BAR(parentLayout, MainActivity.this, getString(R.string.snackbar_internet_available));
            Log.d("fffff", "onMessageEvent: " + CURRENT_TAG);
            switch (CURRENT_TAG) {
                case TAG_HOME:
                    /*HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(TAG_HOME);
                    homeFragment.loadUserProfileData();*/
                case TAG_VIDEOS:
                    /*AlertFragment alertFragment = (AlertFragment) getSupportFragmentManager().findFragmentByTag(TAG_ALERT);
                    alertFragment.getAllNotifications();*/
                case TAG_CHAT:
                   /* StartBroadcastFragment startBroadcastFragment = (StartBroadcastFragment) getSupportFragmentManager().findFragmentByTag(TAG_LIVE);
                    startBroadcastFragment.loadFollowingStreams();*/
                case TAG_DISCOVER:
                    /*SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(TAG_SETTINGS);
//                    settingsFragment.getSettingsData();*/
            }
        } else {
            ShowDialogues.SHOW_SNACK_BAR(parentLayout, MainActivity.this, getString(R.string.snackbar_check_internet));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                break;
            case R.id.ll_chat:
                navItemIndex = 1;
                CURRENT_TAG = TAG_CHAT;
                break;
            case R.id.ll_videos:
                navItemIndex = 2;
                CURRENT_TAG = TAG_VIDEOS;
                break;
            case R.id.ll_discover:
                navItemIndex = 3;
                CURRENT_TAG = TAG_DISCOVER;
                break;
            default:
                navItemIndex = 0;
        }
        loadHomeFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VerifyEvent event) {
        saveUserData();

    }

    private void saveUserData() {

        String accessToken = FunctionsKt.getAccessToken(getApplicationContext());
        APIInterface apiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);
        Call<ProfileModel> call = apiInterface.getUserWall("Bearer " + accessToken, String.valueOf(0));
        call.enqueue(new Callback<ProfileModel>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                Log.d("updateUserData", "" + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    ProfileModel model = response.body();

                    String nameF = model.getWall().getFirstName();
                    String nameL = model.getWall().getLastName();
                    String email = model.getWall().getEmail();
                    String image = model.getWall().getProfilePic();
                    String token = model.getWall().getAuthToken();
                    String id = String.valueOf(model.getWall().getId());
                    String role = String.valueOf(model.getWall().getRole());
                    String gender = String.valueOf(model.getWall().getGender());
                    String userName = String.valueOf(model.getWall().getUsername());
                    String emailVerified = String.valueOf(model.getWall().getEmailVerified());


                    if (emailVerified.equalsIgnoreCase("true")) {
                        verifyEmailCon.setVisibility(View.GONE);
                    } else {
                        verifyEmailCon.setVisibility(View.VISIBLE);
                    }

                    FunctionsKt.saveUserDetails(getApplicationContext(), id, userName, role, image, email);
                    FunctionsKt.saveAccessToken(getApplicationContext(), token);
                    FunctionsKt.savegender(getApplicationContext(), gender);
                    FunctionsKt.saveEmailVerified(getApplicationContext(), emailVerified);
                    FunctionsKt.SaveNames(getApplicationContext(), nameF, nameL);

                }
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();

            }
        });
    }

    private void resendCode() {
        KProgressHUD progressHUD = KProgressHUD.create(MainActivity.this);
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(getApplicationContext());
        APIInterface apiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);
        Call<JsonObject> call = apiInterface.emailVerifyMain("Bearer " + accessToken);
        call.enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("updateUserData", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Code has been Send to your email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }

}
