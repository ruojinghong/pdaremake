package com.bigoffs.pdaremake.viewmodel.state

import androidx.lifecycle.MutableLiveData
import com.bigoffs.pdaremake.data.model.bean.BarcodeFind
import com.bigoffs.pdaremake.data.model.bean.FindSame
import com.bigoffs.pdaremake.data.model.bean.QueryResultBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.IntLiveData
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData

class FindGoodByEpcViewModel : BaseViewModel() {
    var currentPosition  = IntLiveData()
    var currentUnique = StringLiveData()
    var epc = StringLiveData()
    var unique  = StringLiveData()
    var shelfCode  = StringLiveData()
    var foodList: MutableLiveData<BarcodeFind> = MutableLiveData()

}