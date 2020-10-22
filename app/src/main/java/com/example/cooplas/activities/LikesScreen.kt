package com.example.cooplas.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cooplas.R
import com.example.cooplas.adapters.LikesAdapter
import com.example.cooplas.models.LikesData
import com.example.cooplas.models.PostlikeRes
import com.example.cooplas.utils.AppManager
import com.jobesk.gong.utils.getAccessToken
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_likes_screen.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikesScreen : AppCompatActivity() {
    var likes_list:ArrayList<LikesData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likes_screen)
        rl_back.setOnClickListener { finish() }
        rcv_likes.adapter=LikesAdapter(this,likes_list)
        rcv_likes.layoutManager=LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        get_post_likes()
    }

    private fun get_post_likes() {
        val kProgressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.post_likes("Bearer " + getAccessToken(this), "8").enqueue(object :
            Callback<PostlikeRes> {

            override fun onFailure(call: Call<PostlikeRes>, t: Throwable) {
                kProgressHUD.dismiss()
                Toast.makeText(this@LikesScreen, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<PostlikeRes>, response: Response<PostlikeRes>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    likes_list.clear()
                    response.body()?.likes?.let { likes_list.addAll(it) }
                    rcv_likes.adapter?.notifyDataSetChanged()
                } else {
                    val obj = JSONObject(response.errorBody()?.string())
                    Toast.makeText(this@LikesScreen, obj.get("message").toString(), Toast.LENGTH_SHORT).show()
                }

            }
        })
    }
}