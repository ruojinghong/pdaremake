package com.bigoffs.pdaremake.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.ext.DeviceType
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.app.ext.showMessage
import com.bigoffs.pdaremake.app.service.ScanServiceControl
import com.bigoffs.pdaremake.databinding.ActivityNewMainBinding
import com.bigoffs.pdaremake.ui.activity.rfid.RfidQueryActivity
import com.bigoffs.pdaremake.ui.activity.pda.PdaQueryActivity
import com.bigoffs.pdaremake.ui.adapter.MainAdapter
import com.bigoffs.pdaremake.ui.customview.SpaceItemDecoration
import com.bigoffs.pdaremake.viewmodel.state.MainViewModel
import com.blankj.utilcode.util.*

class MainActivity : BaseActivity<MainViewModel, ActivityNewMainBinding>() {
    override fun layoutId(): Int = R.layout.activity_new_main

    //    override fun onReceiverData(data: String) {
//
//        showMessage(data)
//    }
    val adapter = MainAdapter(arrayListOf("入库作业", "拣货作业", "理货作业", "盘点作业", "查询货品", "采集工具"))

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.viewmodel = mViewModel
        mViewModel.username.value = "王硕"
        mViewModel.depotname.value = "永旺国际商城仓"
        mViewModel.taskNum.value = "8"

        mDatabind.recycler.let {
            it.layoutManager = GridLayoutManager(mContext, 2)
            it.adapter = adapter
            it.addItemDecoration(SpaceItemDecoration(ConvertUtils.dp2px(11f)))
            adapter.notifyDataSetChanged()

        }
        when (DeviceUtils.getModel()) {
            DeviceType.RFID.model -> {
                initRfid()
            }
            DeviceType.PDA.model -> {
                initN5()
            }
            else -> {
                showMessage("这是OTHER")
            }


        }

    }


    /**
     * 这是普通PDA
     */
    fun initN5() {
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
        intent.putExtra(
            "action_barcode_broadcast",
            "com.android.server.scannerservice.broadcastokbuy"
        )
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
        startPdaActivity()


    }

    /**
     *这是RFID
     */
    fun initRfid() {
        val setting = findViewById<ImageButton>(R.id.iv_right_btn);
        setting.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                showMessage("设置")
            }

        })
        val scanService = ScanServiceControl.getScanService()
        scanService.init(this)

            scanService.openReader()

        startRfidActivity()


    }

    override fun setStatusBar() {
        initTitle(
            biaotiBg = Color.BLACK,
            titleBg = Color.WHITE,
            showLeftBtn = View.GONE,
            showRightBtn = View.VISIBLE,
            biaoti = "首页"
        ) {

        }
    }

    fun startRfidActivity() {
        adapter.setOnItemClickListener { adapter, view, position ->
            when (position) {
                4 -> {
                    startActivity(Intent(this, RfidQueryActivity::class.java))
                }
            }
        }
    }

    fun startPdaActivity() {
        adapter.setOnItemClickListener { adapter, view, position ->
            when (position) {
                4 -> {

                    startActivity(Intent(this, PdaQueryActivity::class.java))
                }
            }
        }
    }

    fun startPhoneActivity() {
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (position) {
                4 -> {


                }
            }
        }
    }
}