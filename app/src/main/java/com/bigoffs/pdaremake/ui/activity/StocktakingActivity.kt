package com.bigoffs.pdaremake.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.ext.*
import com.bigoffs.pdaremake.app.util.CacheUtil
import com.bigoffs.pdaremake.app.util.SoundUtils.context
import com.bigoffs.pdaremake.data.model.bean.StocktakingListBean
import com.bigoffs.pdaremake.databinding.ActivityMainBinding
import com.bigoffs.pdaremake.databinding.ActivityStocktakingListBinding
import com.bigoffs.pdaremake.ui.adapter.InstoreAdapter
import com.bigoffs.pdaremake.ui.adapter.StocktakingAdapter
import com.bigoffs.pdaremake.viewmodel.request.RequestInstoreViewmodel
import com.bigoffs.pdaremake.viewmodel.request.RequestStocktakingViewModel
import com.bigoffs.pdaremake.viewmodel.state.StocktakingViewModel
import com.blankj.utilcode.util.ConvertUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.SpaceItemDecoration
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.util.ActivityMessenger

/**
 *User:Kirito
 *Time:2021/6/21  23:37
 *Desc:
 */
class StocktakingActivity : BaseActivity<StocktakingViewModel,ActivityStocktakingListBinding>() {


    lateinit var swipeRefresh: SwipeRefreshLayout;
    lateinit var recyclerView: SwipeRecyclerView;
    lateinit var floatbtn: FloatingActionButton;

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    val requestInstoreViewmodel : RequestStocktakingViewModel by viewModels()


    //适配器
    private val stocktakingAdapter: StocktakingAdapter by lazy { StocktakingAdapter(arrayListOf()) }
    override fun layoutId(): Int = R.layout.activity_stocktaking_list

    override fun setStatusBar() {
      initTitle(false,biaoti = "盘点")
    }

    override fun initView(savedInstanceState: Bundle?){

        mDatabind.viewmodel = mViewModel
        swipeRefresh = mDatabind.root.findViewById(R.id.swipeRefresh);
        recyclerView = mDatabind.root.findViewById(R.id.recyclerView);
        floatbtn = mDatabind.root.findViewById(R.id.floatbtn);


        //状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestInstoreViewmodel.getStocktakingList(CacheUtil.getHouse()?.id.toString())
        }

        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), stocktakingAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
//                requestInstoreViewmodel.getStocktakingList(CacheUtil.getHouse()?.id.toString())
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatbtn)
        }

        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestInstoreViewmodel.getStocktakingList(CacheUtil.getHouse()?.id.toString())
        }
        stocktakingAdapter.addChildClickViewIds(R.id.tv_go_detail)
        stocktakingAdapter.setOnItemChildClickListener{adapter, view, position ->
                ActivityMessenger.startActivity<PdaUniqueStocktakingActivity>(context,Pair<String,StocktakingListBean>("data",stocktakingAdapter.data[position]))
        }



    }

    override fun onResume() {
        super.onResume()
        requestInstoreViewmodel.getStocktakingList(CacheUtil.getHouse()?.id.toString())
    }




    override fun createObserver() {
        requestInstoreViewmodel.data.observe(this, Observer { resultState ->
            parseState(resultState, {
                stocktakingAdapter.data.clear()
                stocktakingAdapter.setList(it.data)
                if(stocktakingAdapter.data.isEmpty()){
                    loadsir.showEmpty()
                }else{
                    loadsir.showSuccess()
                }
                swipeRefresh.isRefreshing = false
            }, {
                loadsir.showError(it.message+"")
            })
        })
    }


}