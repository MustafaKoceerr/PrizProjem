package com.mustafa.prizprojem.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
@Serializable
data class TransTime(
    @SerializedName("s_date") val rule: String,
    @SerializedName("e_date") val value: String
)
