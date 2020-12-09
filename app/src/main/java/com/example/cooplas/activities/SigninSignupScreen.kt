package com.example.cooplas.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.cooplas.AgoraClasses.ChatManager
import com.example.cooplas.Firebase.AppState
import com.example.cooplas.Firebase.ChangeEventListener
import com.example.cooplas.Firebase.Services.UserService
import com.example.cooplas.R
import com.example.cooplas.models.SignUpSigninRes
import com.example.cooplas.signup_screens.CompleteSignup
import com.example.cooplas.signup_screens.PhoneVerificatoinScreen
import com.example.cooplas.utils.AppManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import com.jobesk.gong.utils.*
import com.kaopiz.kprogresshud.KProgressHUD
import io.agora.rtm.ErrorInfo
import io.agora.rtm.ResultCallback
import io.agora.rtm.RtmClient
import kotlinx.android.synthetic.main.activity_signin_signup_screen.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninSignupScreen : AppCompatActivity() {

    private var mRtmClient: RtmClient? = null
    private var mChatManager: ChatManager? = null
    private val mIsInChat = false
    private var mAuth: FirebaseAuth? = null
    private var firstName: String? = null
    private var lastName: String? = null
    private var email: String? = null
    private var id: String? = null
    private var userService: UserService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_signup_screen)

        mAuth = FirebaseAuth.getInstance()
        userService = UserService()
        userService?.setOnChangedListener(object : ChangeEventListener {
            override fun onChildChanged(
                type: ChangeEventListener.EventType,
                index: Int,
                oldIndex: Int
            ) {
            }

            override fun onDataChanged() {}
            override fun onCancelled(error: DatabaseError) {}
        })
