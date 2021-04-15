package com.bigoffs.pdaremake.ui.activity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.service.ScanServiceControl
import com.bigoffs.pdaremake.databinding.ActivityLoginBinding
import com.bigoffs.pdaremake.databinding.ActivitySplashBinding
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.dp2px
import me.hgj.jetpackmvvm.ext.util.px2dp

class SplashActivity : BaseActivity<BaseViewModel,ActivitySplashBinding>() {
    override fun layoutId(): Int  = R.layout.activity_splash

    override fun initView(savedInstanceState: Bundle?) {

        if(appViewModel.user.value?.token == null){
                startActivity(Intent(this,LoginActivity::class.java))
        }else{
                startActivity(Intent(this,MainActivity::class.java))
        }
        finish()
    }



    override fun setStatusBar() {

    }


}