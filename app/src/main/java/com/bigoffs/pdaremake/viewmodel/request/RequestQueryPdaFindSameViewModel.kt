package com.bigoffs.pdaremake.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.bigoffs.pdaremake.app.network.apiService
import com.bigoffs.pdaremake.data.model.bean.FindSame
import com.bigoffs.pdaremake.data.model.bean.QueryResultBean
import com.bigoffs.pdaremake.data.model.bean.QueryType
import com.google.gson.Gson
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*

class RequestQueryPdaFindSameViewModel : BaseViewModel() {

    var same  =  MutableLiveData<ResultState<FindSame>>()

    fun findSameByUnique(uniqueCode : String){
        val map  = hashMapOf<String,Any>()
        map.put("unique_code",uniqueCode)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.findSameBySpuNo(requestBody)},same,true,"加载中...")

    }
    fun findSameByBarcode(uniqueCode : String){
        val map  = hashMapOf<String,Any>()
        map.put("barcode",uniqueCode)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.findSameBySpuNo(requestBody)},same,true,"加载中...")

    }
    fun findSameBySpuNo(uniqueCode : String){
        val map  = hashMapOf<String,Any>()
        map.put("spu_no",uniqueCode)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.findSameBySpuNo(requestBody)},same,true,"加载中...")

    }

}