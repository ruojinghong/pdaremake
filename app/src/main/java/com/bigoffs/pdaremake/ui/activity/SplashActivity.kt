package com.bigoffs.pdaremake.ui.activity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.service.ScanServiceControl
import com.bigoffs.pdaremake.databinding.ActivityLoginBinding
import com.bigoffs.pdaremake.databinding.ActivitySplashBinding
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class SplashActivity : BaseActivity<BaseViewModel,ActivitySplashBinding>() {
    override fun layoutId(): Int  = R.layout.activity_splash
    val scanService = ScanServiceControl.getScanService()
    override fun initView(savedInstanceState: Bundle?) {

        scanService.init(this)
        InitTask().execute()
    }

    inner class InitTask :AsyncTask<String,Int,Boolean>(){
        override fun doInBackground(vararg params: String?): Boolean {
            return scanService.openReader()
        }

        override fun onPostExecute(result: Boolean?) {

           startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
            finish()
        }

    }

    override fun setStatusBar() {

    }
}