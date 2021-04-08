package com.bigoffs.pdaremake.app.network

import com.bigoffs.pdaremake.data.model.bean.ApiResponse
import com.bigoffs.pdaremake.data.model.bean.UserInfo
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    companion object{
        const val BASE_URL = "http://wms.pandora.okbuy.com/"
        const val TEST_URL = "http://10.1.1.3:2022/mock/13/"
//        val BASE_URL = ""
    }

    @POST("Index/login")
    suspend fun  login(
        @Body  body : RequestBody
    ):ApiResponse<UserInfo>


    @POST("v1/shelf/query")
    suspend fun  detail(
        @Body  body : RequestBody
    ):ApiResponse<List<UserInfo>>
}