package com.bigoffs.pdaremake.app.weight.loadCallBack

import com.bigoffs.pdaremake.R
import com.kingja.loadsir.callback.Callback

class EmptyCallback : Callback() {
    override fun onCreateView(): Int  = R.layout.layout_empty
}