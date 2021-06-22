package com.bigoffs.pdaremake.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.app.util.CacheUtil
import com.bigoffs.pdaremake.databinding.ActivityMainBinding
import com.bigoffs.pdaremake.databinding.ActivityStocktakingListBinding
import com.bigoffs.pdaremake.viewmodel.request.RequestInstoreViewmodel
import com.bigoffs.pdaremake.viewmodel.request.RequestStocktakingViewModel
import com.bigoffs.pdaremake.viewmodel.state.StocktakingViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 *User:Kirito
 *Time:2021/6/21  23:37
 *Desc:
 */
class StocktakingActivity : BaseActivity<StocktakingViewModel,ActivityStocktakingListBinding>() {


    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    val requestInstoreViewmodel : RequestStocktakingViewModel by viewModels()


    override fun layoutId(): Int = R.layout.activity_stocktaking_list

    override fun setStatusBar() {
      initTitle(false,biaoti = "盘点")
    }

    override fun initView(savedInstanceState: Bundle?){
            requestInstoreViewmodel.getStocktakingList(CacheUtil.getHouse()?.id.toString())
    }
}