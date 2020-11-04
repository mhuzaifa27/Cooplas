package com.example.cooplas.signup_screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.cooplas.activities.EnterNewPassword
import com.example.cooplas.R

import com.example.cooplas.utils.AppManager
import com.example.cooplas.utils.callbacks.ForgetPasResponse
import com.jobesk.gong.utils.saveAccessToken
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_phone_verificatoin_screen.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailVerificationScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verificatoin_screen)




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


}