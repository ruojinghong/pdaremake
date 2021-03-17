package com.bigoffs.pdaremake

import android.os.Bundle
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.databinding.ActivityMainBinding
import com.bigoffs.pdaremake.viewmodel.state.MainViewModel
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity

class MainActivity : BaseActivity<MainViewModel,ActivityMainBinding>() {
    override fun layoutId(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
       showLoading("初始化成功")
    }
}