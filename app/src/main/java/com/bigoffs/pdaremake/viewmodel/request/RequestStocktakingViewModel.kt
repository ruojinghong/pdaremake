package com.bigoffs.pdaremake.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.bigoffs.pdaremake.app.network.apiService
import com.bigoffs.pdaremake.data.model.bean.BarcodeFind
import com.google.gson.Gson
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 *User:Kirito
 *Time:2021/6/21  23:42
 *Desc:
 */
class RequestStocktakingViewModel : BaseViewModel() {

    var data =  MutableLiveData<ResultState<Any>>()

    fun getStocktakingList(w_id:String){
        val map  = hashMapOf<String,Any>()
        map.put("w_id","w_id")
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.getStocktakingList(requestBody)},data,true,"加载中...")

    }


}