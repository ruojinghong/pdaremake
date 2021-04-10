package com.bigoffs.pdaremake.app.ext

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bigoffs.pdaremake.R
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener

/**
 * 隐藏软键盘
 */
fun hideSoftKeyboard(activity: Activity?) {
    activity?.let { act ->
        val view = act.currentFocus
        view?.let {
            val inputMethodManager =
                act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}
fun AppCompatActivity.showBottomSheedList( context: Context,data : Array<String>,listener: OnSelectListener){
    XPopup.Builder(context)
        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
        .asBottomList(
            "选择仓库", data,
            null, 2,false,listener
        , R.layout._xpopup_bottom_impl_list,R.layout._xpopup_adapter_text_match)
        .show()
}

fun Fragment.showBottomSheedList( context: Context,data : Array<String>,listener: OnSelectListener){
    XPopup.Builder(context)
        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
        .asBottomList(
            "选择仓库", data,
            null, 2,false,listener
            , R.layout._xpopup_bottom_impl_list,R.layout._xpopup_adapter_text_match)
        .show()
}


