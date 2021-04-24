package com.bigoffs.pdaremake.ui.activity.pda

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.ext.*
import com.bigoffs.pdaremake.databinding.ActivityPdaQueryDetailBinding
import com.bigoffs.pdaremake.databinding.ActivityPdaUniqueQueryFindSameBinding
import com.bigoffs.pdaremake.ui.customview.ExplainLinearLayout
import com.bigoffs.pdaremake.ui.customview.ImageLoader
import com.bigoffs.pdaremake.viewmodel.request.RequestQueryDetailViewModel
import com.bigoffs.pdaremake.viewmodel.request.RequestQueryPdaFindSameViewModel
import com.bigoffs.pdaremake.viewmodel.state.QueryPdaUniqueFindSameViewModel
import com.bigoffs.pdaremake.viewmodel.state.QueryResultViewModel
import com.blankj.utilcode.util.ToastUtils
import com.kingja.loadsir.core.LoadService
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.ext.parseState

class PdaUniqueQueryFindSamelActivity : BaseActivity<QueryPdaUniqueFindSameViewModel, ActivityPdaUniqueQueryFindSameBinding>() {


    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    private val requestViewModel: RequestQueryPdaFindSameViewModel by viewModels()
    var uniqueCode = ""

    override fun layoutId(): Int = R.layout.activity_pda_unique_query_find_same
    lateinit var image: ImageView
    lateinit var ex: ExplainLinearLayout
    override fun setStatusBar() {
        initTitle(statusBarDarkFont = false, biaoti = "查询") {

        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        uniqueCode = (intent.getStringExtra("unique"))+""
        mDatabind.vm = mViewModel

//状态页配置
        loadsir = loadServiceInit(findViewById(R.id.ll_content)) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestViewModel.findSameByUnique( "")
        }

        //设置界面 加载中
        loadsir.showLoading()
        requestViewModel.findSameByUnique(uniqueCode)

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
    }

    override fun createObserver() {
        requestViewModel.same.observe(this, Observer {

            parseState(it, { same ->

                loadsir.showSuccess()

                mViewModel.uniqueCode.value = ""
                mViewModel.queryDetail.value = same
                ex.setContent(list)
                ex.foldOrUnfold()


            }, { exception ->
                ToastUtils.showShort(exception.message)
                loadsir.showError("加载失败")
                finish()

            }, {
                showLoading()
            })
        })
    }



}