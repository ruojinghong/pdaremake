package com.bigoffs.pdaremake.viewmodel.state

import androidx.lifecycle.MutableLiveData
import com.bigoffs.pdaremake.data.model.bean.FindEpcByUnicodeBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData

/**
 *User:Kirito
 *Time:2021/4/29  1:01
 *Desc:
 */
class FindEpcByUnicodeViewModel : BaseViewModel() {

    var data = MutableLiveData<FindEpcByUnicodeBean>()
    var unique  = StringLiveData()
    var shelfCode  = StringLiveData()
    var currentEpc  = StringLiveData()

}