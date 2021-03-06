package com.bigoffs.pdaremake.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.bigoffs.pdaremake.app.network.apiService
import com.bigoffs.pdaremake.data.model.bean.ApiPagerResponse
import com.bigoffs.pdaremake.data.model.bean.BarcodeFind
import com.bigoffs.pdaremake.data.model.bean.InStoreBean
import com.bigoffs.pdaremake.data.model.bean.InStoreBeanList
import com.google.gson.Gson
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.BooleanLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 *User:Kirito
 *Time:2021/5/6  22:33
 *Desc:
 */
class RequestInstoreViewmodel : BaseViewModel() {

    var pageNo = 1


    var data  =  MutableLiveData<ResultState<InStoreBeanList>>()

    var isfresh = true;

    fun getNewInstoreList(type : String,search_type:String,search_value : String,isfresh:Boolean){
        this.isfresh = isfresh
        if(isfresh){
            pageNo = 1
        }
        val map  = hashMapOf<String,Any>()
        //（1=新品入库,3=调拨入库）
        map.put("type",type)
        //对应 inStore/options 中 search_option.value 字段 （1=条形码,2=供应商,3=批次号,4=店内码,5=箱号,6=调拨单号）
        if(search_type.isNotEmpty())map.put("search_type",search_type)
        //mock: 1=条形码：3333 2=供应商：耐克 3=批次号：2112012110000000001 4=店内码 ：ST1021213 5=箱号：27或28 6=调拨单号：33332133
        if(search_value.isNotEmpty()) map.put("search_value",search_value)
        map.put("page",pageNo)

        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.getInStoreList(requestBody)},data,true,"加载中...")

    }

    fun getTransferInstoreList(type : String,search_type:String,search_value : String,isfresh:Boolean){
        this.isfresh = isfresh
        if(isfresh){
            pageNo = 1
        }
        val map  = hashMapOf<String,Any>()
        //（1=新品入库,3=调拨入库）
        map["type"] = "3"
        //对应 inStore/options 中 search_option.value 字段 （1=条形码,2=供应商,3=批次号,4=店内码,5=箱号,6=调拨单号）
        if(search_type.isNotEmpty()) map["search_type"] = search_type
        //mock: 1=条形码：3333 2=供应商：耐克 3=批次号：2112012110000000001 4=店内码 ：ST1021213 5=箱号：27或28 6=调拨单号：33332133
        if(search_value.isNotEmpty()) map["search_value"] = search_value

        map["page"] = pageNo
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.getInStoreList(requestBody)},data,true,"加载中...")

    }

}