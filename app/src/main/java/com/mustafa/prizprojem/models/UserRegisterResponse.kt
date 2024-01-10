package com.mustafa.prizprojem.models

import com.google.gson.annotations.SerializedName

data class UserRegisterResponse(
    @SerializedName("username") var username: String,
    @SerializedName("password") var password: String,
    @SerializedName("msg") var msg: String
)
