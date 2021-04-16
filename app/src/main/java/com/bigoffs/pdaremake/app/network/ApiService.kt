package com.bigoffs.pdaremake.app.network

import com.bigoffs.pdaremake.data.model.bean.ApiResponse
import com.bigoffs.pdaremake.data.model.bean.QueryType
import com.bigoffs.pdaremake.data.model.bean.User
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    companion object{
        //        dev-pda.bigoffs.com
        //登录接口 admin.bigoffs.com

        const val BASE_URL = "http://wms.pandora.okbuy.com/"
//        const val TEST_URL = "http://10.1.1.3:2022/mock/13/"
        const val TEST_URL = "http://admin.bigoffs.com:9080/"
//        const val TEST_URL = "http://dev-pda.bigoffs.com:9080/"
//        val BASE_URL = ""
    }

    @POST("login")
    suspend fun  login(
        @Body  body : RequestBody
    ):ApiResponse<User>


    @POST("v1/shelf/query")
    suspend fun  detail(
        @Body  body : RequestBody
    ):ApiResponse<User>
    @POST("v1/shelf/query")
    suspend fun getQueryType(
        @Body  body : RequestBody
    ):ApiResponse<List<QueryType>>
}