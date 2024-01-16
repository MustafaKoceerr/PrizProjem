package com.mustafa.prizprojem.models

import com.google.gson.annotations.SerializedName

data class RuleInfoFloat(
    @SerializedName("rule") val rule: Int,
    @SerializedName("value") val value: Float,
)
