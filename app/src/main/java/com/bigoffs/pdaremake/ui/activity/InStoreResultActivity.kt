package com.bigoffs.pdaremake.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.ext.init
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.data.model.bean.InStoreResultBean
import com.bigoffs.pdaremake.databinding.ActivityInstoreResultBinding
import com.bigoffs.pdaremake.ui.adapter.InStoreResultAdapter
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 *User:Kirito
 *Time:2021/5/23  22:21
 *Desc:入库结果页
 */
class InStoreResultActivity : BaseActivity<BaseViewModel,ActivityInstoreResultBinding>(){

    private val adapter: InStoreResultAdapter by lazy { InStoreResultAdapter(arrayListOf()) }
    override fun layoutId(): Int  = R.layout.activity_instore_result

    override fun setStatusBar() {
        initTitle(false, biaoti = "入库结果")
    }

    override fun initView(savedInstanceState: Bundle?) {
            adapter.addHeaderView(LayoutInflater.from(mContext)
                .inflate(R.layout.header_instore_result,null))

        mDatabind.recyclerView.init(LinearLayoutManager(mContext),adapter)

        for(i in 1..10){
            adapter.addData(InStoreResultBean(312312))
        }
    }
}