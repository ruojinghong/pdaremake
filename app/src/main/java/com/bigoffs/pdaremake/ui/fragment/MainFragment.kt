package com.bigoffs.pdaremake.ui.fragment

import android.os.Bundle
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseFragment
import com.bigoffs.pdaremake.databinding.FragmentMainBinding
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction

class MainFragment : BaseFragment<BaseViewModel,FragmentMainBinding>() {
    override fun layoutId(): Int  = R.layout.fragment_main

    override fun initView(savedInstanceState: Bundle?) {
        nav().navigateAction(R.id.action_mainfragment_to_loginfragment)
    }
}