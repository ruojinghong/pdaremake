package com.bigoffs.pdaremake.viewmodel.state

import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.IntLiveData
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData

class RfidQueryViewModel : BaseViewModel() {

    //当前选择的要扫描的数据类型描述
    var currentCodeText = StringLiveData()

    var currentCodeType = StringLiveData()
}