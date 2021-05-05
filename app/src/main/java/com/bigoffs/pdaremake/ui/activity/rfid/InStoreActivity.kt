package com.bigoffs.pdaremake.ui.activity.rfid

import android.os.Bundle
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.ext.bindViewPager2
import com.bigoffs.pdaremake.app.ext.init
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.databinding.ActivityPadInstoreBinding
import com.bigoffs.pdaremake.ui.fragment.LoginFragment
import com.google.android.material.tabs.TabLayoutMediator
import me.hgj.jetpackmvvm.base.activity.BaseVmDbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import net.lucode.hackware.magicindicator.MagicIndicator

/**
 *User:Kirito
 *Time:2021/4/29  22:11
 *Desc:入库activity
 */
class InStoreActivity : BaseActivity<BaseViewModel, ActivityPadInstoreBinding>() {

    lateinit var view_pager: ViewPager2
    lateinit var magic_indicator: MagicIndicator
    var fragments  = arrayListOf<Fragment>()



    override fun layoutId(): Int = R.layout.activity_pad_instore

    override fun setStatusBar() {
        initTitle(false, biaoti = "入库")
    }

    override fun initView(savedInstanceState: Bundle?) {
        view_pager = findViewById(R.id.view_pager)
        magic_indicator = findViewById(R.id.magic_indicator)

        fragments.add(LoginFragment())
        fragments.add(LoginFragment())
        view_pager.init(this,fragments)
        magic_indicator.bindViewPager2(view_pager, arrayListOf("新品入库","调拨入库"))
        view_pager.offscreenPageLimit = fragments.size

        view_pager.currentItem


    }


}