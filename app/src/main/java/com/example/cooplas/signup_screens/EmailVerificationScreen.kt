package com.example.cooplas.signup_screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.cooplas.activities.EnterNewPassword
import com.example.cooplas.R
import com.example.cooplas.models.GeneralRes

import com.example.cooplas.utils.AppManager
import com.example.cooplas.utils.callbacks.ForgetPasResponse
import com.google.gson.Gson
import com.jobesk.gong.utils.getAccessToken
import com.jobesk.gong.utils.saveAccessToken
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_phone_verificatoin_screen.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailVerificationScreen : AppCompatActivity() {

    var counter = 60;
    lateinit var timer: CountDownTimer;
    var timerCheck: Boolean = false
    lateinit var email: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verificatoin_screen)


        var intent = intent
        email = intent.getStringExtra("email").toString()



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

        et_four.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })



        rl_next.setOnClickListener {

            var inputString =
                "${et_one.text}${et_two.text}${et_three.text}${et_four.text}";

            verify_otp(inputString)
        }

        tv_resend.setOnClickListener {
            resent_otp(email)
        }


        count_tv.setText("60")
        tv_resend.isClickable = false
        timer = object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                counter = counter - 1;
                Log.d("timer", "onTick: " + counter)
                count_tv.setText(counter.toString())
                timerCheck = true
            }

            override fun onFinish() {

                tv_resend.isClickable = true
                Log.d("timer", "onTick: Finished")
                timerCheck = false
                count_tv.setText("")
                timer.cancel()
            }
        }
        timer.start()
    }

    private fun verify_otp(code: String) {

        val progressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.verify_forgetPassword(

            code
        ).enqueue(object : Callback<ForgetPasResponse> {
            override fun onFailure(call: Call<ForgetPasResponse>, t: Throwable) {
                progressHUD.dismiss()
                Toast.makeText(this@EmailVerificationScreen, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ForgetPasResponse>,
                response: Response<ForgetPasResponse>
            ) {
                progressHUD.dismiss()
                Log.d("PhoneVerificatoinScreen", "onResponse: " + response.body()?.toString())
                if (response.isSuccessful) {


                    var message = response.body()?.message

                    Toast.makeText(
                        this@EmailVerificationScreen,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()

                    if (message?.contains("Password reset code is incorrect")!!) {

                        return

                    }

                    var token = response.body()?.user?.authToken


//                    saveUserEmailAndPass(this@Email, , password)
                    if (token != null) {
                        saveAccessToken(
                            this@EmailVerificationScreen, token

                        )
                    }

                    startActivity(
                        Intent(this@EmailVerificationScreen, EnterNewPassword::class.java)
                    )

                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                } else {
                    val jsonObject = JSONObject(response.errorBody()?.string())
                    Toast.makeText(
                        this@EmailVerificationScreen,
                        jsonObject.get("message").toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun resent_otp(email: String) {

        val progressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.forgot_password(email).enqueue(object :
            Callback<GeneralRes> {
            override fun onFailure(call: Call<GeneralRes>, t: Throwable) {
                progressHUD.dismiss()
                Toast.makeText(this@EmailVerificationScreen, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<GeneralRes>, response: Response<GeneralRes>) {
                progressHUD.dismiss()

                Log.d("responseHere", Gson().toJson(response))
                if (response.isSuccessful) {
                    resetCounter()
                    Toast.makeText(
                        this@EmailVerificationScreen,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()


                } else {
                    progressHUD.dismiss()
                    val jsonObject = JSONObject(response.errorBody()?.string())
                    Toast.makeText(
                        this@EmailVerificationScreen,
                        jsonObject.get("message").toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }


    private fun resetCounter() {
        tv_resend.isClickable = false
        counter = 60
        count_tv.setText("60")
        timer.start();

    }

    override fun onDestroy() {
        if (timerCheck == true) {
            timer.cancel()
        }


        super.onDestroy()


    }
}