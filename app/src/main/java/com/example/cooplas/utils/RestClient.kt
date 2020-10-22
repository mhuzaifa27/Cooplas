package com.example.cooplas.utils

import android.content.Context
import com.example.cooplas.interfaces.Cooplas
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class  RestClient{
    var cooplas: Cooplas
    var context: Context?= null

    init{

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                    .header("Authorization", AppConstants.ACCESS_TOKEN).method(original.method(), original.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()


        //        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //
        //        OkHttpClient okHttpClient = builder.build();


        val restAdapter = Retrofit.Builder().baseUrl(Cooplas.BASE_URL).addConverterFactory(GsonConverterFactory.create())
                //                .client(httpClient)
                .build()
        cooplas = restAdapter.create(Cooplas::class.java)

    }
}