package com.example.cooplas.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.cooplas.R
import com.example.cooplas.activities.ProfileActivity
import com.example.cooplas.activities.SigninSignupScreen
import com.example.cooplas.models.SignUpSigninRes
import com.example.cooplas.utils.AppConstants
import com.example.cooplas.utils.AppManager
import com.jobesk.gong.utils.getEmail
import com.jobesk.gong.utils.getPassword
import com.jobesk.gong.utils.saveAccessToken
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreen : AppCompatActivity() {
    var new_user=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val pref = getSharedPreferences(AppConstants.SHARED_PREF, 0)
        when(pref.getBoolean("Already_exist",false)){
            true->{new_user=pref.getBoolean("Already_exist",false)}
            false->{pref.edit().putBoolean("Already_exist",true).apply()}
        }
        Handler().postDelayed({
            when(new_user){
                true->{
                    if ((getEmail(this) != "") or (getPassword(this) != "")){
                     getEmail(this)?.let { getPassword(this)?.let { it1 -> login(it, it1) } }
                     }
                    else{
                    startActivity(Intent(this, SigninSignupScreen::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
                }
                false->{
                    startActivity(Intent(this, SplashScreen2::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
        }, 3000)

    }

    private fun login(email: String, password: String) {
     //   val progressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.login(email,password).enqueue(object :
            Callback<SignUpSigninRes> {

            override fun onFailure(call: Call<SignUpSigninRes>, t: Throwable) {
            //    progressHUD.dismiss()
                Toast.makeText(this@SplashScreen,t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<SignUpSigninRes>, response: Response<SignUpSigninRes>) {
           //     progressHUD.dismiss()
                if (response.isSuccessful){
                   // Toast.makeText(this@SigninSignupScreen,response.body()?.message, Toast.LENGTH_SHORT).show()
                    saveAccessToken(this@SplashScreen,response.body()?.user?.auth_token?:"")
//                    when(response.body()?.status){
//                            200-> {
                    startActivity(
                        Intent(this@SplashScreen, ProfileActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//                        }
//                    }
                }
                else {
                    val jsonObject= JSONObject(response.errorBody()?.string())
                    Toast.makeText(this@SplashScreen,jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SplashScreen, SigninSignupScreen::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                }
            }
        })
    }


}
