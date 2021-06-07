package com.bigoffs.pdaremake.app.base

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.event.ScanViewModel
import com.bigoffs.pdaremake.app.scan.receiver.OnReceiverListener
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.getAppViewModel
import java.io.IOException

abstract class BaseScanActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseActivity<VM, DB>(),OnReceiverListener{

    val scanViewModel: ScanViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

        scanViewModel.setOnReceiveListener(this)
    }



    override fun onDestroy() {
        super.onDestroy()
        scanViewModel.scanSwitch(false)
    }

    override fun onResume() {
        super.onResume()
        scanViewModel.addScannerStatusListener()
    }

    override fun onPause() {
        super.onPause()
        scanViewModel.removeScannerStatusListener()
    }

}