package com.example.cooplas.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cooplas.R
import com.example.cooplas.activities.LikesScreen
import com.example.cooplas.activities.PostCommentScreen
import com.example.cooplas.activities.PostImageVideo
import com.example.cooplas.activities.ProfileActivity
import com.example.cooplas.models.AddCommentRes
import com.example.cooplas.models.Comments
import com.example.cooplas.models.GeneralRes
import com.example.cooplas.models.Post
import com.example.cooplas.utils.AppConstants
import com.example.cooplas.utils.AppManager
import com.jobesk.gong.utils.getAccessToken
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.postitem.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsAdapter(var context: Context, var post_list: ArrayList<Post>): RecyclerView.Adapter<PostsAdapter.Postview>() {
    var comment_list:ArrayList<Comments> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Postview {
        return Postview(LayoutInflater.from(context).inflate(R.layout.postitem, parent, false))
    }

    override fun onBindViewHolder(holder: Postview, position: Int) {
        val item=post_list[position]
        holder.tv_name.text="${item.user?.first_name} ${item.user?.last_name}"
        Glide.with(context).load(item.user?.profile_pic).into(holder.iv_profile)
        holder.tv_description.text=item.body?:""
        if (!item.media.isNullOrEmpty())
            Glide.with(context).load(item.media[0].path).into(holder.cover_image)
        holder.like_count.text="${item.likes_count?:""}"
        holder.comment_count.text="${item.comments_count?:""}"
        comment_list.clear()
        comment_list.addAll(item.comments)
        holder.rcv_coment.adapter=CommentAdapter(context, comment_list)
        holder.rcv_coment.layoutManager= LinearLayoutManager(context)

        holder.iv_option.setOnClickListener {
            when(holder.edit_del.visibility){
                View.VISIBLE -> {
                    holder.edit_del.visibility = View.GONE
                }
                View.GONE -> {
                    holder.edit_del.visibility = View.VISIBLE
                }
            }
        }

        holder.rl_like.setOnClickListener {
            context.startActivity(Intent(context, LikesScreen::class.java))
        }

        holder.rl_comment.setOnClickListener {
            AppManager.getInstance().post_data=item
            context.startActivity(Intent(context, PostCommentScreen::class.java))
        }

        holder.cover_image.setOnClickListener {
            if (item.media.isNotEmpty())
            context.startActivity(
                Intent(context, PostImageVideo::class.java).putExtra(
                    AppConstants.IMAGE_PATH,
                    item.media[0].path
                )
            )
            else
                context.startActivity(
                    Intent(context, PostImageVideo::class.java).putExtra(
                        AppConstants.IMAGE_PATH,
                        ""
                    )
                )
        }

        holder.send.setOnClickListener {
            add_comment(item.id, holder.et_comment.text.toString(), holder.et_comment, context)
        }

        holder.del_post.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Cancel"
                ) { p0, _ -> p0?.dismiss() }
                .setNegativeButton("Delete"
                ) { p0, _ -> delete_post(item.id, position, holder.edit_del) }
                .show()
        }
    }

    private fun add_comment(
        post_id: Int,
        comment_body: String,
        etComment: EditText,
        context: Context
    ) {
        val kProgressHUD = KProgressHUD.create(this.context).show()
        AppManager.getInstance().restClient.cooplas.add_comment(
            "Bearer " + getAccessToken(this.context),
            "$post_id",
            comment_body
        ).enqueue(object :
            Callback<AddCommentRes> {

            override fun onFailure(call: Call<AddCommentRes>, t: Throwable) {
                kProgressHUD.dismiss()
                Toast.makeText(this@PostsAdapter.context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<AddCommentRes>, response: Response<AddCommentRes>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    Toast.makeText(this@PostsAdapter.context, response.body()?.message, Toast.LENGTH_SHORT).show()
                    etComment.text.clear()
                    (context as ProfileActivity).getprofiledata()
                } else {
                    val obj = JSONObject(response.errorBody()?.string())
                    Toast.makeText(this@PostsAdapter.context, obj.get("message").toString(), Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    private fun delete_post(post_id: Int, position: Int, ivOption: RelativeLayout) {
        val kProgressHUD = KProgressHUD.create(context).show()
        AppManager.getInstance().restClient.cooplas.delete_post(
            "https://cooplas.jobesk.com/api/delete/post/$post_id",
            "Bearer " + getAccessToken(context)
        ).enqueue(object :
            Callback<GeneralRes> {

            override fun onFailure(call: Call<GeneralRes>, t: Throwable) {
                kProgressHUD.dismiss()
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<GeneralRes>, response: Response<GeneralRes>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                    post_list.removeAt(position)
                    ivOption.visibility = View.GONE
                    notifyItemRemoved(position)
                    notifyDataSetChanged()
                } else {
                    val obj = JSONObject(response.errorBody()?.string())
                    Toast.makeText(context, obj.get("message").toString(), Toast.LENGTH_SHORT)
                        .show()
                }

            }
        })
    }

    override fun getItemCount(): Int {
        return post_list.size
    }

    class Postview(var view: View): RecyclerView.ViewHolder(view){
        var iv_profile=view.iv_profile
        var tv_name=view.tv_name
        var tv_description=view.tv_description
        var like_count=view.tv_like
        var comment_count=view.tv_comment
        var rcv_coment=view.rcv_comment
        var iv_option=view.iv_option
        var edit_del=view.rl_edit_del
        var rl_like=view.rl_like
        var rl_comment=view.rl_comment
        var cover_image=view.iv_image
        var del_post=view.tv_delete
        var et_comment=view.et_comment
        var send=view.iv_send
    }

}