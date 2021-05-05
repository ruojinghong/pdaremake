package com.bigoffs.pdaremake.app.base

import androidx.databinding.ViewDataBinding
import me.hgj.jetpackmvvm.base.fragment.BaseVmDbFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 *User:Kirito
 *Time:2021/5/5  23:53
 *Desc:
 */
abstract class BaseInStoreFragment<VM: BaseViewModel,DB: ViewDataBinding> : BaseFragment<VM, DB>(){

    abstract fun goInStoreDetail()
}