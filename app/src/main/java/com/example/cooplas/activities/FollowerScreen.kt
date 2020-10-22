package com.example.cooplas.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cooplas.R
import com.example.cooplas.adapters.FollowUnfollowAdapter
import com.example.cooplas.models.FollowersRes
import com.example.cooplas.models.User
import com.example.cooplas.utils.AppManager
import com.jobesk.gong.utils.getAccessToken
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_follower_screen.*
import kotlinx.android.synthetic.main.activity_follower_screen.rl_back
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerScreen : AppCompatActivity() {
    var follower_list:ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follower_screen)
        rl_back.setOnClickListener { finish() }
        rcv_follower.adapter= FollowUnfollowAdapter(this,follower_list,0)
        rcv_follower.layoutManager= LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        get_follower()
    }

    private fun get_follower() {
        val kProgressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.get_followers("Bearer " + getAccessToken(this)).enqueue(object :
            Callback<FollowersRes> {

            override fun onFailure(call: Call<FollowersRes>, t: Throwable) {
                kProgressHUD.dismiss()
                Toast.makeText(this@FollowerScreen, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<FollowersRes>, response: Response<FollowersRes>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    follower_list.clear()
                    response.body()?.followers?.let { follower_list.addAll(it) }
                    rcv_follower.adapter?.notifyDataSetChanged()
                } else {
                    val obj = JSONObject(response.errorBody()?.string())
                    Toast.makeText(this@FollowerScreen, obj.get("message").toString(), Toast.LENGTH_SHORT).show()
                }

            }
        })
    }
}