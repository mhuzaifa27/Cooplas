package com.example.cooplas.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cooplas.R
import com.example.cooplas.adapters.FollowUnfollowAdapter
import com.example.cooplas.adapters.LikesAdapter
import com.example.cooplas.models.FollowersRes
import com.example.cooplas.models.LikesData
import com.example.cooplas.models.User
import com.example.cooplas.utils.AppManager
import com.jobesk.gong.utils.getAccessToken
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_follower_screen.*
import kotlinx.android.synthetic.main.activity_following_screen.*
import kotlinx.android.synthetic.main.activity_following_screen.rl_back
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingScreen : AppCompatActivity() {
    var following_list:ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following_screen)
        rl_back.setOnClickListener { finish() }
        rcv_following.adapter= FollowUnfollowAdapter(this,following_list,1)
        rcv_following.layoutManager= LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        get_following()
    }

    private fun get_following() {
        val kProgressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.get_following("Bearer " + getAccessToken(this)).enqueue(object :
            Callback<FollowersRes> {

            override fun onFailure(call: Call<FollowersRes>, t: Throwable) {
                kProgressHUD.dismiss()
                Toast.makeText(this@FollowingScreen, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<FollowersRes>, response: Response<FollowersRes>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    following_list.clear()
                    response.body()?.following?.let { following_list.addAll(it) }
                    rcv_following.adapter?.notifyDataSetChanged()
                } else {
                    val obj = JSONObject(response.errorBody()?.string())
                    Toast.makeText(this@FollowingScreen, obj.get("message").toString(), Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

}