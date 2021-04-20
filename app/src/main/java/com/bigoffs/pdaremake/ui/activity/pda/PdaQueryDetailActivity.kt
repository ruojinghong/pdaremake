package com.bigoffs.pdaremake.ui.activity.pda

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.ext.*
import com.bigoffs.pdaremake.databinding.ActivityPdaQueryDetailBinding
import com.bigoffs.pdaremake.viewmodel.request.RequestQueryDetailViewModel
import com.bigoffs.pdaremake.viewmodel.request.RequestQueryViewModel
import com.bigoffs.pdaremake.viewmodel.state.QueryResultViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState

class PdaQueryDetailActivity :BaseActivity<QueryResultViewModel,ActivityPdaQueryDetailBinding>() {


    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    private val requestQueryDetailViewModel: RequestQueryDetailViewModel by viewModels()

    override fun layoutId(): Int  = R.layout.activity_pda_query_detail

    override fun setStatusBar() {
        initTitle(statusBarDarkFont = false, biaoti = "查询", rightBitaoti = "配置价签") {
            showMessage("配置价签")
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.vm = mViewModel

//状态页配置
        loadsir = loadServiceInit(findViewById(R.id.ll_content)) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestQueryDetailViewModel.queryDetail("","")
        }

        //设置界面 加载中
        loadsir.showLoading()
        requestQueryDetailViewModel.queryDetail("","")
    }

    override fun createObserver() {
        requestQueryDetailViewModel.queryDetail.observe(this, Observer {

            parseState(it, { list ->

                loadsir.showSuccess()


            }, {

                loadsir.showError("加载失败")


            }, {
                showLoading()
            })
        })
    }
}