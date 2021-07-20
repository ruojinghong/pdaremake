package com.bigoffs.pdaremake.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseFragment
import com.bigoffs.pdaremake.app.base.BaseRfidFragment
import com.bigoffs.pdaremake.app.ext.showMessage
import com.bigoffs.pdaremake.app.util.CacheUtil
import com.bigoffs.pdaremake.databinding.FragmentLoginBinding
import com.bigoffs.pdaremake.viewmodel.request.RequestLoginViewModel
import com.bigoffs.pdaremake.viewmodel.state.LoginRegisterViewModel
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.parseState

class LoginFragment : BaseRfidFragment<LoginRegisterViewModel,FragmentLoginBinding>(){

    private val requestLoginViewModel : RequestLoginViewModel   by  viewModels()
    override fun layoutId(): Int  = R.layout.fragment_login

    override fun initView(savedInstanceState: Bundle?) {
            mDatabind.viewmodel = mViewModel

            mDatabind.click = Click()
    }

    override fun createObserver() {
        requestLoginViewModel.loginResult.observe(viewLifecycleOwner, Observer { resultState ->
            parseState(resultState, { userInfo ->
                //登录成功 通知账户数据发生改变鸟
                CacheUtil.setUser(userInfo)
                CacheUtil.setIsLogin(true)
                appViewModel.user.value = userInfo
//                me.hgj.jetpackmvvm.ext.nav().navigateUp()
            }, { appException ->
                //登录失败
                showMessage(appException.msg)
            },{

                showLoading()
            })
        })
    }

    override fun onFinish(data: String) {
        showLoading(data)
    }

    inner  class Click{

        fun login(){
            when{
                mViewModel.username.value.isEmpty() -> showMessage("请填写账号")
                mViewModel.password.value.isEmpty() -> showMessage("请填写密码")
                else -> requestLoginViewModel.loginReq(
                    mViewModel.username.value,
                    mViewModel.password.value
                )
            }

        }

    }

    override fun initScan() {

    }

    override fun readOrClose() {

    }
}