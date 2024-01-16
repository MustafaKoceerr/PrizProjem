package com.mustafa.prizprojem.models

import com.google.gson.annotations.SerializedName

data class RuleInfoJson(
    @SerializedName("rule") val rule: Int,
    @SerializedName("value") val value: Map<String, String>
)
