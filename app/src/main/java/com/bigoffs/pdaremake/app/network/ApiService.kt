package com.bigoffs.pdaremake.app.network

import com.bigoffs.pdaremake.data.model.bean.*
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    companion object{
        //        dev-pda.bigoffs.com
        //登录接口 admin.bigoffs.com

        const val BASE_URL = "http://wms.pandora.okbuy.com/"
//        const val TEST_URL = "http://10.1.1.3:2022/mock/13/"
//        const val TEST_URL = "http://admin.bigoffs.com:9080/"
        const val TEST_URL = "http://dev-pda.bigoffs.com:9080/"
//        val BASE_URL = ""
    }
    @Headers("Domain-Name:loginurl")
    @POST("login")
    suspend fun  login(
        @Body  body : RequestBody
    ):ApiResponse<User>

    @POST("v1/search/warehouseList")
    suspend fun  getHouse(
        @Body  body : RequestBody
    ):ApiResponse<List<House>>


    @POST("v1/shelf/query")
    suspend fun  detail(
        @Body  body : RequestBody
    ):ApiResponse<User>

    @POST("v1/search/index")
    suspend fun getQueryType(
        @Body  body : RequestBody
    ):ApiResponse<List<QueryType>>

    @POST("v1/search/barcode")
    suspend fun getBarcodeQueryDetail(
        @Body  body : RequestBody
    ):ApiResponse<QueryResultBean>

    @POST("v1/search/unique")
    suspend fun getQueryDetail(
        @Body  body : RequestBody
    ):ApiResponse<QueryResultBean>

    @POST("v1/search/spu_no")
    suspend fun findSameByUnique(
        @Body  body : RequestBody
    ):ApiResponse<FindSame>


    @POST("v1/search/barcode_find")
    suspend fun findGoodByEpc(
        @Body  body : RequestBody
    ):ApiResponse<BarcodeFind>

    @POST("v1/search/unique_find")
    suspend fun findEpcByUnicode(
        @Body  body : RequestBody
    ):ApiResponse<FindEpcByUnicodeBean>


    @POST("v1/inStore/list")
    suspend fun getInStoreList(
        @Body  body : RequestBody
    ):ApiResponse<InStoreBeanList>


    @POST("/v1/inStore/refData")
    suspend fun getInStoreDetail(
        @Body  body : RequestBody
    ):ApiResponse<NewInStoreDetail>

    @POST("/v1/tally/uploadTallyData")
    suspend fun uploadTallyData(
        @Body  body : RequestBody
    ):ApiResponse<Any>


    @POST("/v1/inStore/save")
    suspend fun newInStoreByUnique(
        @Body  body : RequestBody
    ):ApiResponse<Any>

    @POST("/v1/stocktaking/index")
    suspend fun getStocktakingList(
        @Body  body : RequestBody
    ):ApiResponse<StocktakingBean>

    @POST("/v1/stocktaking/getEpcSysData")
    suspend fun getEpcSysData(
        @Body  body : RequestBody
    ):ApiResponse<StocktakingSysData>

    @POST("/v1/stocktaking/uploadStData")
    suspend fun uploadStData(
        @Body  body : RequestBody
    ):ApiResponse<Any>

    @POST("/v1/stocktaking/checkEpcCodes")
    suspend fun checkEpcCodes(
        @Body  body : RequestBody
    ):ApiResponse<CheckEpcCodeBean>

    @POST("/v1/tally/checkEpcCodes")
    suspend fun tallyCheckEpcCodes(
        @Body  body : RequestBody
    ):ApiResponse<CheckEpcTally>
}