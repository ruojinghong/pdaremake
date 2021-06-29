package com.bigoffs.pdaremake.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseRfidFActivity
import com.bigoffs.pdaremake.app.event.RfidViewModel
import com.bigoffs.pdaremake.app.ext.addOnNoneEditorActionListener
import com.bigoffs.pdaremake.app.ext.init
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.app.util.CacheUtil
import com.bigoffs.pdaremake.data.model.bean.StocktakingListBean
import com.bigoffs.pdaremake.databinding.ActivityRfidStocktakingBinding
import com.bigoffs.pdaremake.databinding.ActivityRfidTallyBinding
import com.bigoffs.pdaremake.ui.adapter.NewInStoreErrorAdapter
import com.bigoffs.pdaremake.ui.adapter.NewInStoreNormalBarcodeAndUniqueAdapter
import com.bigoffs.pdaremake.ui.dialog.HintDialog
import com.bigoffs.pdaremake.viewmodel.request.RequestStocktakingViewModel
import com.bigoffs.pdaremake.viewmodel.state.MainViewModel
import com.bigoffs.pdaremake.viewmodel.state.StocktakingViewModel
import com.bigoffs.pdaremake.viewmodel.state.TallyViewModel
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.hgj.jetpackmvvm.ext.parseState

/**
 *User:Kirito
 *Time:2021/6/15  23:05
 *Desc: 盘点Activity
 */
class RfidStocktakingActivityActivity :
    BaseRfidFActivity<StocktakingViewModel, ActivityRfidStocktakingBinding>() {


    private var data: StocktakingListBean? = null
    val requestStocktakingViewModel: RequestStocktakingViewModel by viewModels()

    override fun initScan() {
        rfidViewModel.initData()
        rfidViewModel.setReadDataModel(0)
        rfidViewModel.setMode(1)
        rfidViewModel.setCurrentSetting(RfidViewModel.Setting.stockRead)
        rfidViewModel.setListenerProtectModel(this)
    }

    fun readOrClose(view: View) {
        if (mDatabind.scanDesc.text == "点击扫描") {
            rfidViewModel.startReadRfid()
            mDatabind.rlScan.setBackgroundResource(R.mipmap.scan_open)
            //                mActivity.tbCommon.setVisibility(View.INVISIBLE);
            mDatabind.scanDesc.text = "点击关闭"
        } else {
            rfidViewModel.stopReadRfid()
            mDatabind.rlScan.setBackgroundResource(R.mipmap.scan_close)
            //            mActivity.tbCommon.setVisibility(View.VISIBLE);
            mDatabind.scanDesc.text = "点击扫描"
            checkEpcCodes()

        }
    }

    override fun layoutId(): Int = R.layout.activity_rfid_stocktaking

    override fun setStatusBar() {
        initTitle(false, biaoti = "盘点")
    }


    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.stocktakingid.value = ""
        mViewModel.scanNum.value = 0
        data = intent.getParcelableExtra<StocktakingListBean>("data")

        mViewModel.stocktakingid.value = "盘点ID:${data?.id ?: ""}"
        data?.id?.let { requestStocktakingViewModel.getEpcSysData(it) }
    }

    override fun onFinish(data: String) {
//        if(mViewModel.netSet.contains(data)){
//            mViewModel.normalList.add(data)
//        }else{
//            mViewModel.errorList.add(data)
//        }
//        mViewModel.scanNum.value = mViewModel.normalList.size
//        mViewModel.errorNum.value =mViewModel.errorList.size
        mViewModel.mapNum.value =rfidViewModel.map.size
        mViewModel.oneScanList.add(data);
    }

    private fun onReceiverData(data: String) {


    }

    override fun createObserver() {
        super.createObserver()
        requestStocktakingViewModel.systemList.observe(this, { resultState ->
            parseState(resultState, {
                for (i in 1..100000){
                    mViewModel.netSet.add(i.toString())
                }
                it.sys_data.forEach { epc ->
                    mViewModel.netSet.add(epc)
                }
            mViewModel.taskNum.value = mViewModel.netSet.size

            }, {
                ToastUtils.showShort(it.errorMsg)
                finish()
            })
        })

        requestStocktakingViewModel.uploadResult.observe(this ,{resultState ->
            parseState(resultState,{
                ToastUtils.showShort("上传成功")
                finish()
            },{
                ToastUtils.showShort(it.errorMsg)
            })
        })

        requestStocktakingViewModel.checkResult.observe(this,{resultState ->
            parseState(resultState,{
                     mViewModel.normalList.addAll(it.normal.list)
                    mViewModel.errorList.addAll(it.abnormal.list)
                    mViewModel.oneScanList.clear()
                    updateAllNum()
            },{
                beep()
                ToastUtils.showShort(it.message)
            })


        })
    }

    inner class ProxyClick {

        fun clear() {
            showClearDialog()

        }

        fun translate() {
            showUploadDialog()

        }

        fun cancel() {
            ToastUtils.showShort("取消录入")
        }
    }

    private fun updateAllNum() {
        mViewModel.scanNum.value = mViewModel.normalList.size
        mViewModel.errorNum.value = mViewModel.errorList.size
        mViewModel.mapNum.value = rfidViewModel.map.size

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
        if (mDatabind.scanDesc.text.toString() == "停止扫描") {
            readOrClose()
        }
    }

    private fun checkEpcCodes(){
        data?.id?.let { requestStocktakingViewModel.checkEpcCodes(it,mViewModel.oneScanList) }
    }

    fun showClearDialog(){
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("确定重新采集？")
            .setLeftBtnText("取消")
            .setRightBtnText("确定")
            .setDialogListener(object : HintDialog.OnHintDialogListener{
                override fun onClickOk() {
                    rfidViewModel.initData()
                    mViewModel.normalList.clear()
                    mViewModel.errorList.clear()
                    mViewModel.netSet.clear()
                    mViewModel.oneScanList.clear()
                    updateAllNum()
                }

                override fun onClickCancel() {

                }

                override fun onClickOther() {

                }
            }).show()
    }

    fun showUploadDialog(){
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("确定上传数据？")
            .setLeftBtnText("取消")
            .setRightBtnText("确定")
            .setDialogListener(object : HintDialog.OnHintDialogListener{
                override fun onClickOk() {
                    data?.id?.let { CacheUtil.getUser()?.userInfo?.uid?.let { it1 ->
                        requestStocktakingViewModel.uploadStData(it,
                            it1,mViewModel.normalList
                        )
                    } }
                }

                override fun onClickCancel() {

                }

                override fun onClickOther() {

                }
            }).show()
    }
}