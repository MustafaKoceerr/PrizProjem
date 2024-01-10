package com.mustafa.prizprojem.models

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("accessToken") val accessToken: String
)