package com.bigoffs.pdaremake.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.ext.DeviceType
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.app.ext.showMessage
import com.bigoffs.pdaremake.app.service.ScanServiceControl
import com.bigoffs.pdaremake.databinding.ActivityMainBinding
import com.bigoffs.pdaremake.viewmodel.state.MainViewModel
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.LogUtils

class MainActivity : BaseScanActivity<MainViewModel, ActivityMainBinding>() {
    override fun layoutId(): Int  = R.layout.activity_new_main
    override fun onReceiverData(data: String) {

        showMessage(data)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        when(DeviceUtils.getModel()){
            DeviceType.RFID.model -> {
                initRfid()
            }
            DeviceType.PDA.model -> {
                initN5()
            }
                else ->{
                    showMessage("这是OTHER")
                }


        }

    }


    /**
    * 这是普通PDA
    */
    fun initN5(){
        val sp = getSharedPreferences("ScanSettings", MODE_PRIVATE)
        // 获取扫描声音
        // 获取扫描声音
        val scanVoice = sp.getBoolean("voice", true)
        // 获取扫描震动
        // 获取扫描震动
        val scanVibrate = sp.getBoolean("vibrate", true)
        val intent = Intent("com.android.scanner.service_settings")
        // 条码广播名称
        // 条码广播名称
        intent.putExtra("action_barcode_broadcast", "com.android.server.scannerservice.broadcastokbuy")
        // 条码键值名称
        // 条码键值名称
        intent.putExtra("key_barcode_broadcast", "scan_code")
        // 连续扫描
        // 连续扫描
        intent.putExtra("scan_continue", false)
        // 扫描声音
        // 扫描声音
        intent.putExtra("sound_play", scanVoice)
        // 扫描震动
        // 扫描震动
        intent.putExtra("viberate", scanVibrate)
        // 扫描模式：广播
        // 扫描模式：广播
        intent.putExtra("barcode_send_mode", "BROADCAST")
        // 条码结束符
        // 条码结束符
        intent.putExtra("endchar", "NONE")
        sendBroadcast(intent)
    }

    /**
     *这是RFID
     */
    fun initRfid(){
        val scanService = ScanServiceControl.getScanService()
        scanService.init(this)
        Thread{
            LogUtils.d("rfid初始化成功+++++++++++" + scanService.openReader())
        }
    }

    override fun setStatusBar() {
       initTitle(biaotiBg = Color.BLACK,titleBg = Color.WHITE,showLeftBtn = View.GONE,showRightBtn = View.VISIBLE,biaoti = "首页"){

       }
    }
}