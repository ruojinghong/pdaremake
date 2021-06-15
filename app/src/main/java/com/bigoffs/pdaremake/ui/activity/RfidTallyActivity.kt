package com.bigoffs.pdaremake.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseRfidFActivity
import com.bigoffs.pdaremake.app.event.RfidViewModel
import com.bigoffs.pdaremake.app.ext.addOnNoneEditorActionListener
import com.bigoffs.pdaremake.app.ext.init
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.databinding.ActivityRfidTallyBinding
import com.bigoffs.pdaremake.ui.adapter.NewInStoreErrorAdapter
import com.bigoffs.pdaremake.ui.adapter.NewInStoreNormalBarcodeAndUniqueAdapter
import com.bigoffs.pdaremake.viewmodel.state.MainViewModel
import com.bigoffs.pdaremake.viewmodel.state.TallyViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 *User:Kirito
 *Time:2021/6/15  23:05
 *Desc:
 */
class RfidTallyActivity : BaseRfidFActivity<TallyViewModel,ActivityRfidTallyBinding>() {


    private lateinit var errorRecyclerView: RecyclerView
    private lateinit var normalRecyclerView: RecyclerView
    private lateinit var errorBottomsheetDialog: BottomSheetDialog
    private lateinit var normalBottomsheetDialog: BottomSheetDialog

    private lateinit var errorBottomSheetNum: TextView
    private lateinit var normalBottomSheetNum: TextView

    private val errorAdapter: NewInStoreErrorAdapter by lazy { NewInStoreErrorAdapter(arrayListOf()) }
    private val normalAdapter: NewInStoreNormalBarcodeAndUniqueAdapter by lazy {
        NewInStoreNormalBarcodeAndUniqueAdapter(
            arrayListOf()
        )
    }

    override fun initScan() {
        rfidViewModel.initData()
        rfidViewModel.setReadDataModel(0)
        rfidViewModel.setMode(1)
        rfidViewModel.setCurrentSetting(RfidViewModel.Setting.stockRead)
        rfidViewModel.setListenerProtectModel(this)
    }

     fun readOrClose(view:View) {
        if(mDatabind.scanDesc.text == "点击扫描"){
            rfidViewModel.startReadRfid()
            mDatabind.rlScan.setBackgroundResource(R.mipmap.scan_open)
            //                mActivity.tbCommon.setVisibility(View.INVISIBLE);
            mDatabind.scanDesc.text = "点击关闭"
        }else{
            rfidViewModel.stopReadRfid()
            mDatabind.rlScan.setBackgroundResource(R.mipmap.scan_close)
            //            mActivity.tbCommon.setVisibility(View.VISIBLE);
            mDatabind.scanDesc.text = "点击扫描"

        }
    }

    override fun layoutId(): Int = R.layout.activity_rfid_tally

    override fun setStatusBar() {
       initTitle(false,biaoti = "理货")
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.currentShelf.value = ""
        mViewModel.scanNum.value = 0
        mDatabind.etShelf.requestFocus()
        initBottomSheet()
        mDatabind.etShelf.addOnNoneEditorActionListener {
            mDatabind.etShelf.setText("")
            onReceiverData(it)
        }
    }

    override fun onFinish(data: String) {
      mViewModel.scanNum.value =  rfidViewModel.num
    }

    private fun onReceiverData(data: String) {
        mViewModel.currentShelf.value = data

    }

    inner class ProxyClick{

        fun clear(){

        }

        fun translate(){

        }

        fun openErrorBottomSheet(){
            errorBottomsheetDialog.show()
        }
        fun openNormalBottomSheet(){
            normalBottomsheetDialog.show()
        }

    }

    private fun initBottomSheet() {
        var view1 = View.inflate(this, R.layout.bottom_newinstoreerror, null)
        errorRecyclerView = view1.findViewById(R.id.dialog_recycleView)
        errorRecyclerView.init(LinearLayoutManager(this), errorAdapter)
        errorBottomsheetDialog = BottomSheetDialog(this, R.style.dialog)
        errorBottomsheetDialog.setContentView(view1)
        view1.findViewById<ImageView>(R.id.iv_unfold).setOnClickListener {
            errorBottomsheetDialog.dismiss()
        }

        errorBottomSheetNum = view1.findViewById(R.id.tv_bottom_error_num)
        BottomSheetBehavior.from(view1.parent as View).peekHeight = getPeekHeight()

        var view2 = View.inflate(this, R.layout.bottom_newinstorenormal, null)
        normalRecyclerView = view2.findViewById(R.id.dialog_recycleView)
        view2.findViewById<ImageView>(R.id.iv_unfold).setOnClickListener {
            normalBottomsheetDialog.dismiss()
        }
        view2.findViewById<TextView>(R.id.title2).setText("商品编码")
        view2.findViewById<TextView>(R.id.title3).setText("数量")
        normalRecyclerView.init(LinearLayoutManager(this), normalAdapter)
        normalBottomsheetDialog = BottomSheetDialog(this, R.style.dialog)
        normalBottomsheetDialog.setContentView(view2)
        normalBottomSheetNum = view2.findViewById(R.id.tv_bottom_normal_num)
        BottomSheetBehavior.from(view2.parent as View).peekHeight = getPeekHeight()
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

    override fun readOrClose() {

    }

    override fun onPause() {
        super.onPause()
        if (mDatabind.scanDesc.text.toString() == "停止扫描"){
            readOrClose()
        }
    }
}