package com.example.cooplas.signup_screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.cooplas.R
import com.example.cooplas.models.GeneralRes
import com.example.cooplas.utils.AppManager
import com.jobesk.gong.utils.clearSharedPreference
import com.jobesk.gong.utils.getAccessToken
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_phone_verificatoin_screen.*
import kotlinx.android.synthetic.main.fragment_discover.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhoneVerificatoinScreen : AppCompatActivity() {

    var counter = 60;
    lateinit var timer: CountDownTimer;
    var timerCheck: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verificatoin_screen)


        var intent = intent
        if (intent != null) {

            var value = intent.getStringExtra("fromLogin");
            if (value.equals("1")) {

                resent_otp();

            }


        }

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
            resent_otp()
        }

        rl_next.setOnClickListener {

            var inputString =
                "${et_one.text}${et_two.text}${et_three.text}${et_four.text}${et_five.text}${et_six.text}";

            verify_otp(inputString)
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

    override fun onDestroy() {
        if (timerCheck == true) {
            timer.cancel()
        }


        super.onDestroy()


    }

    private fun verify_otp(code: String) {
        val progressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.verify_otp(
            "Bearer " + getAccessToken(this),
            code
        ).enqueue(object : Callback<GeneralRes> {
            override fun onFailure(call: Call<GeneralRes>, t: Throwable) {
                progressHUD.dismiss()
                Toast.makeText(this@PhoneVerificatoinScreen, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<GeneralRes>, response: Response<GeneralRes>) {
                progressHUD.dismiss()
                Log.d("PhoneVerificatoinScreen", "onResponse: " + response.body()?.toString())
                if (response.isSuccessful) {


                    var message = response.body()?.message

                    Toast.makeText(
                        this@PhoneVerificatoinScreen,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()

                    if (message?.contains("Phone verification code is incorrect")!!) {

                        return

                    }

                    startActivity(
                        Intent(this@PhoneVerificatoinScreen, CompleteSignup::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        )
                    )
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                } else {
                    val jsonObject = JSONObject(response.errorBody()?.string())
                    progressHUD.dismiss()
                    Toast.makeText(
                        this@PhoneVerificatoinScreen,
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

    private fun resent_otp() {
        val progressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.resent_otp("Bearer " + getAccessToken(this))
            .enqueue(object : Callback<GeneralRes> {
                override fun onFailure(call: Call<GeneralRes>, t: Throwable) {
                    progressHUD.dismiss()
                    Toast.makeText(this@PhoneVerificatoinScreen, t.message, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(call: Call<GeneralRes>, response: Response<GeneralRes>) {
                    progressHUD.dismiss()

                    Log.d("PhoneVerificatoinScreen", "onResponse: " + response.body()?.toString())

                    if (response.isSuccessful) {

                        resetCounter();
                        Toast.makeText(
                            this@PhoneVerificatoinScreen,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
//                    when(response.body()?.status){
//                        200-> {
//                            tv_resend_otp.visibility= View.GONE
//                            timerobject.start()
//                            resentOtpDialog()
//                        }
//                    }
                    } else {
                        val jsonObject = JSONObject(response.errorBody()?.string())
                        Toast.makeText(
                            this@PhoneVerificatoinScreen,
                            jsonObject.get("message").toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

}