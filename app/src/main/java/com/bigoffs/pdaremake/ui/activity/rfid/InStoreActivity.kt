package com.bigoffs.pdaremake.ui.activity.rfid

import android.os.Bundle
import android.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.base.BaseInStoreFragment
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.ext.bindViewPager2
import com.bigoffs.pdaremake.app.ext.init
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.databinding.ActivityPadInstoreBinding
import com.bigoffs.pdaremake.ui.fragment.LoginFragment
import com.bigoffs.pdaremake.ui.fragment.PdaNewInstoreFragment
import com.bigoffs.pdaremake.ui.fragment.PdaTransferInstoreFragment
import com.google.android.material.tabs.TabLayoutMediator
import me.hgj.jetpackmvvm.base.activity.BaseVmDbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import net.lucode.hackware.magicindicator.MagicIndicator

/**
 *User:Kirito
 *Time:2021/4/29  22:11
 *Desc:入库activity
 */
class InStoreActivity : BaseScanActivity<BaseViewModel, ActivityPadInstoreBinding>() {

    lateinit var view_pager: ViewPager2
    lateinit var magic_indicator: MagicIndicator
    var fragments  = arrayListOf<Fragment>()



    override fun layoutId(): Int = R.layout.activity_pad_instore

    override fun setStatusBar() {
        initTitle(false, biaoti = "入库")
    }

    override fun onReceiverData(data: String) {
        (fragments.get(view_pager.currentItem) as BaseInStoreFragment<BaseViewModel, ViewDataBinding>).goInStoreDetail(data)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        view_pager = findViewById(R.id.view_pager)
        magic_indicator = findViewById(R.id.magic_indicator)

        fragments.add(PdaNewInstoreFragment())
        fragments.add(PdaTransferInstoreFragment())
        view_pager.init(this,fragments)
        magic_indicator.bindViewPager2(view_pager, arrayListOf("新品入库","调拨入库"))
        view_pager.offscreenPageLimit = fragments.size



    }


}