package com.bigoffs.pdaremake.app.base

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.event.AppViewModel
import com.bigoffs.pdaremake.app.event.EventViewModel
import com.bigoffs.pdaremake.app.ext.dismissLoadingExt
import com.bigoffs.pdaremake.app.ext.showLoadingExt
import com.bigoffs.pdaremake.ui.activity.LoginActivity
import com.gyf.immersionbar.ktx.immersionBar
import me.hgj.jetpackmvvm.base.activity.BaseVmDbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.getAppViewModel
import me.hgj.jetpackmvvm.util.ActivityMessenger
import java.io.IOException

/**
 *  描述　: 你项目中的Activity基类，在这里实现显示弹窗，吐司，还有加入自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmActivity例如
 * abstract class BaseActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {
 */
abstract class BaseActivity<VM : BaseViewModel,DB : ViewDataBinding> : BaseVmDbActivity<VM,DB>() {
    var mShouldPlayBeep:Boolean = true
    private var mMediaPlayer: MediaPlayer? =  MediaPlayer()

    val mContext:Context = this
    //Application全局的ViewModel，里面存放了一些账户信息，基本配置信息等
    val appViewModel: AppViewModel by lazy { getAppViewModel<AppViewModel>()}

    //Application全局的ViewModel，用于发送全局通知操作
    val eventViewModel: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    abstract override fun layoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar()
        initBeep()
    }

    private fun initBeep() {
        // 注册默认的音频频道
        setVolumeControlStream(AudioManager.STREAM_MUSIC)
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mShouldPlayBeep = audioManager.ringerMode == AudioManager.RINGER_MODE_NORMAL
        mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        // 监听事件，当播放完毕后，重新指向文件的头部，以便下次播放
        mMediaPlayer?.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            //                mMediaPlayer.seekTo(0);
        })
        // 设定数据源
        val file: AssetFileDescriptor = getResources().openRawResourceFd(R.raw.win_error)
        try {
            mMediaPlayer?.setDataSource(file.fileDescriptor, file.startOffset, file.length)
            file.close()
            mMediaPlayer?.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
            mMediaPlayer = null
        }
    }
    protected open fun beep() {
        if(mShouldPlayBeep){
            mMediaPlayer?.seekTo(0)
            mMediaPlayer?.start()
        }

    }



    abstract fun setStatusBar()

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 创建liveData观察者
     */
    override fun createObserver() {



    }

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        dismissLoadingExt()
    }

    /* *//**
     * 在任何情况下本来适配正常的布局突然出现适配失效，适配异常等问题，只要重写 Activity 的 getResources() 方法
     *//*
    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }*/
}