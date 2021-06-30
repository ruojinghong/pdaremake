package com.bigoffs.pdaremake.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.bigoffs.pdaremake.app.network.apiService
import com.bigoffs.pdaremake.data.model.bean.*
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

    var data =  MutableLiveData<ResultState<StocktakingBean>>()

    var systemList = MutableLiveData<ResultState<StocktakingSysData>>()
    var uploadResult = MutableLiveData<ResultState<Any>>()
    var checkResult = MutableLiveData<ResultState<CheckEpcCodeBean>>()





    fun getStocktakingList(w_id:String){
        val map  = hashMapOf<String,Any>()
        map.put("w_id",w_id)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.getStocktakingList(requestBody)},data,true,"加载中...")

    }

    fun getEpcSysData(st_id :String){
        val map  = hashMapOf<String,Any>()
        map.put("st_id",st_id)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.getEpcSysData(requestBody)},systemList,true,"加载中...")
    }

    fun uploadStData(st_id :String,admin_id:String,data:MutableList<String>){
        val map  = hashMapOf<String,Any>()
        map.put("st_id",st_id)
        map.put("admin_id",admin_id)
        map.put("data",data)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.uploadStData(requestBody)},uploadResult,true,"加载中...")
    }
    fun checkEpcCodes(st_id :String,epc_codes:MutableList<String>){
        val map  = hashMapOf<String,Any>()
        map.put("st_id",st_id)
        map.put("epc_codes",epc_codes)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.checkEpcCodes(requestBody)},checkResult,true,"加载中...")
    }

}