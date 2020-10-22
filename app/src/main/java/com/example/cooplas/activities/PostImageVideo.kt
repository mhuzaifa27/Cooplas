package com.example.cooplas.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import com.bumptech.glide.Glide
import com.example.cooplas.R
import com.example.cooplas.utils.AppConstants
import kotlinx.android.synthetic.main.activity_post_image_video.*

class PostImageVideo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_image_video)
        rl_back.setOnClickListener { finish() }
        Glide.with(this).load(intent.getStringExtra(AppConstants.IMAGE_PATH?:"")).into(iv_image)
//        videoview.setMediaController(MediaController(this))
//        videoview.setVideoPath("https://cooplas.jobesk.com/public/storage/media/10/albums/1/1-1.mp4")
//        videoview.start()
    }
}