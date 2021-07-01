package com.bigoffs.pdaremake.viewmodel.state

import com.bigoffs.pdaremake.data.model.bean.TallyBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.IntLiveData
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData

/**
 *User:Kirito
 *Time:2021/6/15  23:16
 *Desc:
 */
class TallyViewModel : BaseViewModel() {

    var currentShelf =  StringLiveData()
    var CollectedNum = IntLiveData()
    var scanNum = IntLiveData()
    var responseNum = IntLiveData()
    var currentFocus = IntLiveData()
    val errorNum = IntLiveData()
    val normalNum = IntLiveData()
    var openErrorBottomSheet = IntLiveData()
    val scanList = arrayListOf<TallyBean>()
    var oneScanList = mutableListOf<String>()
    val currentUniqueSet = hashSetOf<String>()


    val waitGroundingMap = hashMapOf<String,Int>()
    val alreadyGroundingMap = hashMapOf<String,Int>()


}