package com.example.cooplas.utils.retrofitJava;


import com.example.cooplas.models.GeneralRes;
import com.example.cooplas.models.homeFragmentModel.HomeModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @FormUrlEncoded
    @POST("api/change/password")
    Call<GeneralRes> changeForgetPassword(@Header("Authorization") String token, @Field("password") String offset);


    @GET("api/user/wall")
    Call<HomeModel> getHomeFeed(@Header("Authorization") String token, @Query("posts_offset") String offset);

}
