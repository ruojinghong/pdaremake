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
import com.bigoffs.pdaremake.databinding.ActivityRfidStocktakingBinding
import com.bigoffs.pdaremake.databinding.ActivityRfidTallyBinding
import com.bigoffs.pdaremake.ui.adapter.NewInStoreErrorAdapter
import com.bigoffs.pdaremake.ui.adapter.NewInStoreNormalBarcodeAndUniqueAdapter
import com.bigoffs.pdaremake.viewmodel.state.MainViewModel
import com.bigoffs.pdaremake.viewmodel.state.StocktakingViewModel
import com.bigoffs.pdaremake.viewmodel.state.TallyViewModel
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 *User:Kirito
 *Time:2021/6/15  23:05
 *Desc: 盘点Activity
 */
class RfidStocktakingActivityActivity : BaseRfidFActivity<StocktakingViewModel,ActivityRfidStocktakingBinding>() {






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

    override fun layoutId(): Int = R.layout.activity_rfid_stocktaking

    override fun setStatusBar() {
       initTitle(false,biaoti = "盘点")
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.stocktakingid.value = ""
        mViewModel.scanNum.value = 0


    }

    override fun onFinish(data: String) {
      mViewModel.scanNum.value =  rfidViewModel.num
    }

    private fun onReceiverData(data: String) {


    }

    inner class ProxyClick{

        fun clear(){
            ToastUtils.showShort("重新采集")
        }

        fun translate(){
            ToastUtils.showShort("上传盘点数据")
        }

        fun cancel(){
            ToastUtils.showShort("取消录入")
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

    override fun readOrClose() {

    }

    override fun onPause() {
        super.onPause()
        if (mDatabind.scanDesc.text.toString() == "停止扫描"){
            readOrClose()
        }
    }
}