package com.bigoffs.pdaremake.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.bigoffs.pdaremake.app.network.apiService
import com.bigoffs.pdaremake.app.util.CacheUtil
import com.bigoffs.pdaremake.data.model.bean.*
import com.google.gson.Gson
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData
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

    var checkShelf = MutableLiveData<ResultState<Any>>()
    var checkUnique = MutableLiveData<ResultState<Any>>()
    var checkBarcode = MutableLiveData<ResultState<Any>>()
    fun stocktakingCheckShelf(epc_codes:String){
        val map  = hashMapOf<String,Any>()
        map.put("shelf_code",epc_codes)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.stocktakingCheckShelf(requestBody)},checkShelf,true,"加载中...")
    }

    fun stocktakingCheckUnique(epc_codes:String){
        val map  = hashMapOf<String,Any>()
        map.put("unique_code",epc_codes)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.stocktakingCheckUnique(requestBody)},checkUnique,true,"加载中...")
    }

    fun stocktakingCheckBarcode(epc_codes:String){
        val map  = hashMapOf<String,Any>()
        map.put("barcode",epc_codes)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.tocktakingCheckBarcode(requestBody)},checkBarcode,true,"加载中...")
    }

    var uniqueUploadResult = MutableLiveData<ResultState<Any>>()
    //品码类型：1店内码；2条码，3epc码
    fun uploadStacktakingData(data:MutableList<UniqueStocktakingBean>,save_type:Int,id:String,st_way:Int) {
        //1.这种是在 Activity/Fragment的监听回调中拿到已脱壳的数据（项目有基类的可以用）

        val map = HashMap<String, Any>()
        map.put("save_type",save_type)
        map.put("st_id", id)

        map.put("data",data)
        when(save_type){
            5 -> { map.put("st_way",1)}
            7 -> { map.put("st_way",2)}
            else ->  map.put("st_way",save_type)
        }
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request(
            { apiService.uploadStocktakingData(requestBody) }//请求体
            , uniqueUploadResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            true,//是否显示等待框，，默认false不显示 可以默认不传
            "加载中..."//等待框内容，可以默认不填请求网络中...
        )
    }


}