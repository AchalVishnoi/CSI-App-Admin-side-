package com.example.csiappcompose.Backend

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {


     private const val BASE_URL="https://csi-backend-wvn0.onrender.com/"

    val apiService: ApiService by lazy{
        Retrofit.
        Builder().
        baseUrl(BASE_URL).
        addConverterFactory(GsonConverterFactory.create())
        .build().
        create(ApiService::class.java)
    }




}