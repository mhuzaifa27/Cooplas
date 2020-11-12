package com.example.cooplas.interfaces

import com.example.cooplas.models.*
import com.example.cooplas.utils.callbacks.ForgetPasResponse
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
    fun register(
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String
    ): Call<SignUpSigninRes>

    @FormUrlEncoded
    @POST("/api/signin")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignUpSigninRes>

    @GET("/api/verify/phone")
    fun verify_otp(
        @Header("Authorization") Authorization: String,
        @Query("code") code: String
    ): Call<GeneralRes>


    @GET("/api/verify/password-reset-code?code=")
    fun verify_forgetPassword(
        @Query("code") code: String
    ): Call<ForgetPasResponse>

    @GET("/api/resend/phone-verification-code")
    fun resent_otp(@Header("Authorization") Authorization: String): Call<GeneralRes>

    @Multipart
    @POST("/api/complete/signup")
    fun complete_sign_up(
        @Header("Authorization") Authorization: String,
        @Part profile_image: MultipartBody.Part,
        @Part("gender") gender: RequestBody,
        @Part("username") username: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("relationship_status") relationship_status: RequestBody
    ): Call<GeneralRes>

    @GET("/api/send/password-reset-code")
    fun forgot_password(@Query("email") email: String): Call<GeneralRes>

    @GET("/api/post/likes")
    fun post_likes(
        @Header("Authorization") Authorization: String,
        @Query("post_id") post_id: String
    ): Call<PostlikeRes>

    @GET
    fun follow_user(
        @Url url: String,
        @Header("Authorization") Authorization: String
    ): Call<GeneralRes>

    @GET("/api/user/followers")
    fun get_followers(
        @Header("Authorization") Authorization: String
    ): Call<FollowersRes>

    @GET("/api/user/following")
    fun get_following(
        @Header("Authorization") Authorization: String
    ): Call<FollowersRes>

    @GET("/api/user/wall")
    fun get_user_profile(@Header("Authorization") Authorization: String): Call<ProfileRes>

    @DELETE
    fun delete_post(
        @Url url: String,
        @Header("Authorization") Authorization: String
    ): Call<GeneralRes>

    @FormUrlEncoded
    @POST("/api/add/comment")
    fun add_comment(
        @Header("Authorization") Authorization: String,
        @Field("post_id") post_id: String,
        @Field("body") body: String
    ): Call<AddCommentRes>

    @GET("/api/post/comments")
    fun get_comment(
        @Header("Authorization") Authorization: String,
        @Query("post_id") post_id: String
    ): Call<CommentlistRes>

    @Multipart
    @POST("/api/update/profile")
    fun update_profile(
        @Header("Authorization") Authorization: String,
        @Part profile_image: MultipartBody.Part?,
        @Part("gender") gender: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("username") username: RequestBody
    ): Call<GeneralRes>


    @GET("api/verify/email")
    fun verifyEmail(
        @Header("Authorization") Authorization: String,
        @Query("code") code: String,

        ): Call<GeneralRes>

    @GET("api/resend/email-verification-code")
    fun verfyEmailMain(
        @Header("Authorization") Authorization: String,
              ): Call<GeneralRes>

}