package com.mustafa.prizprojem.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {
    private val BASE_URL = "https://api.fecriati.com.tr"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService: RetrofitService = retrofit.create(RetrofitService::class.java)



    private val BASE_URL2 = "https://raw.githubusercontent.com"

    private val retrofit2: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL2)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiServiceGitHub: RetrofitService = retrofit2.create(RetrofitService::class.java)


}