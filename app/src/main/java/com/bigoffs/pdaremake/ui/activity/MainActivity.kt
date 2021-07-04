package com.bigoffs.pdaremake.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.ext.DeviceType
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.app.ext.showBottomSheedList
import com.bigoffs.pdaremake.app.ext.showMessage
import com.bigoffs.pdaremake.app.service.ScanServiceControl
import com.bigoffs.pdaremake.app.util.CacheUtil
import com.bigoffs.pdaremake.databinding.ActivityNewMainBinding
import com.bigoffs.pdaremake.ui.activity.rfid.RfidQueryActivity
import com.bigoffs.pdaremake.ui.activity.pda.PdaQueryActivity
import com.bigoffs.pdaremake.ui.activity.pda.InStoreActivity
import com.bigoffs.pdaremake.ui.activity.rfid.InStoreRfidActivity
import com.bigoffs.pdaremake.ui.adapter.MainAdapter
import com.bigoffs.pdaremake.ui.customview.SpaceItemDecoration
import com.bigoffs.pdaremake.viewmodel.state.MainViewModel
import com.blankj.utilcode.util.*
import me.hgj.jetpackmvvm.util.ActivityMessenger

class MainActivity : BaseActivity<MainViewModel, ActivityNewMainBinding>() {
    override fun layoutId(): Int = R.layout.activity_new_main

    //    override fun onReceiverData(data: String) {
//
//        showMessage(data)
//    }
    val adapter = MainAdapter(arrayListOf("入库作业", "拣货作业", "理货作业", "盘点作业", "查询货品", "采集工具"))

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.viewmodel = mViewModel
        CacheUtil.getUser()?.let {
            mViewModel.username.value = it.userInfo.nickname
        }

         CacheUtil.getHouse()?.let {
             mViewModel.depotname.value = it.name
         }
        mViewModel.taskNum.value = "unknow"

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

        mDatabind.tvTaskNum.setOnClickListener(){
            ActivityMessenger.startActivity<TaskActivity>(this)
        }
        requestPermission()
    }

    private fun requestPermission() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
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
        setting.setOnClickListener { showMessage("设置") }
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

                0 -> {
                    ActivityMessenger.startActivity<InStoreRfidActivity>(this)
                }
                2->{
                    showBottomSheedList(
                        mContext, arrayOf("店内码理货","条形码理货","RFID理货"),"选择理货类型"
                    ) { position, text ->
                        //登录成功 通知账户数据发生改变鸟

                       when(position){
                            0 ->{
                                ActivityMessenger.startActivity<RfidUniqueTallyActivity>(this)
                            }
                           1->{
                               ActivityMessenger.startActivity<RfidBarcodeTallyActivity>(this)
                           }
                           2 ->{
                               ActivityMessenger.startActivity<RfidTallyActivity>(this)
                           }

                       }

                    }
                }
                3->{
                    ActivityMessenger.startActivity<StocktakingActivity>(this)
                }

                4 -> {
                    startActivity(Intent(this, RfidQueryActivity::class.java))
                }
                5 -> {
                    startActivity(Intent(this, RfidCollectActivity::class.java))
                }
            }
        }
    }

    fun startPdaActivity() {
        adapter.setOnItemClickListener { adapter, view, position ->
            when (position) {

                0 -> {
                    ActivityMessenger.startActivity<InStoreActivity>(this)
                }
                2->{
                    showBottomSheedList(
                        mContext, arrayOf("店内码理货","条形码理货"),"选择理货类型"
                    ) { position, text ->
                        //登录成功 通知账户数据发生改变鸟

                        when(position){
                            0 ->{
                                ActivityMessenger.startActivity<PdaUniqueTallyActivity>(this)
                            }
                            1->{
                                ActivityMessenger.startActivity<PdaBarcodeTallyActivity>(this)
                            }
                            2 ->{

                            }

                        }
                    }
                }
                4 -> {
                    startActivity(Intent(this, PdaQueryActivity::class.java))
                }
                5 ->{
                    startActivity(Intent(this, PdaCollectActivity::class.java))
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