//        mChatManager= App.instance.getChatManager()

        tv_sign_in.setOnClickListener {
            tv_sign_up.alpha = .3f
            tv_sign_in.alpha = 1f
            ll_sign_in.visibility = View.VISIBLE
            tv_term_condition.visibility = View.VISIBLE
            sv_signup.visibility = View.GONE
        }
        tv_sign_up.setOnClickListener {
            tv_sign_up.alpha = 1f
            tv_sign_in.alpha = .3f
            ll_sign_in.visibility = View.GONE
            tv_term_condition.visibility = View.GONE
            sv_signup.visibility = View.VISIBLE
        }
        rl_signin.setOnClickListener {
            validateDataAndLogin()
        }
        rl_signup.setOnClickListener {
            validateDataAndSingUp()
        }
        tv_forgot.setOnClickListener {
            startActivity(Intent(this@SigninSignupScreen, ForgotPassword::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun validateDataAndLogin() {
        when {
            et_email.text.trim().isEmpty() -> run {
                et_email.error = "Enter your Email"
                return
            }

            !isValidEmail(et_email?.text?.trim().toString()) -> run {
                et_email?.requestFocus()
                et_email.error = "Invalid Email!"
                return
            }

            else -> ""
        }

        when {
            et_password.text?.trim()?.isEmpty()!! -> run {
                et_password.error = "Enter your Password"
                return
            }

            else -> ""
        }

        when (checkConnection(this)) {
            true -> {
                Login(et_email.text.trim().toString(), et_password.text?.trim().toString())
            }
            else -> {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun Login(email: String, password: String) {
        val progressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.login(email, password)
            .enqueue(object : Callback<SignUpSigninRes> {
                override fun onFailure(call: Call<SignUpSigninRes>, t: Throwable) {
                    progressHUD.dismiss()
                    Toast.makeText(this@SigninSignupScreen, t.message, Toast.LENGTH_SHORT).show()
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<SignUpSigninRes>,
                    response: Response<SignUpSigninRes>
                ) {

                    progressHUD.dismiss()
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@SigninSignupScreen,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        saveUserEmailAndPass(this@SigninSignupScreen, email, password)
                        saveAccessToken(
                            this@SigninSignupScreen,
                            response.body()?.user?.auth_token ?: ""
                        )
                        var userName =
                            response.body()?.user?.first_name + " " + response.body()?.user?.last_name;
                        var roleVal = response.body()!!.user.role
                        var image = response.body()!!.user.profile_pic
                        var idUser = response.body()!!.user.id
                        saveUserDetails(
                            applicationContext,
                            idUser.toString(),
                            userName,
                            roleVal

                        )
                        loginFirebase(email, password, progressHUD, response.body()!!)


                        /*var phoneVerified = response.body()?.user?.phone_verified
                        if (phoneVerified == false) {
                            var intentPhone =
                                Intent(this@SigninSignupScreen, PhoneVerificatoinScreen::class.java)
                            intentPhone.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            intentPhone.putExtra("fromLogin", "1")
                            startActivity(intentPhone)
                            return
                        }
                        var emailVerified = response.body()?.user?.username
                        if (emailVerified == null) {
                            var intent = Intent(this@SigninSignupScreen, CompleteSignup::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            return
                        }
                        var intentMain = Intent(this@SigninSignupScreen, MainActivity::class.java)
                        intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        saveLoggedIn(applicationContext, "1")
                        startActivity(intentMain)*/
                        //LoginInAgora(idUser.toString())

                    } else {
                        val jsonObject = JSONObject(response.errorBody()?.string())
                        Toast.makeText(
                            this@SigninSignupScreen,
                            jsonObject.get("message").toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    private fun loginFirebase(
        email: String,
        password: String,
        progressHUD: KProgressHUD,
        response: SignUpSigninRes
    ) {
        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this, fun(task: Task<AuthResult>) {
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success")
                    AppState.currentFireUser = mAuth!!.currentUser
                    AppState.currentBpackCustomer =
                        userService?.getUserById(AppState.currentFireUser.uid)

                    var phoneVerified = response?.user?.phone_verified
                        if (phoneVerified == false) {
                            var intentPhone =
                                Intent(this@SigninSignupScreen, PhoneVerificatoinScreen::class.java)
                            intentPhone.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            intentPhone.putExtra("fromLogin", "1")
                            startActivity(intentPhone)
                            return
                        }
                        var emailVerified = response?.user?.username
                        if (emailVerified == null) {
                            var intent = Intent(this@SigninSignupScreen, CompleteSignup::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            return
                        }
                        var intentMain = Intent(this@SigninSignupScreen, MainActivity::class.java)
                        intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        saveLoggedIn(applicationContext, "1")
                        startActivity(intentMain)
                } else {
                    Log.d("TAG", "signInWithEmail:failure", task.exception)
                }
            })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun LoginInAgora(mUserId: String) {
        /*  val rtmTokenBuilder=RtmTokenBuilder()
          val token:String =rtmTokenBuilder.buildToken("08cd21b565a04f78b133cf68a1389d2d"
          ,"9cc458ff6c4349ce8919edf4d7379523"
          ,"2882341273L",RtmTokenBuilder.Role.Rtm_User,0)*/

        mRtmClient = mChatManager?.getRtmClient()
        //Log.i("TANNNNNNG", "login "+token)
        mRtmClient?.login(null, mUserId, object : ResultCallback<Void> {
            override fun onSuccess(responseInfo: Void) {
                Log.i("TANNNNNNG", "login success")
                runOnUiThread {
                    var intentMain = Intent(this@SigninSignupScreen, MainActivity::class.java)
                    intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    saveLoggedIn(applicationContext, "1")
                    startActivity(intentMain)
                }
            }

            override fun onFailure(errorInfo: ErrorInfo) {
                Log.i(
                    "TANNNNNNG",
                    "login failed: " + errorInfo.errorDescription
                )
            }
        })
    }

    private fun validateDataAndSingUp() {

        when {
            et_phone_number.text?.trim()?.isEmpty()!! -> run {
                et_phone_number.error = "Enter Phone Number"
                return
            }
            else -> ""
        }

        when {
            et_signup_email.text.trim().isEmpty() -> run {
                et_signup_email.error = "Enter your Email"
                return
            }

            !isValidEmail(et_signup_email?.text?.trim().toString()) -> run {
                et_signup_email?.requestFocus()
                et_signup_email.error = "Invalid Email!"
                return
            }
            else -> ""
        }

        when {
            et_signup_password.text?.trim()?.isEmpty()!! -> run {
                et_signup_password.error = "Enter your Password"
                return
            }

            et_signup_password.text?.trim()?.length!! < 6 -> run {
                et_signup_password.error = "Password must be greater than Five"
                return
            }

            et_confirm_password.text?.trim()?.isEmpty()!! -> run {
                et_confirm_password.error = "Confirm your Password"
                return
            }

            et_confirm_password.text?.trim()?.length!! < 6 -> run {
                et_confirm_password.error = "Password must be greater than Five"
                return
            }

            else -> ""
        }

        when (et_signup_password.text?.trim()?.toString() == et_confirm_password.text?.trim()
            ?.toString()) {
            false -> {
                Toast.makeText(this, "Password Not Matched", Toast.LENGTH_SHORT).show()
                return
            }
        }

        when (checkConnection(this)) {
            true -> {
                sign_up(
                    et_first_name.text.toString(),
                    et_last_name.text.toString(),
                    (ccp.selectedCountryCode + et_phone_number.text.toString()).trim(),
                    et_signup_email.text.trim().toString(),
                    et_signup_password.text?.trim().toString(),
                    "user"
                )
            }
            else -> {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sign_up(
        first_name: String,
        last_name: String,
        phone: String,
        email_: String,
        password: String,
        role: String
    ) {
        val progressHUD = KProgressHUD.create(this).show()
        AppManager.getInstance().restClient.cooplas.register(
            first_name,
            last_name,
            phone,
            email_,
            password,
            role
        ).enqueue(object : Callback<SignUpSigninRes> {
            override fun onFailure(call: Call<SignUpSigninRes>, t: Throwable) {
                progressHUD.dismiss()
                Toast.makeText(this@SigninSignupScreen, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<SignUpSigninRes>,
                response: Response<SignUpSigninRes>
            ) {
                progressHUD.dismiss()
                if (response.isSuccessful) {

                    var message: String? = response.body()?.message
                    Toast.makeText(this@SigninSignupScreen, message, Toast.LENGTH_SHORT).show()
                    val stringResponse = response.body()
                    Log.d("signUP", "onResponse: " + response.body()?.toString())
                    if (message?.contains("The email has already been taken")!!) {
                        progressHUD.dismiss()
                        return
                    }
                    if (message.contains("The phone number belongs to an existing account")) {
                        progressHUD.dismiss()
                        return
                    }
                    if (message.contains("Could not send phone verification code. Try again")) {
                        progressHUD.dismiss()
                        Toast.makeText(
                            this@SigninSignupScreen,
                            "Could not send phone verification code. Try again",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    saveUserEmailAndPass(this@SigninSignupScreen, email_, password)
                    saveAccessToken(
                        this@SigninSignupScreen, response.body()?.user?.auth_token ?: ""
                    )

                    var userName =
                        response.body()?.user?.first_name + " " + response.body()?.user?.last_name;
                    var roleVal = response.body()!!.user.role
//                    var image = response.body()!!.user.profile_pic
                    var idUser = response.body()!!.user.id
                    saveUserDetails(
                        applicationContext,
                        idUser.toString(),
                        userName, roleVal
                    )
                    firstName = first_name
                    lastName = last_name
                    email = email_
                    id = idUser.toString()
                    registerUser(email_, password, progressHUD);
//                    when(response.body()?.status){
//                        201-> {
//                        }
//                    }
                } else {
                    val jsonObject = JSONObject(response.errorBody()?.string())
                    Toast.makeText(
                        this@SigninSignupScreen,
                        jsonObject.get("message").toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun registerUser(
        email: String,
        password: String,
        progressHUD: KProgressHUD
    ) {

        this.mAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    progressHUD.dismiss()
                    AppState.currentFireUser = mAuth?.getCurrentUser()
                    createUser(AppState.currentFireUser, progressHUD)
                } else {
                    Log.d("messagee", "onComplete: " + task.result)
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        this@SigninSignupScreen, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressHUD.dismiss()
                }
            }
        /*mAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this@SigninSignupScreen,
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {

                    } else {
                        Log.d("messagee", "onComplete: " + task.result)
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this@SigninSignupScreen, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressHUD.dismiss()
                    }
                })*/
    }

    private fun createUser(
        currentUser: FirebaseUser,
        progressHUD: KProgressHUD
    ) {
        if (currentUser.uid != null) {
            userService?.registerUser(
                email,
                firstName + " " + lastName,
                currentUser.uid,
                DatabaseReference.CompletionListener { databaseError, databaseReference ->
                    if (databaseError == null) {
                        progressHUD.dismiss()
                        FirebaseMessaging.getInstance()
                            .unsubscribeFromTopic("/topics/order_" + AppState.currentFireUser.uid)
                        FirebaseMessaging.getInstance()
                            .subscribeToTopic("/topics/order_" + AppState.currentFireUser.uid)
//
                        startActivity(
                            Intent(
                                this@SigninSignupScreen,
                                PhoneVerificatoinScreen::class.java
                            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                    } else {
                        progressHUD.dismiss()
                        Log.d(
                            "messagee",
                            "onComplete: " + databaseError.message
                        )
                        Toast.makeText(
                            this@SigninSignupScreen,
                            "SigUp failed - database error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            Toast.makeText(
                this@SigninSignupScreen,
                "SigUp failed, email empty. Please try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}