package com.example.cooplas.utils;

import android.app.Application;

import com.example.cooplas.AgoraClasses.ChatManager;

public class AGApplication extends Application {
    private static AGApplication sInstance;
    private ChatManager mChatManager;


    public static AGApplication the() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mChatManager = new ChatManager(this);
        mChatManager.init();
    }

    public ChatManager getChatManager() {
        return mChatManager;
    }
}