package com.bigoffs.pdaremake.viewmodel.state

import androidx.lifecycle.MutableLiveData
import com.bigoffs.pdaremake.data.model.bean.FindSame
import com.bigoffs.pdaremake.data.model.bean.QueryResultBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData

class QueryPdaUniqueFindSameViewModel : BaseViewModel() {
    var uniqueCode  = StringLiveData()
    var salePrice  = StringLiveData()
    var queryDetail: MutableLiveData<FindSame> = MutableLiveData()
    var spec = StringLiveData()
    var stockNum = StringLiveData()

}