package com.bigoffs.pdaremake.viewmodel.state

import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.databind.StringObservableField
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData

class LoginRegisterViewModel : BaseViewModel() {

    //用户名
    var username = StringLiveData()

    //密码(登录注册界面)
    var password = StringLiveData()
}