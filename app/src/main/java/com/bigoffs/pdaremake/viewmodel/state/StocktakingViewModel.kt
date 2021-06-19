package com.bigoffs.pdaremake.viewmodel.state

import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.IntLiveData
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData

/**
 *User:Kirito
 *Time:2021/6/19  23:27
 *Desc:
 */
class StocktakingViewModel : BaseViewModel() {
    var stocktakingid  = StringLiveData()
    var taskNum  = IntLiveData()
    var scanNum  = IntLiveData()
    var errorNum  = IntLiveData()

}