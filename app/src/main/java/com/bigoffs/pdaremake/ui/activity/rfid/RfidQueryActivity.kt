package com.bigoffs.pdaremake.ui.activity.rfid

import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseRfidFActivity
import com.bigoffs.pdaremake.app.event.RfidViewModel
import com.bigoffs.pdaremake.app.ext.*
import com.bigoffs.pdaremake.data.model.bean.QueryType
import com.bigoffs.pdaremake.databinding.ActivityRfidQueryBinding
import com.bigoffs.pdaremake.ui.activity.pda.PdaQueryDetailActivity
import com.bigoffs.pdaremake.viewmodel.request.RequestQueryViewModel
import com.bigoffs.pdaremake.viewmodel.state.RfidQueryViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.util.ActivityMessenger
import me.hgj.jetpackmvvm.util.LogUtils

class RfidQueryActivity : BaseRfidFActivity<RfidQueryViewModel, ActivityRfidQueryBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    var select = arrayListOf<String>("店内码", "条形码", "货架")
    var currentType = "";
    var currentList = arrayListOf<QueryType>();
    var intercept = false
    private lateinit var btn_read_or_stop:TextView
    private val requestQueryViewModel: RequestQueryViewModel by viewModels()
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        mDatabind.click = Click()
        btn_read_or_stop = findViewById(R.id.btn_read_or_stop)
        findViewById<LinearLayout>(R.id.ll_select).setOnClickListener {

            showSpinner(it, this, select) { position, text ->
                setCurrentType(position)
            }

        }
        val sum = {x:Int,y:Int -> x+y}
        val finish = {onFinish("11")}


        findViewById<EditText>(R.id.common_et).addOnEditorActionListener{
            onFinish(it)
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

    override fun onFinish(data: String) {
        if (intercept) return
        com.blankj.utilcode.util.LogUtils.i("it---------$data")
        intercept = true
        rfidViewModel.stopReadRfid()
        when (mViewModel.currentCodeText.value) {
            "店内码" -> {
                ActivityMessenger.startActivity<PdaQueryDetailActivity>(this, "unique" to data)
            }
        }


    }

    override fun onResume() {
        super.onResume()
        initScan()
        intercept = false
    }

    override fun readOrClose() {
        if (btn_read_or_stop.text.toString() == "停止扫描") {
            rfidViewModel.stopReadRfid()
            btn_read_or_stop.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
            //            mActivity.tbCommon.setVisibility(View.VISIBLE);
            btn_read_or_stop.text = "开始扫描"
        } else {
            rfidViewModel.setReadDataModel(1)
            rfidViewModel.startReadRfid()
            btn_read_or_stop.setBackgroundColor(Color.RED)
            //                mActivity.tbCommon.setVisibility(View.INVISIBLE);
            btn_read_or_stop.text = "停止扫描"
        }
    }

    override fun layoutId(): Int = R.layout.activity_rfid_query

    override fun setStatusBar() {
        initTitle(statusBarDarkFont = false, biaoti = "查询", rightBitaoti = "配置价签") {
            showMessage("配置价签")
        }

    }

    inner class Click {

        fun switchCodeType() {
                readOrClose()
        }


    }

    override fun createObserver() {
        requestQueryViewModel.queryData.observe(this, Observer {

            parseState(it, { list ->
                currentList.addAll(list)
                loadsir.showSuccess()
                val stringList = arrayListOf<String>()
                for (item in list) {
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
        currentType = currentList.get(position).type
        mViewModel.currentCodeText.value = currentList.get(position).name

    }

    override fun onPause() {
        super.onPause()
        if (btn_read_or_stop.text.toString() == "停止扫描"){
            readOrClose()
        }
    }

    override fun initScan() {
        rfidViewModel.initData()
        rfidViewModel.setReadDataModel(0)
        rfidViewModel.setMode(1)
        rfidViewModel.setCurrentSetting(RfidViewModel.Setting.stockRead)
        rfidViewModel.setListener(this)
    }
}