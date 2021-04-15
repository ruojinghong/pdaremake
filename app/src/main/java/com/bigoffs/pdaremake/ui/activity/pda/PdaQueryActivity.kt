package com.bigoffs.pdaremake.ui.activity.pda

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseRfidFActivity
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.event.RfidViewModel
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.app.ext.showMessage
import com.bigoffs.pdaremake.app.ext.showSpinner
import com.bigoffs.pdaremake.app.util.StatusBarUtil
import com.bigoffs.pdaremake.databinding.ActivityRfidQueryBinding
import com.bigoffs.pdaremake.ui.customview.DropDownMenu
import com.bigoffs.pdaremake.viewmodel.state.RfidQueryViewModel

class PdaQueryActivity : BaseScanActivity<RfidQueryViewModel,ActivityRfidQueryBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
//        findViewById<DropDownMenu>(R.id.dropdown_menu).setDropDownMenu("店内码",arrayListOf("店内码","条形码","货架号"),
//             TextView(this)
//        ) { p0, p1, p2, p3 ->
//
//        }

        mDatabind.viewmodel = mViewModel
        findViewById<LinearLayout>(R.id.ll_select).setOnClickListener{

            showSpinner(it,this, arrayOf("店内码","条形码","货架")){position,text ->
                mViewModel.currentCodeText.value = "haha"
            }

        }
    }



    override fun layoutId(): Int  = R.layout.activity_rfid_query

    override fun setStatusBar() {
        initTitle(statusBarDarkFont =  false,biaoti = "查询",rightBitaoti = "配置价签"){
            showMessage("配置价签")
        }
    }

    override fun onReceiverData(data: String?) {

    }
}