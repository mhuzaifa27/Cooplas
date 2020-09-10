package com.example.cooplas.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cooplas.R
import com.example.cooplas.activities.MainActivity
import com.example.cooplas.activities.SigninSignupScreen
import kotlinx.android.synthetic.main.activity_splash_screen5.*

class SplashScreen5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen5)
        rl_continue.setOnClickListener {
            startActivity(Intent(this, SigninSignupScreen::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}