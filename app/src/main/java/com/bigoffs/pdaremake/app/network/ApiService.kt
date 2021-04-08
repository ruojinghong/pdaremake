package com.bigoffs.pdaremake.app.network

import com.bigoffs.pdaremake.data.model.bean.ApiResponse
import com.bigoffs.pdaremake.data.model.bean.UserInfo
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    companion object{
        const val BASE_URL = "http://wms.pandora.okbuy.com/"
        const val TEST_URL = "http://10.1.1.3:2022/mock/13/"
//        val BASE_URL = ""
    }
    @Headers("Content-type:application/json")
    @POST("Index/login")
    @FormUrlEncoded
    suspend fun  login(
        @Field("user_name") username:String,
        @Field("password") password :String,
        @Field("w_id") id:String
    ):ApiResponse<UserInfo>

    @Headers("Content-type:application/json")
    @POST("instore/detail")
    @FormUrlEncoded
    suspend fun  detail(
        @Field("id") id:String
    ):ApiResponse<List<UserInfo>>
}