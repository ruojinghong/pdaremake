package com.bigoffs.pdaremake.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.ext.bindViewPager2
import com.bigoffs.pdaremake.app.ext.init
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.databinding.ActivityTaskBinding
import com.bigoffs.pdaremake.ui.fragment.LoginFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import net.lucode.hackware.magicindicator.MagicIndicator

/**
 *User:Kirito
 *Time:2021/4/30  0:18
 *Desc: 任务activity
 */
class TaskActivity : BaseActivity<BaseViewModel,ActivityTaskBinding>() {


    var fragments  = arrayListOf<Fragment>()

    override fun initView(savedInstanceState: Bundle?) {



        fragments.add(LoginFragment())
        fragments.add(LoginFragment())
        mDatabind.viewPager.init(this,fragments)
        mDatabind.magicIndicator.bindViewPager2( mDatabind.viewPager, arrayListOf("待办任务","已办任务"))
        mDatabind.viewPager.offscreenPageLimit = fragments.size
    }

    override fun layoutId(): Int = R.layout.activity_task

    override fun setStatusBar() {
        initTitle(false,biaoti = "任务")
    }
}