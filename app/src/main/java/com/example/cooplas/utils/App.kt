package com.example.cooplas.utils

import android.app.Application
import com.example.cooplas.AgoraClasses.ChatManager




class  App : Application(){


    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
}