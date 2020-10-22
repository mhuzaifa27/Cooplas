package com.example.cooplas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cooplas.R

class NotificationAdapter(var context: Context,): RecyclerView.Adapter<NotificationAdapter.NotificationView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationView {
        return NotificationView(LayoutInflater.from(context).inflate(R.layout.notificationitem, parent, false))
    }

    override fun onBindViewHolder(holder: NotificationView, position: Int) {

    }

    override fun getItemCount(): Int {
       return 10
    }

    class NotificationView(view: View) : RecyclerView.ViewHolder(view) {
//        var follow = view.iv_follow
//        var unfollow = view.iv_unfollow
//        var iv_profile = view.iv_profile
//        var name = view.tv_name
    }
}