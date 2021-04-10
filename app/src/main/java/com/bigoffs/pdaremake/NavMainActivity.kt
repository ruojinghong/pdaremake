package com.bigoffs.pdaremake

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.util.StatusBarUtil
import com.bigoffs.pdaremake.databinding.ActivityMainBinding
import com.bigoffs.pdaremake.viewmodel.state.MainViewModel
import com.blankj.utilcode.util.ToastUtils
import me.hgj.jetpackmvvm.network.manager.NetState

class NavMainActivity : BaseActivity<MainViewModel,ActivityMainBinding>() {

    var exitTime = 0L
    override fun layoutId(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
            onBackPressedDispatcher.addCallback(this,object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    val nav = Navigation.findNavController(this@NavMainActivity, R.id.host_fragment)
                    if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.mainfragment) {
                        //如果当前界面不是主页，那么直接调用返回即可
                        nav.navigateUp()
                    } else {
                        //是主页
                        if (System.currentTimeMillis() - exitTime > 2000) {
                            ToastUtils.showShort("再按一次退出程序")
                            exitTime = System.currentTimeMillis()
                        } else {
                            finish()
                        }
                    }
                }

            })
    }

    override fun createObserver() {
        appViewModel.appColor.observe(this, Observer {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
            StatusBarUtil.setColor(this, it, 0)
        })
    }

    /**
     * 示例，在Activity/Fragment中如果想监听网络变化，可重写onNetworkStateChanged该方法
     */
    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        if (netState.isSuccess) {
            Toast.makeText(applicationContext, "网络恢复!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "请检查网络设置", Toast.LENGTH_SHORT).show()
        }
    }

    override fun setStatusBar() {

    }

}