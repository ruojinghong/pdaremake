package com.bigoffs.pdaremake.viewmodel.state

import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.IntLiveData
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData

class MainViewModel : BaseViewModel() {
    //用户名
    var username = StringLiveData()
    //仓库名称
    var depotname = StringLiveData()
    //待办任务数量
    var taskNum = StringLiveData()


}