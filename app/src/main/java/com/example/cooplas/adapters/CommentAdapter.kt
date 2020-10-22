package com.example.cooplas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cooplas.R
import com.example.cooplas.models.Comments
import kotlinx.android.synthetic.main.comment_item.view.*

class CommentAdapter (var context: Context, var comment_list:ArrayList<Comments>): RecyclerView.Adapter<CommentAdapter.Commentview>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Commentview {
        return Commentview(LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false))
    }

    override fun onBindViewHolder(holder: Commentview, position: Int) {
        val item=comment_list[position]
        Glide.with(context).load(item.user.profile_pic).into(holder.comment_profile)
        holder.comment_name.text="${item.user.first_name} ${item.user.last_name}"
        holder.comment_body.text=item.body?:""
    }

    override fun getItemCount(): Int {
        return comment_list.size
    }

    class Commentview(var view: View): RecyclerView.ViewHolder(view){
        var comment_profile=view.iv_comment_profile
        var comment_name=view.tv_comment_name
        var comment_time=view.tv_comment_time
        var comment_body=view.tv_comment_body
    }

}