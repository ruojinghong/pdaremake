package com.bigoffs.pdaremake.viewmodel.state

import androidx.lifecycle.MutableLiveData
import com.bigoffs.pdaremake.data.model.bean.QueryResultBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData

class QueryResultViewModel : BaseViewModel() {
    //店内吗
    var uniqueCode = StringLiveData()
    //条形码
    var barcode = StringLiveData()
    var shelfcode = StringLiveData()
    var salePrice = StringLiveData()
    var spuName = StringLiveData()
    var spuNo = StringLiveData()
    var spuId = StringLiveData()
    var skuId = StringLiveData()
    var brandName = StringLiveData()
    var queryDetail: MutableLiveData<QueryResultBean> = MutableLiveData()

}