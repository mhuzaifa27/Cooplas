package com.example.cooplas.fragments.Main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cooplas.R;
import com.example.cooplas.fragments.Others.CallLogsFragment;
import com.example.cooplas.fragments.Others.InboxFragment;
import com.example.cooplas.utils.CheckConnectivity;
import com.example.cooplas.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class ChatFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ChatFragment";
    private Context context;
    private Activity activity;

    CallLogsFragment callLogsFragment;
    InboxFragment inboxFragment;

    public static final String TAG_INBOX = "tag_inbox";
    public static final String TAG_CALL_LOGS = "tag_call_logs";
    public static String CURRENT_TAG = TAG_INBOX;
    public static int navItemIndex = 0;
    private Handler mHandler;
    private LinearLayout ll_inbox,ll_call_logs;
    private TextView tv_inbox,tv_call_logs;
    private View view_inbox,view_call_logs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        iniComponents(view);
        loadHomeFragment();

        ll_inbox.setOnClickListener(this);
        ll_call_logs.setOnClickListener(this);

//        /**********Swipe Listener On Fragment*************/
//        final GestureDetector gesture = new GestureDetector(getActivity(),
//                new GestureDetector.SimpleOnGestureListener() {
//
//                    @Override
//                    public boolean onDown(MotionEvent e) {
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                                           float velocityY) {
//                        Log.i(TAG, "onFling has been called!");
//                        final int SWIPE_MIN_DISTANCE = 120;
//                        final int SWIPE_MAX_OFF_PATH = 250;
//                        final int SWIPE_THRESHOLD_VELOCITY = 200;
//                        try {
//                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//                                return false;
//                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
//                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                                setCallLogTab();
//                                Log.i(TAG, "Right to Left");
//                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
//                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                                Log.i(TAG, "Left to Right");
//                                setInboxTab();
//                            }
//                        } catch (Exception e) {
//                            // nothing
//                        }
//                        return super.onFling(e1, e2, velocityX, velocityY);
//                    }
//                });
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return gesture.onTouchEvent(event);
//            }
//        });
//        /***********************/
//
        return view;
    }

    private void iniComponents(View view) {
        context=getContext();
        activity=getActivity();

        mHandler = new Handler();

        callLogsFragment = new CallLogsFragment();
        inboxFragment = new InboxFragment();

        ll_inbox=view.findViewById(R.id.ll_inbox);
        ll_call_logs=view.findViewById(R.id.ll_call_logs);

        tv_inbox=view.findViewById(R.id.tv_inbox);
        tv_call_logs=view.findViewById(R.id.tv_call_logs);

        view_inbox=view.findViewById(R.id.view_inbox);
        view_call_logs=view.findViewById(R.id.view_call_logs);
    }
    private void loadHomeFragment() {
        setTabView();
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getMeetFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                /*fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);*/
                fragmentTransaction.replace(R.id.chat_frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
    }

    private void setTabView() {
        switch (navItemIndex) {
            case 0:
                setInboxTab();
                break;
            case 1:
                setCallLogTab();
                break;
        }
    }

    private void setCallLogTab() {
        ll_inbox.setBackgroundColor(getResources().getColor(R.color.grey_light));
        tv_inbox.setTextColor(getResources().getColor(R.color.mid_grey));
        view_inbox.setVisibility(View.GONE);

        ll_call_logs.setBackgroundColor(getResources().getColor(R.color.white));
        tv_call_logs.setTextColor(getResources().getColor(R.color.colorSecond));
        view_call_logs.setVisibility(View.VISIBLE);
    }

    private void setInboxTab() {
        ll_inbox.setBackgroundColor(getResources().getColor(R.color.white));
        tv_inbox.setTextColor(getResources().getColor(R.color.colorSecond));
        view_inbox.setVisibility(View.VISIBLE);

        ll_call_logs.setBackgroundColor(getResources().getColor(R.color.grey_light));
        tv_call_logs.setTextColor(getResources().getColor(R.color.mid_grey));
        view_call_logs.setVisibility(View.GONE);
    }

    private Fragment getMeetFragment() {
        switch (navItemIndex) {
            case 0:
                CURRENT_TAG=TAG_INBOX;
                return inboxFragment;
            case 1:
                CURRENT_TAG=TAG_CALL_LOGS;
                return callLogsFragment;
            default:
                CURRENT_TAG=TAG_INBOX;
                return new InboxFragment();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_inbox:
                navItemIndex = 0;
                CURRENT_TAG = TAG_INBOX;
                break;
            case R.id.ll_call_logs:
                navItemIndex = 1;
                CURRENT_TAG = TAG_CALL_LOGS;
                break;
            default:
                navItemIndex = 0;
        }
        loadHomeFragment();
    }
}
