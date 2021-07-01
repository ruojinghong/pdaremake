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
 *Time:2021/6/17  0:47
 *Desc:
 */
class RequestTallyViewModel :BaseViewModel() {
    var detail = MutableLiveData<ResultState<NewInStoreDetail>>()
    fun getInStoreDetail(instoreId: Int) {
        //1.这种是在 Activity/Fragment的监听回调中拿到已脱壳的数据（项目有基类的可以用）
        val map = HashMap<String, Int>()
        map.put("in_store_id",instoreId)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request(
            { apiService.getInStoreDetail(requestBody) }//请求体
            , detail,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            true,//是否显示等待框，，默认false不显示 可以默认不传
            "加载中..."//等待框内容，可以默认不填请求网络中...
        )
    }

    var uniqueUploadResult = MutableLiveData<ResultState<Any>>()
    //品码类型：1店内码；2条码，3epc码
    fun uploadTallyData(data:MutableList<TallyBean>,t_type:String) {
        //1.这种是在 Activity/Fragment的监听回调中拿到已脱壳的数据（项目有基类的可以用）
        val map = HashMap<String, Any>()
        map.put("data",data)
        map.put("t_type",t_type)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request(
            { apiService.uploadTallyData(requestBody) }//请求体
            , uniqueUploadResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            true,//是否显示等待框，，默认false不显示 可以默认不传
            "加载中..."//等待框内容，可以默认不填请求网络中...
        )
    }

    var barcodeUploadResult = MutableLiveData<ResultState<Any>>()
    //品码类型：1店内码；2条码，3epc码
    fun uploadBarcodeTallyData(data:MutableList<StocktakingOnBean>,t_type:String) {
        //1.这种是在 Activity/Fragment的监听回调中拿到已脱壳的数据（项目有基类的可以用）
        val map = HashMap<String, Any>()
        map.put("data",data)
        map.put("t_type",t_type)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request(
            { apiService.uploadTallyData(requestBody) }//请求体
            , barcodeUploadResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            true,//是否显示等待框，，默认false不显示 可以默认不传
            "加载中..."//等待框内容，可以默认不填请求网络中...
        )
    }

    var checkResult = MutableLiveData<ResultState<CheckEpcTally>>()
    fun checkEpcCodes(epc_codes:MutableList<String>){
        val map  = hashMapOf<String,Any>()
        map.put("data",epc_codes)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.tallyCheckEpcCodes(requestBody)},checkResult,true,"加载中...")
    }
}