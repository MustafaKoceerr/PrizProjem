package com.mustafa.prizprojem.models

import com.google.gson.annotations.SerializedName

data class DenemeData(
    val verim : List<mod1>,
    )

data class mod1(
    @SerializedName("il_adi") val il_adi : String,
    @SerializedName("ilceler") var ilceler: List<mod2>

)

data class mod2(
    @SerializedName("ilce_adi") val ilce_adi : String

)

data class myExampleDataClass(
    val myList: List<myExampleDataClass2>
)
data class myExampleDataClass2(
    @SerializedName("il_adi") val il_adi : String
)