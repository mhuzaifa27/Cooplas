package com.example.cooplas.interfaces

import com.example.cooplas.models.GeneralRes
import com.example.cooplas.models.SignUpSigninRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Cooplas {

    // constants used in "companion Object"
    companion object {
        const val BASE_URL = "https://cooplas.jobesk.com"
    }

    @FormUrlEncoded
    @POST("/api/signup")
    fun register(@Field("first_name") first_name:String,
                 @Field("last_name") last_name:String,
                 @Field("phone") phone:String,
                 @Field("email") email:String,
                 @Field("password") password:String,
                 @Field("role") role:String):Call<SignUpSigninRes>

    @FormUrlEncoded
    @POST("/api/signin")
    fun login(@Field("email") email:String,
              @Field("password") password:String ):Call<SignUpSigninRes>

    @GET("/api/verify/phone")
    fun verify_otp(@Header("Authorization") Authorization: String,
                   @Query("code") code:String):Call<GeneralRes>

    @GET("/api/resend/phone-verification-code")
    fun resent_otp(@Header("Authorization") Authorization: String):Call<GeneralRes>

    @Multipart
    @POST("/api/complete/signup")
    fun complete_sign_up(@Header("Authorization") Authorization:String,
                         @Part profile_image: MultipartBody.Part,
                         @Part("gender") gender: RequestBody,
                         @Part("username") username: RequestBody,
                         @Part("dob") dob: RequestBody,
                         @Part("relationship_status") relationship_status: RequestBody ):Call<GeneralRes>

//
//    @FormUrlEncoded
//    @POST("/api/forgot-password")
//    fun forgot_password(@Field("email") email:String ):Call<GeneralRes>

}