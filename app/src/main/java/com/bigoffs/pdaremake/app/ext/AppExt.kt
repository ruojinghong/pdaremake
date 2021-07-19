package com.bigoffs.pdaremake.app.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.util.SettingUtil
import com.bigoffs.pdaremake.ui.customview.FreeArrowView
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.text.NumberFormat
import java.text.ParseException

/**
 * @author: sxy
 * @date: 2021/3/19
 * @description:
 */
/**
 * @param message 显示对话框的内容 必填项
 * @param title 显示对话框的标题 默认 温馨提示
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 *
 */
fun AppCompatActivity.showMessage(
    message: String,
    title: String = "温馨提示",
    positiveButtonText: String = "确定",
    positiveAction: () -> Unit = {},
    negativeButtonText: String = "",
    negativeAction: () -> Unit = {}
) {
    MaterialDialog(this)
        .cancelable(true)
        .lifecycleOwner(this)
        .show {
            title(text = title)
            message(text = message)
            positiveButton(text = positiveButtonText) {
                positiveAction.invoke()
            }
            if (negativeButtonText.isNotEmpty()) {
                negativeButton(text = negativeButtonText) {
                    negativeAction.invoke()
                }
            }
            getActionButton(WhichButton.POSITIVE).updateTextColor(SettingUtil.getColor(this@showMessage))
            getActionButton(WhichButton.NEGATIVE).updateTextColor(SettingUtil.getColor(this@showMessage))
        }
}

/**
 * @param message 显示对话框的内容 必填项
 * @param title 显示对话框的标题 默认 温馨提示
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 */
fun Fragment.showMessage(
    message: String,
    title: String = "温馨提示",
    positiveButtonText: String = "确定",
    positiveAction: () -> Unit = {},
    negativeButtonText: String = "",
    negativeAction: () -> Unit = {}
) {
    activity?.let {
        MaterialDialog(it)
            .cancelable(true)
            .lifecycleOwner(viewLifecycleOwner)
            .show {
                title(text = title)
                message(text = message)
                positiveButton(text = positiveButtonText) {
                    positiveAction.invoke()
                }
                if (negativeButtonText.isNotEmpty()) {
                    negativeButton(text = negativeButtonText) {
                        negativeAction.invoke()
                    }
                }
                getActionButton(WhichButton.POSITIVE).updateTextColor(SettingUtil.getColor(it))
                getActionButton(WhichButton.NEGATIVE).updateTextColor(SettingUtil.getColor(it))
            }
    }
}

/**
 * 获取进程号对应的进程名
 *
 * @param pid 进程号
 * @return 进程名
 */
fun getProcessName(pid: Int): String? {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
        var processName = reader.readLine()
        if (!TextUtils.isEmpty(processName)) {
            processName = processName.trim { it <= ' ' }
        }
        return processName
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    } finally {
        try {
            reader?.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }

    }
    return null
}

fun List<*>?.isNull(): Boolean {
    return this?.isEmpty() ?: true
}

fun List<*>?.isNotNull(): Boolean {
    return this != null && this.isNotEmpty()
}

/**
 * 根据索引获取集合的child值
 * @receiver List<T>?
 * @param position Int
 * @return T?
 */
inline fun <reified T> List<T>?.getChild(position: Int): T? {
    //如果List为null 返回null
    return if (this == null) {
        null
    } else {
        //如果position大于集合的size 返回null
        if (position + 1 > this.size) {
            null
        } else {
            //返回正常数据
            this[position]
        }
    }
}
/**
 * 初始化titlebar
 */
fun AppCompatActivity.initTitle(statusBarDarkFont:Boolean = true,
                                titleBg: Int = Color.parseColor("#0033cc"),
                                biaotiBg: Int = Color.WHITE,
                                biaoti: String = "",
                                showRightBtn : Int = View.GONE,
                                showLeftBtn : Int = View.VISIBLE,
                                rightBitaoti : String ="",
                                onClickListener: View.OnClickListener? = null){
    var tvBar = findViewById<TextView>(R.id.tv_bar)
    var title = findViewById<ConstraintLayout>(R.id.common_title)
    var tvTitle = findViewById<TextView>(R.id.tv_title)
    var right_image_btn = findViewById<ImageButton>(R.id.iv_right_btn)
    var leftArrow = findViewById<FreeArrowView>(R.id.fa_left)
    var rightTv = findViewById<TextView>(R.id.tv_right)
    immersionBar {
        //解决状态栏和布局重叠问题
        tvBar?.let {
            statusBarView(tvBar)
            it.setBackgroundColor(titleBg)
        }
        title?.let {
            it.setBackgroundColor(titleBg)
        }
        tvTitle?.let {
            it.setText(biaoti)
            it.setTextColor(biaotiBg)
        }
        right_image_btn?.let {

            it.visibility = showRightBtn
            it.setOnClickListener(onClickListener)
        }
        rightTv?.let {

            if(rightBitaoti != ""){
                rightTv.visibility = View.VISIBLE
                rightTv.setText(rightBitaoti)
                it.setOnClickListener(onClickListener)
            }
        }
        leftArrow?.let {
            it.setOnClickListener(object : View.OnClickListener{
                override fun onClick(p0: View?) {
                   finish()
                }
            })
            it.visibility = showLeftBtn
        }
        immersionBar {
            //状态栏字体是深色，不写默认为亮色
            statusBarDarkFont(statusBarDarkFont)
        }

    }
}

inline fun <reified T : Activity> Activity.startActivity(context: Context) {
    startActivity(Intent(context, T::class.java))
}

fun  Int.fenToYuan ( amount:String):String{
    var format = NumberFormat.getInstance()
    try{
        var number = format.parse(amount)
        var temp = number.toDouble()/100.0;
        format.setGroupingUsed(false);
        // 设置返回的小数部分所允许的最大位数
        format.setMaximumFractionDigits(2);
        return  format.format(temp);
    } catch ( e : ParseException){
        e.printStackTrace();
    }
    return amount;
}


