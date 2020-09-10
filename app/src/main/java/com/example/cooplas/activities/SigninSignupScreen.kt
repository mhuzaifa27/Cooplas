package com.example.cooplas.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.cooplas.R
import com.example.cooplas.models.SignUpSigninRes
import com.example.cooplas.utils.AppManager
import com.jobesk.gong.utils.checkConnection
import com.jobesk.gong.utils.isValidEmail
import com.jobesk.gong.utils.saveAccessToken
import com.jobesk.gong.utils.saveUserEmailAndPass
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_signin_signup_screen.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninSignupScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_signup_screen)

        tv_sign_in.setOnClickListener {
            tv_sign_up.alpha=.3f
            tv_sign_in.alpha=1f
            ll_sign_in.visibility= View.VISIBLE
            tv_term_condition.visibility= View.VISIBLE
            sv_signup.visibility=View.GONE
        }
        tv_sign_up.setOnClickListener {
            tv_sign_up.alpha=1f
            tv_sign_in.alpha=.3f
            ll_sign_in.visibility= View.GONE
            tv_term_condition.visibility= View.GONE
            sv_signup.visibility=View.VISIBLE
        }
        rl_signin.setOnClickListener {
            validateDataAndLogin()
        }
        rl_signup.setOnClickListener {
            validateDataAndSingUp()
        }

    }

    private fun validateDataAndLogin(){

        when{
            et_email.text.trim().isEmpty()-> run {
                et_email.error = "Enter your Email"
                return }

            !isValidEmail(et_email?.text?.trim().toString()) -> run {
                et_email?.requestFocus()
                et_email.error = "Invalid Email!"
                return }

            else -> ""
        }

        when{
            et_password.text?.trim()?.isEmpty()!! ->run {
                et_password.error = "Enter your Password"
                return }

            else -> ""
        }

        when (checkConnection(this)) {
            true->{ Login(et_email.text.trim().toString(), et_password.text?.trim().toString())}
            else->{ Toast.makeText(this,"No Internet Connection", Toast.LENGTH_SHORT).show()}
        }
    }

    private fun Login(email: String, password: String) {
        val progressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.login(email,password).enqueue(object : Callback<SignUpSigninRes> {

            override fun onFailure(call: Call<SignUpSigninRes>, t: Throwable) {
                progressHUD.dismiss()
                Toast.makeText(this@SigninSignupScreen,t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<SignUpSigninRes>, response: Response<SignUpSigninRes>) {
                progressHUD.dismiss()
                if (response.isSuccessful){
                    Toast.makeText(this@SigninSignupScreen,response.body()?.message, Toast.LENGTH_SHORT).show()
                    saveUserEmailAndPass(this@SigninSignupScreen, email, password)
                    saveAccessToken(this@SigninSignupScreen,response.body()?.user?.auth_token?:"")
//                    when(response.body()?.status){
//                            200-> {
                            startActivity(
                                Intent(this@SigninSignupScreen, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//                        }
//                    }
                }
                else {
                    val jsonObject= JSONObject(response.errorBody()?.string())
                    Toast.makeText(this@SigninSignupScreen,jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun validateDataAndSingUp(){

        when{
            et_phone_number.text?.trim()?.isEmpty()!! -> run {
                et_phone_number.error = "Enter Phone Number"
                return }
            else -> ""
        }

        when{
            et_signup_email.text.trim().isEmpty()-> run {
                et_signup_email.error = "Enter your Email"
                return }

            !isValidEmail(et_signup_email?.text?.trim().toString()) -> run {
                et_signup_email?.requestFocus()
                et_signup_email.error = "Invalid Email!"
                return }
            else -> ""
        }

        when{
            et_signup_password.text?.trim()?.isEmpty()!! ->run {
                et_signup_password.error = "Enter your Password"
                return }

            et_signup_password.text?.trim()?.length!! < 6 -> run {
                et_signup_password.error = "Password must be greater than Five"
                return }

            et_confirm_password.text?.trim()?.isEmpty()!! ->run {
                et_confirm_password.error = "Confirm your Password"
                return }

            et_confirm_password.text?.trim()?.length!! < 6 -> run {
                et_confirm_password.error = "Password must be greater than Five"
                return }

            else -> ""
        }

        when(et_signup_password.text?.trim()?.toString()==et_confirm_password.text?.trim()?.toString()){
            false -> {
                Toast.makeText(this, "Password Not Matched", Toast.LENGTH_SHORT).show()
                return
            }
        }

        when (checkConnection(this)) {
            true->{ sign_up(et_first_name.text.toString(),et_last_name.text.toString(),(ccp.selectedCountryCode+et_phone_number.text.toString()).trim(),et_signup_email.text.trim().toString(), et_signup_password.text?.trim().toString(),"user")}
            else->{ Toast.makeText(this,"No Internet Connection", Toast.LENGTH_SHORT).show()}
        }
    }

    private fun sign_up(first_name: String, last_name: String, phone: String, email: String, password: String, role: String) {
        val progressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.register(first_name, last_name, phone, email ,password,role).enqueue(object : Callback<SignUpSigninRes> {
            override fun onFailure(call: Call<SignUpSigninRes>, t: Throwable) {
                progressHUD.dismiss()
                Toast.makeText(this@SigninSignupScreen,t.message, Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<SignUpSigninRes>, response: Response<SignUpSigninRes>) {
                progressHUD.dismiss()
                if (response.isSuccessful){
                    Toast.makeText(this@SigninSignupScreen,response.body()?.message,Toast.LENGTH_SHORT).show()
                    saveUserEmailAndPass(this@SigninSignupScreen,email,password)
                    saveAccessToken(this@SigninSignupScreen,response.body()?.user?.auth_token?:"")
//                    when(response.body()?.status){
//                        201-> {
                            startActivity(Intent(this@SigninSignupScreen, PhoneVerificatoinScreen::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//                        }
//                    }
                }
                else {
                    val jsonObject= JSONObject(response.errorBody()?.string())
                    Toast.makeText(this@SigninSignupScreen,jsonObject.get("message").toString(),Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}