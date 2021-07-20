package com.bigoffs.pdaremake.ui.activity.rfid

import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseRfidFActivity
import com.bigoffs.pdaremake.app.event.RfidViewModel
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.app.ext.loadServiceInit
import com.bigoffs.pdaremake.app.ext.showError
import com.bigoffs.pdaremake.app.ext.showLoading
import com.bigoffs.pdaremake.app.util.SoundUtils
import com.bigoffs.pdaremake.databinding.ActivityFindGoosByEpcBinding
import com.bigoffs.pdaremake.viewmodel.request.RequestFindGoodByEpcViewModel
import com.bigoffs.pdaremake.viewmodel.state.FindGoodByEpcViewModel
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState

class FindEpcByBarcodeActivity : BaseRfidFActivity<FindGoodByEpcViewModel, ActivityFindGoosByEpcBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    private val requestViewModel: RequestFindGoodByEpcViewModel by viewModels()
    lateinit var btn_read_or_stop:TextView
    lateinit var tv_change:TextView
    lateinit var animation1:ImageView



    override fun readOrClose() {

        if (btn_read_or_stop.text.toString() == "停止扫描") {
            rfidViewModel.stopReadRfid()
            animation1.setImageResource(R.drawable.icon1)
            btn_read_or_stop.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
            //            mActivity.tbCommon.setVisibility(View.VISIBLE);
            btn_read_or_stop.text = "开始扫描"
        } else {
            rfidViewModel.setReadDataModel(1)
            rfidViewModel.startReadRfid()
            btn_read_or_stop.setBackgroundColor(Color.RED)
            //                mActivity.tbCommon.setVisibility(View.INVISIBLE);
            btn_read_or_stop.text = "停止扫描"
        }
        
    }

    override fun layoutId(): Int  = R.layout.activity_find_goos_by_epc

    override fun setStatusBar() {
       initTitle(false, biaoti = "找货")
    }

    override fun initView(savedInstanceState: Bundle?) {

        btn_read_or_stop = findViewById(R.id.btn_read_or_stop)
        tv_change = findViewById(R.id.tv_change)
        animation1 = findViewById(R.id.animation1)


        mViewModel.shelfCode.value = intent.getStringExtra("shelf_code")+""
        mViewModel.unique.value = intent.getStringExtra("unique")+""

        mDatabind.vm = mViewModel
        mDatabind.click = ClickProxy()

        //状态页配置
        loadsir = loadServiceInit(findViewById(R.id.ll_content)) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestViewModel.findGoodByEpc(mViewModel.unique.value, mViewModel.shelfCode.value)
        }
        loadsir.showLoading()
        requestViewModel.findGoodByEpc(mViewModel.unique.value, mViewModel.shelfCode.value)
    }

    override fun onFinish(data: String) {

        val split = data.split("@".toRegex()).toTypedArray()
        val epc: String = mViewModel.currentUnique.value

        if (epc == split[0]) {
            if (split.size == 1) {
                icon(0)
            } else {
                val rssi = java.lang.Double.valueOf(split[1]).toInt()
                icon(rssi)
            }
        }
    }

    override fun createObserver() {
        super.createObserver()
        requestViewModel.data.observe(this, Observer {
            parseState(it, { data ->
                //成功

                mViewModel.foodList.value = data
                if (data.epc_codes.isEmpty()){
                    ToastUtils.showShort("未找到匹配的EPC")
                    finish()
                }
                mViewModel.currentUnique.value = data.epc_codes[0]

            }, { excepion ->
                //失败
                loadsir.showError(excepion.errorLog + "")
            }, {
                //加载中
                showLoading("加载中")
            })


        })
    }

    override fun onPause() {
        super.onPause()
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0)
    }

    override fun onResume() {
        super.onResume()
        animation1.setImageResource(R.drawable.icon1)
    }


    fun icon(iconNum: Int) {
        when (iconNum) {
            0 -> {
                animation1.setImageResource(R.drawable.icon1)
                SoundUtils.playByVolume(1, 0.17f)
            }
            1 -> {
                animation1.setImageResource(R.drawable.icon2)
                SoundUtils.playByVolume(1, 0.33f)
            }
            2 -> {
                animation1.setImageResource(R.drawable.icon3)
                SoundUtils.playByVolume(1, 0.5f)
            }
            3 -> {
                animation1.setImageResource(R.drawable.icon4)
                SoundUtils.playByVolume(1, 0.66f)
            }
            4 -> {
                animation1.setImageResource(R.drawable.icon5)
                SoundUtils.playByVolume(1, 0.78f)
            }
            5 -> {
                animation1.setImageResource(R.drawable.icon6)
                SoundUtils.playByVolume(1, 1f)
            }
        }
    }

    inner class ClickProxy(){
        fun openOrClose(){
            readOrClose()
        }

        fun change(){
           nextEpc()
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        when (keyCode) {
            523 -> if (event!!.repeatCount == 0) {
                readOrClose()
                return true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                nextEpc()
                return true
            }
            KeyEvent.KEYCODE_DPAD_UP -> lastEpc()
        }


        return super.onKeyDown(keyCode, event)
    }

//    //移动到第n个
//    fun onChange(i: Int) {
//        currentPoint = i
//        if (tvIncode != null) tvIncode.setText("加载中...")
//        if (TextUtils.isEmpty(rfidList.get(currentPoint).getIncode())) {
//            listener.queryIncode(rfidList.get(i).getRfid())
//        } else {
//            setCurrenIncode(rfidList.get(i).getIncode())
//        }
//    }

    fun nextEpc() {
        if (mViewModel.foodList.value?.epc_codes?.size == 0) return
        mViewModel.currentPosition.value++
        if (mViewModel.currentPosition.value == mViewModel.foodList.value?.epc_codes?.size) {
            mViewModel.currentPosition.value = 0
        }
        mViewModel.currentUnique.value =
            mViewModel.foodList.value?.epc_codes?.get(mViewModel.currentPosition.value)+""
        animation1.setImageResource(R.drawable.icon1)
    }

    fun lastEpc() {
        if (mViewModel.foodList.value?.epc_codes?.size == 0) return
        mViewModel.currentPosition.value--
        if (mViewModel.currentPosition.value == -1) {
            mViewModel.currentPosition.value = mViewModel.foodList.value?.epc_codes!!.size - 1
        }
        mViewModel.currentUnique.value =
            mViewModel.foodList.value?.epc_codes?.get(mViewModel.currentPosition.value)+""
        animation1.setImageResource(R.drawable.icon1)
    }

    override fun initScan() {
        rfidViewModel.initData()
        rfidViewModel.setReadDataModel(0)
        rfidViewModel.setMode(1)
        rfidViewModel.setCurrentSetting(RfidViewModel.Setting.stockRead)
        rfidViewModel.setListener(this)
    }


}