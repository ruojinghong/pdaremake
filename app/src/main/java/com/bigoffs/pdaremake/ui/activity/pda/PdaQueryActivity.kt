package com.bigoffs.pdaremake.ui.activity.pda

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseRfidFActivity
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.event.RfidViewModel
import com.bigoffs.pdaremake.app.ext.*
import com.bigoffs.pdaremake.app.util.StatusBarUtil
import com.bigoffs.pdaremake.databinding.ActivityRfidQueryBinding
import com.bigoffs.pdaremake.ui.customview.DropDownMenu
import com.bigoffs.pdaremake.viewmodel.request.RequestQueryViewModel
import com.bigoffs.pdaremake.viewmodel.state.RfidQueryViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState

class PdaQueryActivity : BaseScanActivity<RfidQueryViewModel,ActivityRfidQueryBinding>() {
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    var select  = arrayListOf<String>("店内码","条形码","货架")

    private val requestQueryViewModel: RequestQueryViewModel by viewModels()
    override fun initView(savedInstanceState: Bundle?) {
//        findViewById<DropDownMenu>(R.id.dropdown_menu).setDropDownMenu("店内码",arrayListOf("店内码","条形码","货架号"),
//             TextView(this)
//        ) { p0, p1, p2, p3 ->
//
//        }

        mDatabind.viewmodel = mViewModel
        findViewById<LinearLayout>(R.id.ll_select).setOnClickListener{

            showSpinner(it,this,select ){position,text ->
                mViewModel.currentCodeText.value = select[position]
            }

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



    override fun layoutId(): Int  = R.layout.activity_rfid_query

    override fun setStatusBar() {
        initTitle(statusBarDarkFont =  false,biaoti = "查询",rightBitaoti = "配置价签"){
            showMessage("配置价签")
        }
    }

    override fun onReceiverData(data: String?) {

    }






    override fun createObserver() {
        requestQueryViewModel.queryData.observe(this, Observer {

            parseState(it,{list ->
                val stringList = arrayListOf<String>()
              for (item in list){
                  stringList.add(item.name)
              }
                select = stringList

                loadsir.showSuccess()
            },{

                loadsir.showError("加载失败")



            })
        })
    }
}