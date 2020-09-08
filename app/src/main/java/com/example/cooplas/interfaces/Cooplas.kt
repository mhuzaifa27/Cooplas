package com.jobesk.gong.interfaces

import com.jobesk.gong.models.GeneralRes
import com.jobesk.gong.models.SignUpRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Cooplas {

    // constants used in "companion Object"
    companion object {
        const val BASE_URL = "https://gong.jobesk.com"
    }

    @FormUrlEncoded
    @POST("/api/register")
    fun register(@Field("email") email:String,
                 @Field("phone") phone:String,
                 @Field("password") password:String,
                 @Field("in_app_notifications") in_app_notifications:Boolean,
                 @Field("text_notifications") text_notifications:Boolean):Call<SignUpRes>

    @FormUrlEncoded
    @POST("/api/verify-number")
    fun verify_otp(@Header("Authorization") Authorization: String,
                   @Field("code") code:String):Call<GeneralRes>

    @GET("/api/resend-otp")
    fun resent_otp(@Header("Authorization") Authorization: String):Call<GeneralRes>

    @FormUrlEncoded
    @POST("/api/update/role")
    fun update_role(@Header("Authorization") Authorization: String,
                   @Field("role") role:String):Call<GeneralRes>

    @Multipart
    @POST("/api/update/user-details")
    fun complete_sign_up(@Header("Authorization") Authorization:String,
                         @Part("first_name") first_name: RequestBody,
                         @Part("last_name") last_name: RequestBody,
                         @Part("username") username: RequestBody,
                         @Part("date_of_birth") date_of_birth: RequestBody,
                         @Part("gender") gender: RequestBody,
                         @Part("region_id") region_id: RequestBody,
                         @Part("country_id") country_id: RequestBody,
                         @Part profile_image: MultipartBody.Part ):Call<GeneralRes>

    @FormUrlEncoded
    @POST("/api/login")
    fun login(@Field("email") email:String,
              @Field("password") password:String ):Call<SignUpRes>

    @FormUrlEncoded
    @POST("/api/forgot-password")
    fun forgot_password(@Field("email") email:String ):Call<GeneralRes>

}