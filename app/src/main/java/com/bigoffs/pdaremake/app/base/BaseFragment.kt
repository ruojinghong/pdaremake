package com.bigoffs.pdaremake.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bigoffs.pdaremake.app.event.AppViewModel
import com.bigoffs.pdaremake.app.event.EventViewModel
import com.bigoffs.pdaremake.app.ext.dismissLoadingExt
import com.bigoffs.pdaremake.app.ext.hideSoftKeyboard
import com.bigoffs.pdaremake.app.ext.showLoadingExt
import me.hgj.jetpackmvvm.base.fragment.BaseVmDbFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.getAppViewModel

abstract class BaseFragment<VM:BaseViewModel,DB:ViewDataBinding> : BaseVmDbFragment<VM,DB>(){
    //Application全局的ViewModel，里面存放了一些账户信息，基本配置信息等
    val appViewModel: AppViewModel by lazy { getAppViewModel<AppViewModel>() }

    //Application全局的ViewModel，用于发送全局通知操作
    val eventViewModel: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract override fun layoutId(): Int


    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载 只有当前fragment视图显示时才会触发该方法
     */
    override fun lazyLoadData() {}

    /**
     * 创建LiveData观察者 Fragment执行onViewCreated后触发
     */
    override fun createObserver() {}

    /**
     * Fragment执行onViewCreated后触发
     */
    override fun initData() {

    }

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        dismissLoadingExt()
    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard(activity)
    }
}