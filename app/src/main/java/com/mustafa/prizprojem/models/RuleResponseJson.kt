package com.mustafa.prizprojem.models

import com.google.gson.annotations.SerializedName

data class RuleResponseJson(
    @SerializedName("msg") val msg: String,
    @SerializedName("rule") val rule: Int,
    @SerializedName("value") val value: Map<String, String>
)
