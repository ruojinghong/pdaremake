package com.bigoffs.pdaremake.app.event

import com.bigoffs.pdaremake.data.model.bean.TokenBus
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.event.EventLiveData

/**
 * 描述　:APP全局的ViewModel，可以在这里发送全局通知替代EventBus，LiveDataBus等
 */
class EventViewModel : BaseViewModel() {

    val tokenEvent = EventLiveData<TokenBus>()
}