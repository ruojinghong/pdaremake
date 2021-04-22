package com.bigoffs.pdaremake.ui.activity

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseActivity
import com.bigoffs.pdaremake.app.base.BaseRfidFActivity
import com.bigoffs.pdaremake.app.ext.showBottomSheedList
import com.bigoffs.pdaremake.app.ext.showMessage
import com.bigoffs.pdaremake.app.util.CacheUtil
import com.bigoffs.pdaremake.databinding.ActivityLoginBinding
import com.bigoffs.pdaremake.databinding.FragmentLoginBinding
import com.bigoffs.pdaremake.viewmodel.request.RequestLoginViewModel
import com.bigoffs.pdaremake.viewmodel.state.LoginRegisterViewModel
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import com.lxj.xpopup.interfaces.OnSelectListener
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.util.ActivityMessenger

class LoginActivity : BaseActivity<LoginRegisterViewModel, ActivityLoginBinding>() {

    private val requestLoginViewModel: RequestLoginViewModel by viewModels()
    override fun layoutId(): Int = R.layout.activity_login


    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel

        mDatabind.click = Click()
    }

    override fun createObserver() {
        requestLoginViewModel.loginResult.observe(this, Observer { resultState ->
            parseState(resultState, { userInfo ->

//                me.hgj.jetpackmvvm.ext.nav().navigateUp()
                appViewModel.user.value = userInfo
                CacheUtil.setUser(appViewModel.user.value)
                requestLoginViewModel.getHouseId()
            }, { appException ->
                //登录失败
                showMessage(appException.errorMsg)
            }, {

                showLoading()
            })
        })

        requestLoginViewModel.houseResult.observe(this, { resultState ->
            parseState(resultState, { houseList ->

                val arrayString = arrayListOf<String>()
                for(house in houseList){
                    arrayString.add(house.name)
                }

                showBottomSheedList(
                    mContext, arrayString.toTypedArray()
                ) { position, text ->
                    //登录成功 通知账户数据发生改变鸟

                    CacheUtil.setHouse(houseList[position])
                    CacheUtil.setIsLogin(true)
                    ActivityMessenger.startActivity<MainActivity>(this)
                }
            }, { appException ->
                //登录失败
                showMessage(appException.errorMsg)
            }, {

                showLoading()
            }
            )
        })

    }

//    override fun onFinish(data: String) {
//
//        showLoading(data)
//        rfidViewModel.stopReadRfid()
//    }

    inner class Click {

        fun login() {
//            requestLoginViewModel.getDetail("1")

            when {
                mViewModel.username.value.isEmpty() -> showMessage("请填写账号")
                mViewModel.password.value.isEmpty() -> showMessage("请填写密码")
                else -> requestLoginViewModel.loginReq(
                    mViewModel.username.value,
                    mViewModel.password.value
                )
            }

        }

    }

//    override fun readOrClose() {
//      rfidViewModel.startReadRfid()
//    }

    override fun setStatusBar() {
        immersionBar {
            //解决状态栏和布局重叠问题
            statusBarView(findViewById(R.id.bar))
            //状态栏字体是深色，不写默认为亮色
            statusBarDarkFont(true)
        }
    }


}