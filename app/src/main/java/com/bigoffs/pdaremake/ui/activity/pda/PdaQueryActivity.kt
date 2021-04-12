package com.bigoffs.pdaremake.ui.activity.pda

import android.os.Bundle
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseRfidFActivity
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.event.RfidViewModel
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.app.ext.showMessage
import com.bigoffs.pdaremake.app.util.StatusBarUtil
import com.bigoffs.pdaremake.databinding.ActivityRfidQueryBinding

class PdaQueryActivity : BaseScanActivity<RfidViewModel,ActivityRfidQueryBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

    }



    override fun layoutId(): Int  = R.layout.activity_rfid_query

    override fun setStatusBar() {
        initTitle(biaoti = "查询",rightBitaoti = "配置价签"){
            showMessage("配置价签")
        }
    }

    override fun onReceiverData(data: String?) {

    }
}