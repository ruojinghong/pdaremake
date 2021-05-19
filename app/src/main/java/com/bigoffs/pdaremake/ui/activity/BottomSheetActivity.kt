package com.bigoffs.pdaremake.ui.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.ext.init
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.data.model.bean.NewInStoreErrorBean
import com.bigoffs.pdaremake.ui.adapter.NewInStoreErrorAdapter
import com.bigoffs.pdaremake.data.model.bean.NewInStoreNormalBean
import com.bigoffs.pdaremake.ui.adapter.NewInStoreNormalAdapter
import com.bigoffs.pdaremake.databinding.ActivityBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 *Author:sxy
 *Time:2021/5/19  10:39 上午
 *Desc:
 */
class BottomSheetActivity : BaseActivity<BaseViewModel,ActivityBottomSheetBinding>() {

    private val errorList = arrayListOf<NewInStoreErrorBean>()
    private val normalList = arrayListOf<NewInStoreNormalBean>()

    private val errorAdapter : NewInStoreErrorAdapter by lazy { NewInStoreErrorAdapter(errorList) }
    private val normalAdapter : NewInStoreNormalAdapter by lazy { NewInStoreNormalAdapter(normalList) }
    private lateinit var errorRecyclerView : RecyclerView
    private lateinit var normalRecyclerView : RecyclerView
    private lateinit var errorBottomsheetDialog : BottomSheetDialog
    private lateinit var normalBottomsheetDialog : BottomSheetDialog

    override fun layoutId(): Int  = R.layout.activity_bottom_sheet

    override fun setStatusBar() {
        initTitle(false, biaoti = "BOTTOMSHEET")
    }

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.click = ProxyClick()
        initBottomSheet()

        for (i in 1..100){
                errorAdapter.addData(NewInStoreErrorBean("1236264381"))
        }
        for (i in 1..100){
            normalAdapter.addData(NewInStoreNormalBean("1236264381","5437289","43758934",37))
        }


    }

    private fun initBottomSheet() {
        var view1= View.inflate(this,R.layout.bottom_newinstoreerror,null)
        errorRecyclerView  = view1.findViewById(R.id.dialog_recycleView)
        errorRecyclerView.init(LinearLayoutManager(this),errorAdapter)
        errorBottomsheetDialog = BottomSheetDialog(this,R.style.dialog)
        errorBottomsheetDialog.setContentView(view1)
        BottomSheetBehavior.from(view1.parent as View).peekHeight = getPeekHeight()

        var view2= View.inflate(this,R.layout.bottom_newinstoreerror,null)
        normalRecyclerView  = view2.findViewById(R.id.dialog_recycleView)
        normalRecyclerView.init(LinearLayoutManager(this),normalAdapter)
        normalBottomsheetDialog = BottomSheetDialog(this,R.style.dialog)
        normalBottomsheetDialog.setContentView(view2)
        BottomSheetBehavior.from(view2.parent as View).peekHeight = getPeekHeight()
    }

    inner class ProxyClick{
            fun openErrorBottomSheet(){
                errorBottomsheetDialog.show()
            }
        fun openNormalBottomSheet(){
            normalBottomsheetDialog.show()

        }

    }

    /**
     * 弹窗高度，默认为屏幕高度的四分之三
     * 子类可重写该方法返回peekHeight
     *
     * @return height
     */
    protected fun getPeekHeight(): Int {
        val peekHeight = resources.displayMetrics.heightPixels
        //设置弹窗高度为屏幕高度的3/4
        return peekHeight - peekHeight / 3
    }
}