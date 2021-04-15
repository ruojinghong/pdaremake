package com.bigoffs.pdaremake.ui.activity.Rfid

import android.os.Bundle
import android.widget.LinearLayout
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseRfidFActivity
import com.bigoffs.pdaremake.app.event.RfidViewModel
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.app.ext.showMessage
import com.bigoffs.pdaremake.app.ext.showSpinner
import com.bigoffs.pdaremake.app.util.StatusBarUtil
import com.bigoffs.pdaremake.databinding.ActivityRfidQueryBinding
import com.gyf.immersionbar.ktx.immersionBar
import com.lxj.xpopup.interfaces.OnSelectListener

class RfidQueryActivity : BaseRfidFActivity<RfidViewModel,ActivityRfidQueryBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
                findViewById<LinearLayout>(R.id.ll_select).setOnClickListener{
                        showSpinner(it,this, arrayOf("店内码","条形码","货架号")
                        ) { position, text ->



                        }
                }
    }

    override fun onFinish(data: String) {

    }

    override fun readOrClose() {

    }

    override fun layoutId(): Int  = R.layout.activity_rfid_query

    override fun setStatusBar() {
        initTitle(statusBarDarkFont =  false,biaoti = "查询",rightBitaoti = "配置价签"){
            showMessage("配置价签")
        }

        immersionBar {
            //状态栏字体是深色，不写默认为亮色
            statusBarDarkFont(true)
        }
    }

    inner  class Click{

        fun switchCodeType(){

        }


    }
}