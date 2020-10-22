package com.example.cooplas.signup_screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.example.cooplas.R
import com.example.cooplas.models.GeneralRes
import com.example.cooplas.utils.AppManager
import com.jobesk.gong.utils.getAccessToken
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_phone_verificatoin_screen.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhoneVerificatoinScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verificatoin_screen)
        et_one.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                et_two.requestFocus()
            }
        })

        et_two.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                et_three.requestFocus()
            }
        })

        et_three.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                et_four.requestFocus()
            }
        })

        tv_resend.setOnClickListener {
            resent_otp()
        }

        rl_next.setOnClickListener {
            verify_otp("${et_one.text}${et_two.text}${et_three.text}${et_four.text}")
        }
    }

    private fun verify_otp(code: String) {
        val progressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.verify_otp("Bearer " + getAccessToken(this),code).enqueue(object : Callback<GeneralRes> {

            override fun onFailure(call: Call<GeneralRes>, t: Throwable) {
                progressHUD.dismiss()
                Toast.makeText(this@PhoneVerificatoinScreen,t.message, Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<GeneralRes>, response: Response<GeneralRes>) {
                progressHUD.dismiss()
                if (response.isSuccessful){
                    Toast.makeText(this@PhoneVerificatoinScreen,response.body()?.message, Toast.LENGTH_SHORT).show()
//                    when(response.body()?.status){
//                        200-> {
//                            startActivity(
//                                Intent(this@PhoneVerificatoinScreen, WelcomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
//                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//                        }
//                    }
                }
                else {
                    val jsonObject= JSONObject(response.errorBody()?.string())
                    Toast.makeText(this@PhoneVerificatoinScreen,jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun resent_otp() {
        val progressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.resent_otp("Bearer " + getAccessToken(this)).enqueue(object : Callback<GeneralRes> {
            override fun onFailure(call: Call<GeneralRes>, t: Throwable) {
                progressHUD.dismiss()
                Toast.makeText(this@PhoneVerificatoinScreen,t.message, Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<GeneralRes>, response: Response<GeneralRes>) {
                progressHUD.dismiss()
                if (response.isSuccessful){
                    Toast.makeText(this@PhoneVerificatoinScreen,response.body()?.message, Toast.LENGTH_SHORT).show()
//                    when(response.body()?.status){
//                        200-> {
//                            tv_resend_otp.visibility= View.GONE
//                            timerobject.start()
//                            resentOtpDialog()
//                        }
//                    }
                }
                else {
                    val jsonObject= JSONObject(response.errorBody()?.string())
                    Toast.makeText(this@PhoneVerificatoinScreen,jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}