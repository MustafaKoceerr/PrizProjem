package com.mustafa.prizprojem.services

import com.mustafa.prizprojem.models.DeleteInfo
import com.mustafa.prizprojem.models.RuleInfoFloat
import com.mustafa.prizprojem.models.RuleInfoJson
import com.mustafa.prizprojem.models.RuleResponseFloat
import com.mustafa.prizprojem.models.RuleResponseJson
import com.mustafa.prizprojem.models.UserInfo
import com.mustafa.prizprojem.models.UserRegisterInfo
import com.mustafa.prizprojem.models.UserRegisterResponse
import com.mustafa.prizprojem.models.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RetrofitService {


    @POST("/login")
    fun postLogin(@Body userInfo: UserInfo): retrofit2.Call<UserResponse>


    /*
    BODY json: {username,password,city,province}

RETURNS:
{
    "username": "mert15",
    "password": "gucluturkiye2023",
    "msg": "Kullanıcı başarı ile kaydedildi."
}
     */
    @POST("register")
    fun postRegister(@Body userRegisterInfo: UserRegisterInfo): retrofit2.Call<UserRegisterResponse>

    @POST("setrule")
    fun postRuleFloat(
        @Header("Authorization") authToken: String,
        @Body ruleInfoFloat: RuleInfoFloat
        ): retrofit2.Call<RuleResponseFloat>

    @POST("setrule")
    fun postRuleJson(
        @Header("Authorization") authToken: String,
        @Body ruleInfoJson: RuleInfoJson
    ): retrofit2.Call<RuleResponseJson>

    @GET("getrule")
    fun getRuleArray(
        @Header("Authorization") authToken: String
    ): retrofit2.Call<List<Map<String, Any>>>

    @POST("deleterule")
    fun postDeleteRule(
        @Header("Authorization") authToken: String,
        @Body ruleId : DeleteInfo
    ): retrofit2.Call<Map<String,Any>>


    @GET("/MustafaKoceerr/PrizProjem/main/sehirListesi.json")
    fun getSehirListesi(): retrofit2.Call<List<String>>

    @GET("/MustafaKoceerr/PrizProjem/main/il_ilce.json")
    fun getIlceListesi(): retrofit2.Call<List<Map<String, Any>>>



}

