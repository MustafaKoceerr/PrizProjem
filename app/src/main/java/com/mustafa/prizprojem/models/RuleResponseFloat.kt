package com.mustafa.prizprojem.models

import com.google.gson.annotations.SerializedName

data class RuleResponseFloat(
    @SerializedName("msg") val msg: String,
    @SerializedName("rule") val rule: Int,
    @SerializedName("value") val value: Float,
)
