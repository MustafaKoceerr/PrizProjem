package com.mustafa.prizprojem.modelView

import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.mustafa.prizprojem.LoginEkraniFragmentDirections
import com.mustafa.prizprojem.models.UserInfo
import com.mustafa.prizprojem.models.UserResponse
import com.mustafa.prizprojem.services.RetrofitService
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginModelView : ViewModel() {
    private val BASE_URL = "https://api.fecriati.com.tr"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService: RetrofitService = retrofit.create(RetrofitService::class.java)


}