package com.bigoffs.pdaremake.ui.activity.pda

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.ext.*
import com.bigoffs.pdaremake.data.model.bean.QueryType
import com.bigoffs.pdaremake.databinding.ActivityPdaQueryBinding
import com.bigoffs.pdaremake.viewmodel.request.RequestQueryViewModel
import com.bigoffs.pdaremake.viewmodel.state.RfidQueryViewModel
import com.blankj.utilcode.util.LogUtils
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.util.ActivityMessenger

class PdaQueryActivity : BaseScanActivity<RfidQueryViewModel, ActivityPdaQueryBinding>() {
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    var select = arrayListOf<String>("店内码", "条形码", "货架")
    var currentList = arrayListOf<QueryType>();

    private val requestQueryViewModel: RequestQueryViewModel by viewModels()
    override fun initView(savedInstanceState: Bundle?) {

        scanViewModel.setOnReceiveListener(this)
        mDatabind.viewmodel = mViewModel
        findViewById<LinearLayout>(R.id.ll_select).setOnClickListener {

            showSpinner(it, this, select) { position, text ->
                setCurrentType(position)
            }

        }

        findViewById<EditText>(R.id.common_et).addOnEditorActionListener{

            onReceiverData(it)
        }

        //状态页配置
        loadsir = loadServiceInit(findViewById(R.id.ll_content)) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestQueryViewModel.getQueryData()
        }

        //设置界面 加载中
        loadsir.showLoading()
        requestQueryViewModel.getQueryData()
    }


    override fun layoutId(): Int = R.layout.activity_pda_query

    override fun setStatusBar() {
        initTitle(statusBarDarkFont = false, biaoti = "查询", rightBitaoti = "配置价签") {
            startActivity(Intent(this,UniqueQueryDetailActivity::class.java))
        }
    }

    override fun onReceiverData(data: String) {
        LogUtils.i("it---------$data")
            when( mViewModel.currentCodeText.value){
                    "店内码" ->{
                        ActivityMessenger.startActivity<UniqueQueryDetailActivity>(this,"unique" to data)
                    }
                "条形码" ->{
                    ActivityMessenger.startActivity<BarCodeQueryDetailActivity>(this,"barcode" to data)
                }
                "货号" ->{
                    ActivityMessenger.startActivity<PdaBarcodeQueryFindSamelActivity>(this,"spu_no" to data)
                }


            }
    }


    override fun createObserver() {
        requestQueryViewModel.queryData.observe(this, Observer {

            parseState(it, { list ->
                currentList.addAll(list)
                loadsir.showSuccess()
                val stringList = arrayListOf<String>()
                for (item in list){
                    stringList.add(item.name)
                }
                select = stringList
                setCurrentType(0)

            }, {

                loadsir.showError("加载失败")


            }, {
                showLoading()
            })
        })
    }

    fun setCurrentType(position: Int) {
        if (position >= currentList.size) {
            return
        }
        mViewModel.currentCodeType.value = currentList.get(position).type
        mViewModel.currentCodeText.value = currentList.get(position).name

    }
}