package com.example.cooplas.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.cooplas.R
import com.example.cooplas.activities.MainActivity
import com.example.cooplas.activities.SigninSignupScreen
import com.example.cooplas.utils.AppConstants
import com.jobesk.gong.utils.*


class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var loggedIN = getLoggedIn(applicationContext)




        Handler().postDelayed({
            when (loggedIN.isEmpty()) {
                true -> {

                    startActivity(
                        Intent(
                            this,
                            SigninSignupScreen::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)


                }
                false -> {
                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )

//                    getEmail(this)?.let { getPassword(this)?.let { it1 -> Login(it, it1) } }
                }
            }
        }, 3000)

    }

//    private fun Login(email: String, password: String) {
//        val progressHUD = KProgressHUD.create(this).show()
//        AppManager.getInstance().restClient.cooplas.login(email, password)
//            .enqueue(object : Callback<SignUpSigninRes> {
//
//                override fun onFailure(call: Call<SignUpSigninRes>, t: Throwable) {
//                    progressHUD.dismiss()
//                    Toast.makeText(this@SplashScreen, t.message, Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onResponse(
//                    call: Call<SignUpSigninRes>,
//                    response: Response<SignUpSigninRes>
//                ) {
//                    progressHUD.dismiss()
//                    if (response.isSuccessful) {
//                        Toast.makeText(
//                            this@SplashScreen,
//                            response.body()?.message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        saveUserEmailAndPass(this@SplashScreen, email, password)
//                        saveAccessToken(
//                            this@SplashScreen,
//                            response.body()?.user?.auth_token ?: ""
//                        )
////                    when(response.body()?.status){
////                            200-> {
//
//                        var phoneVerified = response.body()?.user?.phone_verified
//                        if (phoneVerified == false) {
//                            startActivity(
//                                Intent(
//                                    this@SplashScreen,
//                                    PhoneVerificatoinScreen::class.java
//                                ).setFlags(
//                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                                )
//                            )
//                        }
//
//
//                        var emailVerified = response.body()?.user?.username
//                        if (emailVerified == null) {
//                            startActivity(
//                                Intent(
//                                    this@SplashScreen,
//                                    CompleteSignup::class.java
//                                ).setFlags(
//                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                                )
//                            )
//                        }
//
//                        Intent(this@SplashScreen, MainActivity::class.java).setFlags(
//                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                        )
//
//
//                    } else {
//                        val jsonObject = JSONObject(response.errorBody()?.string())
//                        Toast.makeText(
//                            this@SplashScreen,
//                            jsonObject.get("message").toString(),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            })
//    }
}
