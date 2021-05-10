package com.bigoffs.pdaremake.ui.fragment

import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseFragment
import com.bigoffs.pdaremake.app.base.BaseInStoreFragment
import com.bigoffs.pdaremake.app.ext.*
import com.bigoffs.pdaremake.app.weight.loadCallBack.ErrorCallback
import com.bigoffs.pdaremake.databinding.FragmentPdaNewinstoreBinding
import com.bigoffs.pdaremake.ui.activity.NewInStoreDetailActivity
import com.bigoffs.pdaremake.ui.adapter.InstoreAdapter
import com.bigoffs.pdaremake.viewmodel.request.RequestInstoreViewmodel
import com.bigoffs.pdaremake.viewmodel.state.PdaNewInstoreViewModel
import com.bigoffs.pdaremake.viewmodel.state.RfidQueryViewModel
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.OnItemClickListener
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.SpaceItemDecoration
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.util.ActivityMessenger

/**
 *User:Kirito
 *Time:2021/5/5  23:40
 *Desc:新品入库
 */
class PdaNewInstoreFragment : BaseInStoreFragment<PdaNewInstoreViewModel,FragmentPdaNewinstoreBinding>(){


    var select = arrayListOf<String>("条形码", "供应商", "入库批次")

    lateinit var swipeRefresh:SwipeRefreshLayout;
    lateinit var recyclerView:SwipeRecyclerView;
    lateinit var floatbtn:FloatingActionButton;


    //适配器
    private val articleAdapter: InstoreAdapter by lazy { InstoreAdapter(arrayListOf(),1) }

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    val requestInstoreViewmodel :RequestInstoreViewmodel  by viewModels()



    override fun layoutId(): Int  = R.layout.fragment_pda_newinstore

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.viewmodel = mViewModel

        swipeRefresh = mDatabind.root.findViewById(R.id.swipeRefresh);
        recyclerView = mDatabind.root.findViewById(R.id.recyclerView);
        floatbtn = mDatabind.root.findViewById(R.id.floatbtn);

        mDatabind.root.findViewById<LinearLayout>(R.id.ll_select_new)?.setOnClickListener {

            context?.let { it1 ->
                showSpinner(it, it1, select) { position, text ->
                    setCurrentType(position)
                }
            }

        }

        mViewModel.currentCodeText.value = select[0]

        //状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestInstoreViewmodel.getNewInstoreList("","","",true)
        }

        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
                requestInstoreViewmodel.getNewInstoreList("","","",false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatbtn)
        }

        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestInstoreViewmodel.getNewInstoreList("","","",true)
        }
        articleAdapter.addChildClickViewIds(R.id.tv_go_detail)
        articleAdapter.setOnItemChildClickListener{adapter, view, position ->
                when(view.id){
                        R.id.tv_go_detail ->{
                            ActivityMessenger.startActivity<NewInStoreDetailActivity>(requireActivity())
                        }
                }
        }

    }


    fun setCurrentType(position: Int) {
        if (position >= select.size) {
            return
        }
        mViewModel.currentCodeText.value = select[position]

    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        loadsir.showLoading()
        requestInstoreViewmodel.getNewInstoreList("","","",true)

    }

    override fun createObserver() {
        requestInstoreViewmodel.data.observe(viewLifecycleOwner, Observer {resultState ->
            parseState(resultState,{

                swipeRefresh.isRefreshing = false
                //请求成功，页码+1
                if(requestInstoreViewmodel.pageNo == 1){
                    //当前数据是refresh
                    requestInstoreViewmodel.pageNo++
                    //如果是刷新的，有数据
                    articleAdapter.setList(it.list)
                    if(articleAdapter.data.size > 0){
                        loadsir.showSuccess()
                    }else{
                        loadsir.showEmpty()
                    }
                }else{
                    loadsir.showSuccess()
                    articleAdapter.addData(it.list)
                    recyclerView.loadMoreFinish(it.list.isEmpty(),it.list.size < it.limit)

                }


            },{

                //这里代表请求失败
                swipeRefresh.isRefreshing = false
                if (articleAdapter.data.size == 0) {
                    //如果适配器数据没有值，则显示错误界面，并提示错误信息
                    loadsir.setErrorText(it.errorMsg)
                    loadsir.showCallback(ErrorCallback::class.java)
                } else {
                    recyclerView.loadMoreError(0, it.errorMsg)
                }
            })
//            loadListData(it, articleAdapter, loadsir, recyclerView,swipeRefresh)
        })
    }

    override fun goInStoreDetail(code: String) {

    }
}