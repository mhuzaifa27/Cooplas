package com.example.cooplas.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cooplas.R
import com.example.cooplas.adapters.NotificationAdapter
import kotlinx.android.synthetic.main.activity_notification_screen.*

class NotificationScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_screen)
        rl_back.setOnClickListener { finish() }
        rcv_notification.adapter=NotificationAdapter(this)
        rcv_notification.layoutManager=LinearLayoutManager(this)
    }
}