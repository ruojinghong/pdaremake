package com.bigoffs.pdaremake.app.event

import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Handler
import com.bigoffs.pdaremake.app.scan.receiver.CodeReceiver
import com.bigoffs.pdaremake.app.scan.receiver.OnReceiverListener
import com.zltd.industry.ScannerManager
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import java.io.UnsupportedEncodingException

/**
 * 红外扫描viewmodel
 */
class ScanViewModel : BaseViewModel(){


    private val mReceiver: CodeReceiver? = null
    private val mFilter: IntentFilter? = null

    private var mShouldPlayBeep = false
    private var mMediaPlayer: MediaPlayer? = null

    // N5扫描用到的类
    private lateinit var mScannerManager: ScannerManager
    private val mHandler = Handler()
    var onReceiverListener:OnReceiverListener? =null
    val mIScannerStatusListener:ScannerManager.IScannerStatusListener = object :ScannerManager.IScannerStatusListener {
        override fun onScannerStatusChanage(p0: Int) {
            TODO("Not yet implemented")
        }

        /**
         * 一旦扫描到结果就会调用
         */

        override fun onScannerResultChanage(p0: ByteArray) {
            mHandler.post {

                try {
                    // s = new String(arg0, "UTF-8");

                    //这里通过onScan方法来接受这个string
                    onScan(String(p0))
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
            }
        }

    }
    init {

        mScannerManager = ScannerManager.getInstance()
        scanSwitch(true)
        mScannerManager.scanMode = ScannerManager.SCAN_KEY_HOLD_MODE
        //2.可选. 设置当前调用方式通过API来调用
        mScannerManager.dataTransferType = ScannerManager.TRANSFER_BY_API
        //3.添加扫描的回调函数
        mScannerManager.addScannerStatusListener(mIScannerStatusListener)
    }



    /**
     * 对扫描接口的开启和关闭
     *
     * @param scanSwitch
     */
    fun scanSwitch(scanSwitch: Boolean) {
        //设置开关
        mScannerManager?.scannerEnable(scanSwitch)
    }

    fun setOnReceiveListener(listener: OnReceiverListener){
            onReceiverListener = listener
    }



    protected fun onScan(barcode: String) {


    }


    fun addScannerStatusListener(){

        //这里是为扫码管理类 添加一个 监听
        mScannerManager.addScannerStatusListener(mIScannerStatusListener)
    }

    fun removeScannerStatusListener(){

        //这里是为扫码管理类 添加一个 监听
        mScannerManager.removeScannerStatusListener(mIScannerStatusListener)
    }


}