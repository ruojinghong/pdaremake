package com.bigoffs.pdaremake.app.weight.loadCallBack

import android.content.Context
import android.view.View
import com.bigoffs.pdaremake.R
import com.kingja.loadsir.callback.Callback

class LoadingCallback : Callback() {
    override fun onCreateView(): Int  = R.layout.layout_loading

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return super.onReloadEvent(context, view)
    }
}