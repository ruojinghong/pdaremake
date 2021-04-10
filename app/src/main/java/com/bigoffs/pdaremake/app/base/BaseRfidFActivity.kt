package com.bigoffs.pdaremake.app.base

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import com.bigoffs.pdaremake.app.event.RfidViewModel
import com.bigoffs.pdaremake.app.service.OnFinishListener
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.getAppViewModel

/**
 *  描述　: 你项目中的Activity基类，在这里实现显示弹窗，吐司，还有加入自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmActivity例如
 * abstract class BaseActivity<VM : BaseViewModel> : BaseActivity<VM>() {
 */
abstract class BaseRfidFActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseActivity<VM, DB>(),OnFinishListener {


    val rfidViewModel: RfidViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        initScan()
    }
    private fun initScan(){
        rfidViewModel.initData()
        rfidViewModel.setReadDataModel(0)
        rfidViewModel.setMode(1)
        rfidViewModel.setCurrentSetting(RfidViewModel.Setting.stockRead)
        rfidViewModel.setListenerProtectModel(this)
    }

    override fun onPause() {
        super.onPause()
        rfidViewModel.stopReadRfid()
    }
    abstract fun  readOrClose()

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == 523) {
            if (event!!.repeatCount == 0) {
                readOrClose()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}