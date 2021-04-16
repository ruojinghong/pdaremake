package com.bigoffs.pdaremake.app.ext

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.util.SettingUtil
import com.bigoffs.pdaremake.app.weight.loadCallBack.EmptyCallback
import com.bigoffs.pdaremake.app.weight.loadCallBack.ErrorCallback
import com.bigoffs.pdaremake.app.weight.loadCallBack.LoadingCallback
import com.bigoffs.pdaremake.data.model.bean.QueryType
import com.blankj.utilcode.util.ToastUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import com.lxj.xpopup.interfaces.OnSelectListener
import me.hgj.jetpackmvvm.base.appContext

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
        .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
        .asBottomList(
            "选择仓库", data,
            null, 2,false,listener
        , R.layout._xpopup_bottom_impl_list,R.layout._xpopup_adapter_text_match)
        .show()
}

fun Fragment.showBottomSheedList( context: Context,data : Array<String>,listener: OnSelectListener){
    XPopup.Builder(context)
        .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
        .asBottomList(
            "选择仓库", data,
            null, 2,false,listener
            , R.layout._xpopup_bottom_impl_list,R.layout._xpopup_adapter_text_match)
        .show()
}

fun AppCompatActivity.showSpinner(v: View, context: Context, data : List<String>, listener: OnSelectListener){
    XPopup.Builder(context).popupAnimation(PopupAnimation.ScrollAlphaFromTop)
        .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
        .hasShadowBg(false)
        .atView(v)
        .asAttachList(data.toTypedArray(),null,listener,R.layout._xpopup_down_impl_list,R.layout._xpopup_adapter_text_match).show()


}


fun loadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
    val loadsir = LoadSir.getDefault().register(view) {
        //点击重试时触发的操作
        callback.invoke()
    }
    loadsir.showSuccess()
    SettingUtil.setLoadingColor(SettingUtil.getColor(appContext), loadsir)
    return loadsir
}


fun LoadService<*>.setErrorText(message: String) {
    if (message.isNotEmpty()) {
        this.setCallBack(ErrorCallback::class.java) { _, view ->
            view.findViewById<TextView>(R.id.error_text).text = message
        }
    }
}

/**
 * 设置错误布局
 * @param message 错误布局显示的提示内容
 */
fun LoadService<*>.showError(message: String = "") {
    this.setErrorText(message)
    this.showCallback(ErrorCallback::class.java)
}

/**
 * 设置空布局
 */
fun LoadService<*>.showEmpty() {
    this.showCallback(EmptyCallback::class.java)
}

/**
 * 设置加载中
 */
fun LoadService<*>.showLoading() {
    this.showCallback(LoadingCallback::class.java)
}


