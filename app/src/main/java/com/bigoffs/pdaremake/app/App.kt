package com.bigoffs.pdaremake.app

import androidx.multidex.MultiDex
import com.bigoffs.pdaremake.app.weight.loadCallBack.EmptyCallback
import com.bigoffs.pdaremake.app.weight.loadCallBack.ErrorCallback
import com.bigoffs.pdaremake.app.weight.loadCallBack.LoadingCallback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.BuildConfig
import com.tencent.mmkv.MMKV
import me.hgj.jetpackmvvm.base.BaseApp
import me.hgj.jetpackmvvm.ext.util.jetpackMvvmLog
import me.hgj.jetpackmvvm.ext.util.logd

class App : BaseApp(){
    companion object {
        lateinit var instance: App
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")
        //界面加载管理 初始化
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())//加载
            .addCallback(ErrorCallback())//错误
            .addCallback(EmptyCallback())//空
            .setDefaultCallback(SuccessCallback::class.java)//设置默认加载状态页
            .commit()
        //初始化Bugly
        val context = applicationContext
        // 获取当前包名
        val packageName = context.packageName
        // 获取当前进程名
//        val processName = getProcessName(android.os.Process.myPid())
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(context)
//        strategy.isUploadProcess = processName == null || processName == packageName
        // 初始化Bugly
        Bugly.init(context, if (BuildConfig.DEBUG) "xxx" else "a52f2b5ebb", BuildConfig.DEBUG)
        "".logd()
        jetpackMvvmLog = BuildConfig.DEBUG

        //防止项目崩溃，崩溃后打开错误界面
//        CaocConfig.Builder.create()
//            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
//            .enabled(true)//是否启用CustomActivityOnCrash崩溃拦截机制 必须启用！不然集成这个库干啥？？？
//            .showErrorDetails(false) //是否必须显示包含错误详细信息的按钮 default: true
//            .showRestartButton(false) //是否必须显示“重新启动应用程序”按钮或“关闭应用程序”按钮default: true
//            .logErrorOnRestart(false) //是否必须重新堆栈堆栈跟踪 default: true
//            .trackActivities(true) //是否必须跟踪用户访问的活动及其生命周期调用 default: false
//            .minTimeBetweenCrashesMs(2000) //应用程序崩溃之间必须经过的时间 default: 3000
//            .restartActivity(MainActivity::class.java) // 重启的activity
//            .errorActivity(ErrorActivity::class.java) //发生错误跳转的activity
//            .apply()
    }

}