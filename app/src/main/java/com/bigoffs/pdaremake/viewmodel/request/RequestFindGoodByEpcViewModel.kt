package com.bigoffs.pdaremake.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.bigoffs.pdaremake.app.network.apiService
import com.bigoffs.pdaremake.data.model.bean.BarcodeFind
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

class RequestFindGoodByEpcViewModel : BaseViewModel() {

    var data  =  MutableLiveData<ResultState<BarcodeFind>>()

    fun findGoodByEpc(uniqueCode : String,shelfcode:String){
        val map  = hashMapOf<String,Any>()
        map.put("barcode","aafdse")
        map.put("shelf_code","q-w-e-1")
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        request({ apiService.findGoodByEpc(requestBody)},data,true,"加载中...")

    }

}