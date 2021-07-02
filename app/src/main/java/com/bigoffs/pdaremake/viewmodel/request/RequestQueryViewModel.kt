package com.bigoffs.pdaremake.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.bigoffs.pdaremake.app.network.apiService
import com.bigoffs.pdaremake.data.model.bean.QueryType
import com.google.gson.Gson
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import okhttp3.MediaType
import okhttp3.RequestBody

class RequestQueryViewModel : BaseViewModel() {
                var queryData  =  MutableLiveData<ResultState<List<QueryType>>>()

        fun getQueryData(){
                val requestBody: RequestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "")
                request({apiService.getQueryType(requestBody)},queryData,true,"加载中...")

        }




}