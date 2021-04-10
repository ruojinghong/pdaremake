package com.bigoffs.pdaremake.app.base

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.event.ScanViewModel
import com.bigoffs.pdaremake.app.scan.receiver.OnReceiverListener
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.getAppViewModel
import java.io.IOException

abstract class BaseScanActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseActivity<VM, DB>(),OnReceiverListener{

    val scanViewModel: ScanViewModel by viewModels()
    var mShouldPlayBeep:Boolean = true
    private var mMediaPlayer: MediaPlayer? =  MediaPlayer()
    override fun initView(savedInstanceState: Bundle?) {
        initBeep()
        scanViewModel.setOnReceiveListener(this)
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

    override fun onDestroy() {
        super.onDestroy()
        scanViewModel.scanSwitch(false)
    }

    override fun onResume() {
        super.onResume()
        scanViewModel.addScannerStatusListener()
    }

    override fun onPause() {
        super.onPause()
        scanViewModel.removeScannerStatusListener()
    }

}