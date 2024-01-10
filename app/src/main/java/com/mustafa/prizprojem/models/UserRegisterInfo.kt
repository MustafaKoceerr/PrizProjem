package com.mustafa.prizprojem.models

import com.google.gson.annotations.SerializedName

data class UserRegisterInfo(
    @SerializedName("username") var username: String,
    @SerializedName("password") var password: String,
    @SerializedName("city") var city: String,
    @SerializedName("province") var province: String
)