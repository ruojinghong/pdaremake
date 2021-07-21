package com.bigoffs.pdaremake.ui.activity.pda

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.ext.*
import com.bigoffs.pdaremake.app.util.DeviceUtil
import com.bigoffs.pdaremake.data.model.bean.StockMap
import com.bigoffs.pdaremake.databinding.ActivityBarcodeQueryDetailBinding
import com.bigoffs.pdaremake.ui.activity.rfid.FindEpcByBarcodeActivity
import com.bigoffs.pdaremake.ui.adapter.BarcodeDetailAdapter
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
import java.math.BigDecimal
import java.text.DecimalFormat

class BarCodeQueryDetailActivity : BaseActivity<QueryResultViewModel, ActivityBarcodeQueryDetailBinding>() {


    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    private val requestQueryDetailViewModel: RequestQueryDetailViewModel by viewModels()
    var uniqueCode = ""

    override fun layoutId(): Int = R.layout.activity_barcode_query_detail
    lateinit var image: ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var ex: ExplainLinearLayout
    override fun setStatusBar() {
        initTitle(statusBarDarkFont = false, biaoti = "查询", rightBitaoti = "配置价签") {
            showMessage("配置价签")
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        uniqueCode = (intent.getStringExtra("barcode"))+""
        mDatabind.vm = mViewModel
        mDatabind.click = ClickProxy()

//状态页配置
        loadsir = loadServiceInit(findViewById(R.id.ll_content)) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestQueryDetailViewModel.queryBarcodeDetail(uniqueCode)
        }

        //设置界面 加载中
        loadsir.showLoading()
        requestQueryDetailViewModel.queryBarcodeDetail( uniqueCode)

        image = findViewById(R.id.iv)
        ex = findViewById(R.id.ex)
        recyclerView = findViewById(R.id.recyclerView)


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
        requestQueryDetailViewModel.barcodeDetail.observe(this, Observer {

            parseState(it, { list ->

                loadsir.showSuccess()
                mViewModel.barcode.value = list.barcode
                mViewModel.uniqueCode.value = list.unique_code
                mViewModel.shelfcode.value = list.shelf_code
                mViewModel.queryDetail.value = list
                mViewModel.stockNum.value = "库存：${list.stock_num}"
                val spec  = StringBuilder()
                spec.append("规格：")
                for (position in list.spec_list.indices){
                    if(list.spec_list.size == 1){
                        spec.append(list.spec_list[position].spec_value)
                    }else{
                        if(list.spec_list.size == position+1){
                            spec.append(list.spec_list[position].spec_value)
                        }else{
                            spec.append(list.spec_list[position].spec_value).append("/")
                        }
                    }
                }
                mViewModel.spec.value = spec.toString()
                mViewModel.salePrice.value = "销售价：${list.sale_price.toString().fen2yuan()}"
                recyclerView.init(LinearLayoutManager(mContext),BarcodeDetailAdapter(list.stock_map as ArrayList<StockMap>))
                ex.setContent(list)
                ex.foldOrUnfold()


            }, { exception ->
                ToastUtils.showShort(exception.msg)
                loadsir.showError("加载失败")
                finish()

            }, {
                showLoading()
            })
        })
    }

    fun goFindSame(){
        ActivityMessenger.startActivity<PdaBarcodeQueryFindSamelActivity>(this,"barcode" to mViewModel.barcode.value)
    }
    fun goFindGood(){
        ActivityMessenger.startActivity<FindEpcByBarcodeActivity>(this,
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