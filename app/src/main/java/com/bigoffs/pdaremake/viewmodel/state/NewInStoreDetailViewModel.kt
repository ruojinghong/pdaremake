package com.bigoffs.pdaremake.viewmodel.state

import android.util.ArraySet
import androidx.collection.arraySetOf
import androidx.lifecycle.MutableLiveData
import com.bigoffs.pdaremake.data.model.bean.NewInStoreDetail
import com.bigoffs.pdaremake.data.model.bean.NewInStoreErrorBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreNormalBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.IntLiveData
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData

/**
 *User:Kirito
 *Time:2021/5/11  0:32
 *Desc:
 */
class NewInStoreDetailViewModel : BaseViewModel() {
    //货品数
    var goodsCount  = IntLiveData()
    //已入库数
    var inStoreCount  = IntLiveData()
    //本次入库数
    var thisCount  = IntLiveData()

    val detail = MutableLiveData<NewInStoreDetail>()


    var currentFocus = IntLiveData()
    //当前扫描过的店内码
    val currentUniqueSet = arraySetOf<String>()
    //当前扫描过的二维码
    val currentBarCodeSet = arraySetOf<String>()
    //二维码 sku 映射表
    val barcodeSkuMap = mutableMapOf<String,Int>()
    //未入库的sku 数量 映射表
    val taskSkuNumMap = mutableMapOf<String,Int>()
    //已入库的sku 数量 映射表
    val inSotreSkuNumMap = mutableMapOf<String,Int>()
        //当前扫描的错误列表
    val currenErrorList = arrayListOf<NewInStoreErrorBean>()
    //当前扫描的正常列表
    val currenNormalList = arrayListOf<NewInStoreNormalBean>()



}