package com.bigoffs.pdaremake.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.bigoffs.pdaremake.app.network.apiService
import com.bigoffs.pdaremake.data.model.bean.QueryResultBean
import com.bigoffs.pdaremake.data.model.bean.QueryType
import com.google.gson.Gson
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*

class RequestQueryDetailViewModel : BaseViewModel() {

    var queryDetail  =  MutableLiveData<ResultState<QueryResultBean>>()

    fun queryDetail(wid:String,uniqueCode : String){
        val map  = hashMapOf<String,Any>()
        map.put("w_id",14080257)
        map.put("unique_code","ST1021331")

        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.getQueryDetail(requestBody)},queryDetail,true,"加载中...")

    }

}