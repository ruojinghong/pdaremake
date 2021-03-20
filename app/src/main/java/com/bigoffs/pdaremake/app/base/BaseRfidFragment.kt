package com.bigoffs.pdaremake.app.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

 abstract class BaseRfidFragment<VM: BaseViewModel,DB:ViewDataBinding> : BaseFragment<VM,DB>() {



}