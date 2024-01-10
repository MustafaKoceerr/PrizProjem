package com.mustafa.prizprojem.models

import com.google.gson.annotations.SerializedName

data class RuleInfo(
    @SerializedName("rule") val rule: Int,
    @SerializedName("value") val value: Any,
)
