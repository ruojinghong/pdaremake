package com.bigoffs.pdaremake.ui.activity.Rfid

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseRfidFActivity
import com.bigoffs.pdaremake.app.event.RfidViewModel
import com.bigoffs.pdaremake.app.ext.*
import com.bigoffs.pdaremake.app.util.StatusBarUtil
import com.bigoffs.pdaremake.data.model.bean.QueryType
import com.bigoffs.pdaremake.databinding.ActivityRfidQueryBinding
import com.bigoffs.pdaremake.ui.activity.pda.PdaQueryDetailActivity
import com.bigoffs.pdaremake.ui.customview.DropDownMenu
import com.bigoffs.pdaremake.viewmodel.request.RequestQueryViewModel
import com.bigoffs.pdaremake.viewmodel.state.RfidQueryViewModel
import com.gyf.immersionbar.ktx.immersionBar
import com.kingja.loadsir.core.LoadService
import com.lxj.xpopup.interfaces.OnSelectListener
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.util.ActivityMessenger

class RfidQueryActivity : BaseRfidFActivity<RfidQueryViewModel,ActivityRfidQueryBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    var select = arrayListOf<String>("店内码", "条形码", "货架")
    var currentType = "";
    var currentList = arrayListOf<QueryType>();

    private val requestQueryViewModel: RequestQueryViewModel by viewModels()
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        findViewById<LinearLayout>(R.id.ll_select).setOnClickListener {

            showSpinner(it, this, select) { position, text ->
                setCurrentType(position)
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

    override fun onFinish(data: String) {
        when( mViewModel.currentCodeText.value){
            "店内码" ->{
                ActivityMessenger.startActivity<PdaQueryDetailActivity>(this,"unique" to data)
            }


        }
    }

    override fun readOrClose() {
        rfidViewModel.startReadRfid()
    }

    override fun layoutId(): Int  = R.layout.activity_rfid_query

    override fun setStatusBar() {
        initTitle(statusBarDarkFont =  false,biaoti = "查询",rightBitaoti = "配置价签"){
            showMessage("配置价签")
        }

    }

    inner  class Click{

        fun switchCodeType(){

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
        currentType = currentList.get(position).type
        mViewModel.currentCodeText.value = currentList.get(position).name

    }
}