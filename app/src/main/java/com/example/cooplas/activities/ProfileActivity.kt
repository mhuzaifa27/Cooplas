package com.example.cooplas.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cooplas.R
import com.example.cooplas.adapters.PostsAdapter
import com.example.cooplas.models.Post
import com.example.cooplas.models.ProfileRes
import com.example.cooplas.models.Wall
import com.example.cooplas.utils.AppConstants
import com.example.cooplas.utils.AppManager
import com.jobesk.gong.utils.getAccessToken
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.rl_back
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        rl_back.setOnClickListener { finish() }

        rl_following.setOnClickListener {
            startActivity(Intent(this, FollowingScreen::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        rl_followers.setOnClickListener {
            startActivity(Intent(this, FollowerScreen::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onResume() {
        super.onResume()
        getprofiledata()
    }

    fun getprofiledata() {
        val kProgressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.get_user_profile("Bearer " + getAccessToken(this)).enqueue(object : Callback<ProfileRes> {

            override fun onFailure(call: Call<ProfileRes>, t: Throwable) {
                kProgressHUD.dismiss()
                Toast.makeText(this@ProfileActivity, t.message, Toast.LENGTH_SHORT).show()
            }


            override fun onResponse(call: Call<ProfileRes>, response: Response<ProfileRes>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    response.body()?.wall?.let { populatedata(it) }
                }
            }
        })
    }

    private fun populatedata(wall: Wall) {
        tv_name.text="${wall.first_name} ${wall.last_name}"
        Glide.with(this).load(wall.profile_pic).into(iv_profile)
        tv_name_heading.text="${wall.first_name} ${wall.last_name}"
        tv_user_name.text="@${wall.username?:""}"
        tv_following.text="${wall.following_count}"
        tv_follow.text="${wall.followers_count}"
        rcv_post.adapter=PostsAdapter(this, wall.posts as ArrayList<Post>)
        rcv_post.layoutManager=LinearLayoutManager(this)
        rl_edit.setOnClickListener {
            startActivity(Intent(this, EditProfileScreen::class.java)
                .putExtra(AppConstants.IMAGE_PATH,wall.profile_pic)
                .putExtra(AppConstants.GENDER,wall.gender)
                .putExtra(AppConstants.FIRST_NAME,wall.first_name)
                .putExtra(AppConstants.LAST_NAME,wall.last_name)
                .putExtra(AppConstants.USERNAME,wall.username)
            )
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

}