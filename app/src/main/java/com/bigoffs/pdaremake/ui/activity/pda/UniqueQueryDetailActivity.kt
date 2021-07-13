package com.bigoffs.pdaremake.ui.activity.pda

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.ext.*
import com.bigoffs.pdaremake.app.util.DeviceUtil
import com.bigoffs.pdaremake.databinding.ActivityPdaQueryDetailBinding
import com.bigoffs.pdaremake.ui.activity.rfid.FindEpcByBarcodeActivity
import com.bigoffs.pdaremake.ui.activity.rfid.FindEpcByUniqueActivity
import com.bigoffs.pdaremake.ui.customview.ExplainLinearLayout
import com.bigoffs.pdaremake.ui.customview.ImageLoader
import com.bigoffs.pdaremake.viewmodel.request.RequestQueryDetailViewModel
import com.bigoffs.pdaremake.viewmodel.state.QueryResultViewModel
import com.blankj.utilcode.util.ToastUtils
import com.kingja.loadsir.core.LoadService
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.util.ActivityMessenger
import java.lang.StringBuilder

class UniqueQueryDetailActivity : BaseActivity<QueryResultViewModel, ActivityPdaQueryDetailBinding>() {


    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    private val requestQueryDetailViewModel: RequestQueryDetailViewModel by viewModels()
    var uniqueCode = ""

    override fun layoutId(): Int = R.layout.activity_pda_query_detail
    lateinit var image: ImageView
    lateinit var ex: ExplainLinearLayout
    override fun setStatusBar() {
        initTitle(statusBarDarkFont = false, biaoti = "查询", rightBitaoti = "配置价签") {
            showMessage("配置价签")
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        uniqueCode = (intent.getStringExtra("unique"))+""
        mDatabind.vm = mViewModel
        mDatabind.click = ClickProxy()

//状态页配置
        loadsir = loadServiceInit(findViewById(R.id.ll_content)) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestQueryDetailViewModel.queryDetail("", uniqueCode)
        }

        //设置界面 加载中
        loadsir.showLoading()
        requestQueryDetailViewModel.queryDetail("", uniqueCode)

        image = findViewById(R.id.iv)
        ex = findViewById(R.id.ex)


        image.setOnClickListener() {

            XPopup.Builder(mContext)
                .isDestroyOnDismiss(true)
                .asImageViewer(
                    image,
                    mViewModel.queryDetail.value?.image,
                    true,
                    Color.parseColor("#f1f1f1"),
                    -1,
                    0,
                    false,
                    Color.BLACK,
                    ImageLoader
                )
                .show()

        }
        if (DeviceUtil.isRfidDevice()){
            findViewById<TextView>(R.id.tv_find_good).visibility = View.VISIBLE
        }
    }

    override fun createObserver() {
        requestQueryDetailViewModel.queryDetail.observe(this, Observer {

            parseState(it, { list ->

                loadsir.showSuccess()
                mViewModel.barcode.value = list.barcode
                mViewModel.uniqueCode.value = list.unique_code
                mViewModel.queryDetail.value = list
                mViewModel.salePrice.value = "销售价：${list.sale_price.toInt()/100}"
                val spec  = StringBuilder()
                spec.append("规格：")
                for (item in list.spec_list){
                    spec.append(item.spec_value+"/")
                }
                mViewModel.spec.value = spec.toString()
                ex.setContent(list)
                ex.foldOrUnfold()


            }, { exception ->
                ToastUtils.showShort(exception.errorMsg)
                loadsir.showError("加载失败")
                finish()

            }, {
                showLoading()
            })
        })
    }

    fun goFindSame(){
        ActivityMessenger.startActivity<PdaBarcodeQueryFindSamelActivity>(this,"unique" to mViewModel.uniqueCode.value)
    }
    fun goFindGood(){
        ActivityMessenger.startActivity<FindEpcByUniqueActivity>(this,
           "unique" to mViewModel.barcode.value ,"shelf_code" to mViewModel.shelfcode.value)
    }

    inner class ClickProxy{

        fun findSame(){
            goFindSame()
        }

        fun printTag(){

        }
        fun findGood(){
            goFindGood();
        }


    }

}