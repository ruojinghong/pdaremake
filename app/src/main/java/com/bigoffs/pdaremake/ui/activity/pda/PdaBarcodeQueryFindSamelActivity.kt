package com.bigoffs.pdaremake.ui.activity.pda

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.ext.*
import com.bigoffs.pdaremake.data.model.bean.FindSame
import com.bigoffs.pdaremake.data.model.bean.ShelfCodeStockNum
import com.bigoffs.pdaremake.databinding.ActivityPdaBarcodeQueryFindSameBinding
import com.bigoffs.pdaremake.ui.adapter.FindGoodlAdapter
import com.bigoffs.pdaremake.ui.customview.FindSameExplainLinearLayout
import com.bigoffs.pdaremake.ui.customview.ImageLoader
import com.bigoffs.pdaremake.viewmodel.request.RequestQueryPdaFindSameViewModel
import com.bigoffs.pdaremake.viewmodel.state.QueryPdaUniqueFindSameViewModel
import com.blankj.utilcode.util.ToastUtils
import com.kingja.loadsir.core.LoadService
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.ext.parseState
import java.math.BigDecimal
import java.text.DecimalFormat

class PdaBarcodeQueryFindSamelActivity :
    BaseActivity<QueryPdaUniqueFindSameViewModel, ActivityPdaBarcodeQueryFindSameBinding>() {


    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    private val requestViewModel: RequestQueryPdaFindSameViewModel by viewModels()
    var uniqueCode :String? = null
    var barcode :String? = null
    var spu_no :String? = null

    override fun layoutId(): Int = R.layout.activity_pda_barcode_query_find_same
    lateinit var image: ImageView
    lateinit var ex: FindSameExplainLinearLayout
    lateinit var recyclerView: RecyclerView
    override fun setStatusBar() {
        initTitle(statusBarDarkFont = false, biaoti = "查询") {

        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        uniqueCode = intent.getStringExtra("unique")
        barcode = intent.getStringExtra("barcode")
        spu_no = intent.getStringExtra("spu_no")
        mDatabind.vm = mViewModel

//状态页配置
        loadsir = loadServiceInit(findViewById(R.id.ll_content)) {
            //点击重试时触发的操作
            loadsir.showLoading()
            way()
        }

        //设置界面 加载中
        loadsir.showLoading()
        way()

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
    }

    override fun createObserver() {
        requestViewModel.same.observe(this, Observer {

            parseState(it, { same ->

                loadsir.showSuccess()

                mViewModel.uniqueCode.value = same.spu_name
                mViewModel.queryDetail.value = same
                mViewModel.stockNum.value = "库存：${same.stock_num}"
                mViewModel.salePrice.value = "销售价：${same.sale_price.toString().fen2yuan()}"
                recyclerView.init(
                    LinearLayoutManager(mContext),
                    FindGoodlAdapter(same.stock_map as ArrayList<ShelfCodeStockNum>)
                )
                ex.setContent(same)
                ex.foldOrUnfold()


            }, { exception ->
                ToastUtils.showShort(exception.msg)
                loadsir.showError(exception.msg + "")
                finish()

            }, {
                showLoading()
            })
        })
    }


    fun way() {
        uniqueCode?.let {
            requestViewModel.findSameByUnique(it)
        }
        barcode?.let {
            requestViewModel.findSameByBarcode(it)
        }
        spu_no?.let {
            requestViewModel.findSameBySpuNo(it)
        }




    }

}