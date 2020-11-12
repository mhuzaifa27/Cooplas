package com.example.cooplas.signup_screens

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cooplas.R
import com.example.cooplas.events.VerifyEvent
import com.example.cooplas.models.GeneralRes
import com.example.cooplas.utils.AppManager
import com.jobesk.gong.utils.getAccessToken
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_phone_verificatoin_screen.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EmailInAppScreeenVerification : AppCompatActivity() {


    var counter = 60;
    lateinit var timer: CountDownTimer;
    var timerCheck: Boolean = false
    var resendCheck: Boolean = false


    override fun onDestroy() {
        if (timerCheck == true) {
            timer.cancel()
        }


        super.onDestroy()


    }

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

        et_four.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                et_five.requestFocus()
            }
        })
        et_five.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                et_six.requestFocus()
            }
        })

        tv_resend.setOnClickListener {
            resend_code()
        }
        rl_next.setOnClickListener {
            var inputString =
                "${et_one.text}${et_two.text}${et_three.text}${et_four.text}${et_five.text}${et_six.text}";
            verify_otp(inputString)
        }
        resend_code()
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
        AppManager.getInstance().restClient.cooplas.verifyEmail(
            "Bearer " + getAccessToken(this),
            code
        ).enqueue(object : Callback<GeneralRes> {
            override fun onFailure(call: Call<GeneralRes>, t: Throwable) {
                progressHUD.dismiss()
                Toast.makeText(this@EmailInAppScreeenVerification, t.message, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(call: Call<GeneralRes>, response: Response<GeneralRes>) {
                progressHUD.dismiss()
                Log.d("PhoneVerificatoinScreen", "onResponse: " + response.body()?.toString())
                if (response.isSuccessful) {


                    var message = response.body()?.message

                    Toast.makeText(
                        this@EmailInAppScreeenVerification,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()

                    if (message?.contains("Email verification code is incorrect")!!) {

                        return

                    }

                    EventBus.getDefault().post(VerifyEvent())

                    finish()

                } else {
                    val jsonObject = JSONObject(response.errorBody()?.string())
                    Toast.makeText(
                        this@EmailInAppScreeenVerification,
                        jsonObject.get("message").toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun resend_code() {

        val progressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.verfyEmailMain(
            "Bearer " + getAccessToken(this),

            ).enqueue(object : Callback<GeneralRes> {
            override fun onFailure(call: Call<GeneralRes>, t: Throwable) {
                progressHUD.dismiss()
                Toast.makeText(this@EmailInAppScreeenVerification, t.message, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(call: Call<GeneralRes>, response: Response<GeneralRes>) {
                progressHUD.dismiss()
                Log.d("PhoneVerificatoinScreen", "onResponse: " + response.body()?.toString())
                if (response.isSuccessful) {


                    var message = response.body()?.message

                    Toast.makeText(
                        this@EmailInAppScreeenVerification,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()

                    if (message?.contains("Email verification code is incorrect")!!) {

                        return

                    }

                    if (resendCheck == false) {
                        resendCheck = true

                    } else {
                        resetCounter();
                    }


                } else {
                    val jsonObject = JSONObject(response.errorBody()?.string())
                    Toast.makeText(
                        this@EmailInAppScreeenVerification,
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

}