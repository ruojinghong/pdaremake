package com.bigoffs.pdaremake.app.base

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.bigoffs.pdaremake.app.event.AppViewModel
import com.bigoffs.pdaremake.app.event.RfidViewModel
import com.bigoffs.pdaremake.app.service.OnFinishListener
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.getAppViewModel

abstract class BaseRfidFragment<VM: BaseViewModel,DB:ViewDataBinding> : BaseFragment<VM,DB>(),
 OnFinishListener {

  val rfidViewModel: RfidViewModel by viewModels()

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  initScan()
 }
 abstract fun initScan()

 override fun onPause() {
  super.onPause()
  rfidViewModel.stopReadRfid()
 }

 abstract fun  readOrClose()


}