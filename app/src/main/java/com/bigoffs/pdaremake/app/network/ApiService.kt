package com.bigoffs.pdaremake.app.network

import com.bigoffs.pdaremake.data.model.bean.ApiResponse
import com.bigoffs.pdaremake.data.model.bean.UserInfo
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    companion object{
        const val BASE_URL = "http://wms.pandora.okbuy.com/"
//        val BASE_URL = ""
    }
    @POST
    @FormUrlEncoded
    suspend fun  login(
        @Field("user_name") username:String
        ,@Field("password") password :String
    ):ApiResponse<UserInfo>
}