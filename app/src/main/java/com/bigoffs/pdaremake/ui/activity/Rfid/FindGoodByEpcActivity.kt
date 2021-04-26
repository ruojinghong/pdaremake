package com.bigoffs.pdaremake.ui.activity.Rfid

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseRfidFActivity
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.app.ext.loadServiceInit
import com.bigoffs.pdaremake.app.ext.showError
import com.bigoffs.pdaremake.app.ext.showLoading
import com.bigoffs.pdaremake.databinding.ActivityFindGoosByEpcBinding
import com.bigoffs.pdaremake.viewmodel.request.RequestFindGoodByEpcViewModel
import com.bigoffs.pdaremake.viewmodel.request.RequestQueryPdaFindSameViewModel
import com.bigoffs.pdaremake.viewmodel.state.FindGoodByEpcViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState

class FindGoodByEpcActivity : BaseRfidFActivity<FindGoodByEpcViewModel,ActivityFindGoosByEpcBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    private val requestViewModel: RequestFindGoodByEpcViewModel by viewModels()



    override fun readOrClose() {
        TODO("Not yet implemented")
    }

    override fun layoutId(): Int  = R.layout.activity_find_goos_by_epc

    override fun setStatusBar() {
       initTitle(false,biaoti = "找货")
    }

    override fun initView(savedInstanceState: Bundle?) {

        mViewModel.shelfCode.value = intent.getStringExtra("shelf_code")+""
        mViewModel.unique.value = intent.getStringExtra("unique")+""

        mDatabind.vm = mViewModel

        //状态页配置
        loadsir = loadServiceInit(findViewById(R.id.ll_content)) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestViewModel.findGoodByEpc( mViewModel.unique.value,mViewModel.shelfCode.value)
        }
        loadsir.showLoading()
        requestViewModel.findGoodByEpc( mViewModel.unique.value,mViewModel.shelfCode.value)
    }

    override fun onFinish(data: String) {
        TODO("Not yet implemented")
    }

    override fun createObserver() {
        super.createObserver()
        requestViewModel.data.observe(this, Observer {
            parseState(it, { data ->
                //成功
                    mViewModel.foodList.value = data

            }, { excepion ->
                //失败
                loadsir.showError(excepion.message + "")
            }, {
                //加载中
                showLoading("加载中")
            })


        })
    }
}