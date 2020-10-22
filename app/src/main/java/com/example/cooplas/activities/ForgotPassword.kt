package com.example.cooplas.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cooplas.R
import com.example.cooplas.models.GeneralRes
import com.example.cooplas.signup_screens.PhoneVerificatoinScreen
import com.example.cooplas.utils.AppManager
import com.jobesk.gong.utils.isValidEmail
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        rl_back.setOnClickListener { finish() }
        rl_next.setOnClickListener {
            when{
                et_forgot_email.text.trim().isEmpty()-> run {
                    et_forgot_email.error = "Enter your Email"
                    return@setOnClickListener }

                !isValidEmail(et_forgot_email?.text?.trim().toString()) -> run {
                    et_forgot_email?.requestFocus()
                    et_forgot_email.error = "Invalid Email!"
                    return@setOnClickListener }

                else -> ""
            }
            forgot_password(et_forgot_email.text.toString())

        }
    }

    private fun forgot_password(email: String) {
        val progressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.forgot_password(email).enqueue(object :
            Callback<GeneralRes> {
            override fun onFailure(call: Call<GeneralRes>, t: Throwable) {
                progressHUD.dismiss()
                Toast.makeText(this@ForgotPassword,t.message, Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<GeneralRes>, response: Response<GeneralRes>) {
                progressHUD.dismiss()
                if (response.isSuccessful){
                    Toast.makeText(this@ForgotPassword,response.body()?.message, Toast.LENGTH_SHORT).show()
//                    when(response.body()?.status){
//                        201-> {
                    startActivity(Intent(this@ForgotPassword, PhoneVerificatoinScreen::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//                        }
//                    }
                }
                else {
                    val jsonObject= JSONObject(response.errorBody()?.string())
                    Toast.makeText(this@ForgotPassword,jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}