package com.bigoffs.pdaremake.viewmodel.state

import com.bigoffs.pdaremake.data.model.bean.TallyBean
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
    var mapNum  = IntLiveData()
    //样本总数
    var  netSet = mutableSetOf<String>()
    var  normalList = mutableListOf<String>()
    var  errorList = mutableListOf<String>()
    //一次开启扫描到关闭扫描的数据集合
    var  oneScanList = mutableListOf<String>()




    var currentShelf =  StringLiveData()
    var CollectedNum = IntLiveData()

    var responseNum = IntLiveData()
    var currentFocus = IntLiveData()

    val normalNum = IntLiveData()
    var openErrorBottomSheet = IntLiveData()
    val scanList = arrayListOf<TallyBean>()

    val currentUniqueSet = hashSetOf<String>()


    val waitGroundingMap = hashMapOf<String,Int>()
    val alreadyGroundingMap = hashMapOf<String,Int>()

}