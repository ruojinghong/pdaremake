package com.bigoffs.pdaremake.ui.fragment

import android.os.Bundle
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseFragment
import com.bigoffs.pdaremake.databinding.FragmentLoginBinding
import com.bigoffs.pdaremake.viewmodel.state.LoginRegisterViewModel

class LoginFragment : BaseFragment<LoginRegisterViewModel,FragmentLoginBinding>(){
    override fun layoutId(): Int  = R.layout.fragment_login

    override fun initView(savedInstanceState: Bundle?) {
        showLoading("登录页")
    }
}