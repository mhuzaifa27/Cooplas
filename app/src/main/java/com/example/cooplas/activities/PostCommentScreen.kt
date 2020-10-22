package com.example.cooplas.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cooplas.R
import com.example.cooplas.adapters.CommentAdapter
import com.example.cooplas.models.*
import com.example.cooplas.utils.AppManager
import com.jobesk.gong.utils.getAccessToken
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_post_comment_screen.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostCommentScreen : AppCompatActivity() {
    var comment_list:ArrayList<Comments> = ArrayList()
    var item=AppManager.getInstance().post_data
    var total_comment=item.comments_count

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_comment_screen)
        rl_back.setOnClickListener { finish() }
        tv_name.text="${item.user?.first_name} ${item.user?.last_name}"
        Glide.with(this).load(item.user?.profile_pic).into(iv_profile)
        tv_description.text=item.body?:""
        if (!item.media.isNullOrEmpty())
            Glide.with(this).load(item.media[0].path).into(iv_image)
        tv_likes.text="${item.likes_count?:""}"
        tv_comments.text="$total_comment"
        /////////////////// Get All Comments ////////////
        getcommentdata(item.id)
        //////////////////////
        rl_like.setOnClickListener {
            startActivity(Intent(this, LikesScreen::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        iv_option.setOnClickListener {
            when(rl_edit_del.visibility){
                View.VISIBLE->{
                    rl_edit_del.visibility= View.GONE
                }
                View.GONE->{
                    rl_edit_del.visibility= View.VISIBLE
                }
            }
        }
        iv_send.setOnClickListener {
            add_comment(item.id, et_comment.text.toString())
        }

        tv_delete.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Cancel"
                ) { p0, _ -> p0?.dismiss() }
                .setNegativeButton("Delete"
                ) { p0, _ -> delete_post(item.id) }
                .show()
        }
    }

    private fun getcommentdata(post_id:Int) {
        val kProgressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.get_comment("Bearer " + getAccessToken(this),"$post_id").enqueue(object : Callback<CommentlistRes> {

            override fun onFailure(call: Call<CommentlistRes>, t: Throwable) {
                kProgressHUD.dismiss()
                Toast.makeText(this@PostCommentScreen, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<CommentlistRes>, response: Response<CommentlistRes>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    comment_list.clear()
                    response.body()?.comments?.let { comment_list.addAll(it) }
                    rcv_comment.adapter=CommentAdapter(this@PostCommentScreen, comment_list)
                    rcv_comment.layoutManager= LinearLayoutManager(this@PostCommentScreen)
                }
            }
        })
    }

    private fun add_comment(post_id: Int, comment_body: String) {
        val kProgressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.add_comment("Bearer " + getAccessToken(this), "$post_id", comment_body).enqueue(object : Callback<AddCommentRes> {

            override fun onFailure(call: Call<AddCommentRes>, t: Throwable) {
                kProgressHUD.dismiss()
                Toast.makeText(this@PostCommentScreen, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<AddCommentRes>, response: Response<AddCommentRes>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    Toast.makeText(this@PostCommentScreen, response.body()?.message, Toast.LENGTH_SHORT).show()
                    et_comment.text.clear()
                    response.body()?.comment?.let { comment_list.add(it) }
                    rcv_comment.adapter=CommentAdapter(this@PostCommentScreen, comment_list)
                    tv_comments.text="${total_comment+1}"

                } else {
                    val obj = JSONObject(response.errorBody()?.string())
                    Toast.makeText(this@PostCommentScreen, obj.get("message").toString(), Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    private fun delete_post(post_id: Int) {
        val kProgressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.delete_post(
            "https://cooplas.jobesk.com/api/delete/post/$post_id",
            "Bearer " + getAccessToken(this)
        ).enqueue(object :
            Callback<GeneralRes> {

            override fun onFailure(call: Call<GeneralRes>, t: Throwable) {
                kProgressHUD.dismiss()
                Toast.makeText(this@PostCommentScreen, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<GeneralRes>, response: Response<GeneralRes>) {
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    Toast.makeText(this@PostCommentScreen, response.body()?.message, Toast.LENGTH_SHORT).show()
                   finish()
                } else {
                    val obj = JSONObject(response.errorBody()?.string())
                    Toast.makeText(this@PostCommentScreen, obj.get("message").toString(), Toast.LENGTH_SHORT)
                        .show()
                }

            }
        })
    }
}