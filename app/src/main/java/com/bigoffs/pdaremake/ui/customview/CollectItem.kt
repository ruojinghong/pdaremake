package com.bigoffs.pdaremake.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bigoffs.pdaremake.R

/**
 *User:Kirito
 *Time:2021/7/4  13:06
 *Desc:
 */
class CollectItem : LinearLayout {
    constructor(context: Context?) : super(context){
        initview(context)
    }

    private fun initview(context: Context?) {
        orientation = LinearLayout.VERTICAL
        addView(LayoutInflater.from(context).inflate(R.layout.pad_collect_item,null))

    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initview(context)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        initview(context)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes){
        initview(context)
    }
}