package com.bigoffs.pdaremake.app.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.bigoffs.pdaremake.app.event.AppViewModel
import com.bigoffs.pdaremake.app.event.RfidViewModel
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.getAppViewModel

abstract class BaseRfidFragment<VM: BaseViewModel,DB:ViewDataBinding> : BaseFragment<VM,DB>(),RfidViewModel.OnFinishListener {

  val rfidViewModel: RfidViewModel by lazy { getAppViewModel<RfidViewModel>() }

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  initScan()
 }
 fun initScan(){
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
}