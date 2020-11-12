package com.example.cooplas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cooplas.R
import com.example.cooplas.models.GeneralRes
import com.example.cooplas.models.User
import com.example.cooplas.utils.AppManager
import com.jobesk.gong.utils.getAccessToken
import com.kaopiz.kprogresshud.KProgressHUD
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowUnfollowAdapter (var context: Context, var likes_list: ArrayList<User>,var from_following: Int): RecyclerView.Adapter<FollowUnfollowAdapter.FollowUnfollowView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowUnfollowView {
        return FollowUnfollowView(LayoutInflater.from(context).inflate(R.layout.like_item, parent, false))
    }

    override fun onBindViewHolder(holder: FollowUnfollowView, position: Int) {
//        val item = likes_list[position]
//        Glide.with(context).load(item.profile_pic).into(holder.iv_profile)
//        holder.name.text = "${item.first_name} ${item.last_name}"
//        when (item.following) {
//            0 -> {
//                holder.follow.visibility = View.VISIBLE
//                holder.unfollow.visibility = View.GONE
//            }
//            else -> {
//                holder.follow.visibility = View.GONE
//                holder.unfollow.visibility = View.VISIBLE
//            }
//        }

//        if (from_following == 1) {
//            holder.follow.visibility = View.GONE
//            holder.unfollow.visibility = View.VISIBLE
//        }
//        holder.follow.setOnClickListener {
//            holder.follow.visibility = View.GONE
//            holder.unfollow.visibility = View.VISIBLE
//            follow_user(item.id)
//        }
//        holder.unfollow.setOnClickListener {
//            holder.follow.visibility = View.VISIBLE
//            holder.unfollow.visibility = View.GONE
//            unfollow_user(item.id)
//        }
    }

    override fun getItemCount(): Int {
        return likes_list.size
    }

    private fun unfollow_user(userId: Int) {
        val kProgressHUD = KProgressHUD.create(context).show()
        AppManager.getInstance().restClient.cooplas.follow_user(
            "https://cooplas.jobesk.com/api/unfollow/user/$userId",
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
                } else {
                    val obj = JSONObject(response.errorBody()?.string())
                    Toast.makeText(context, obj.get("message").toString(), Toast.LENGTH_SHORT)
                        .show()
                }

            }
        })
    }

    private fun follow_user(userId: Int) {
        val kProgressHUD = KProgressHUD.create(context).show()
        AppManager.getInstance().restClient.cooplas.follow_user(
            "https://cooplas.jobesk.com/api/follow/user/$userId",
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
                } else {
                    val obj = JSONObject(response.errorBody()?.string())
                    Toast.makeText(context, obj.get("message").toString(), Toast.LENGTH_SHORT)
                        .show()
                }

            }
        })
    }

    class FollowUnfollowView(view: View) : RecyclerView.ViewHolder(view) {
//        var follow = view.iv_follow
//        var unfollow = view.iv_unfollow
//        var iv_profile = view.iv_profile
//        var name = view.tv_name
    }
}