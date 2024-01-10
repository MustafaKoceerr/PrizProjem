package com.mustafa.prizprojem.services

import com.mustafa.prizprojem.models.DenemeData
import com.mustafa.prizprojem.models.Il_Ilce
import com.mustafa.prizprojem.models.UserRegisterInfo
import com.mustafa.prizprojem.models.UserInfo
import com.mustafa.prizprojem.models.UserRegisterResponse
import com.mustafa.prizprojem.models.UserResponse
import com.mustafa.prizprojem.models.myExampleDataClass
import com.mustafa.prizprojem.models.myExampleDataClass2
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.Objects

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

    @GET("/MustafaKoceerr/PrizProjem/main/sehirListesi.json")
    fun getSehirListesi(): retrofit2.Call<List<String>>

    @GET("/MustafaKoceerr/PrizProjem/main/il_ilce.json")
    fun getIlceListesi(): retrofit2.Call<List<Map<String,Any>>>

   @GET("/MustafaKoceerr/PrizProjem/main/deneme.json")
   fun getDeneme(): retrofit2.Call<List<Any>>

}

