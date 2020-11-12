package com.example.cooplas.utils.retrofitJava;

import android.content.Context;

import com.example.cooplas.utils.NetworkConnectionInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;
    private static Retrofit retrofit2 = null;
    public static String BASEURL = "https://cooplas.jobesk.com/";
    public static String GOOGLE_DISTANCE_API = "https://maps.googleapis.com/maps/";

    public static Retrofit getClient(Context context) {

        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .addInterceptor(new NetworkConnectionInterceptor(context)).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        } else {
            return retrofit;
        }
        return retrofit;
    }

    public static Retrofit getClientForGoogleApi(Context context) {
        if (retrofit2 == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .addInterceptor(new NetworkConnectionInterceptor(context)).build();

            retrofit2 = new Retrofit.Builder()
                    .baseUrl(GOOGLE_DISTANCE_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        } else {
            return retrofit2;
        }
        return retrofit2;
    }
}