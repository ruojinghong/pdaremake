package com.bigoffs.pdaremake.ui.activity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.service.ScanServiceControl
import com.bigoffs.pdaremake.databinding.ActivityLoginBinding
import com.bigoffs.pdaremake.databinding.ActivitySplashBinding
import com.blankj.utilcode.util.ToastUtils
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class SplashActivity : BaseActivity<BaseViewModel,ActivitySplashBinding>() {
    override fun layoutId(): Int  = R.layout.activity_splash

    override fun initView(savedInstanceState: Bundle?) {


    }



    override fun setStatusBar() {

    }


}