package com.bigoffs.pdaremake.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class SquareLinearLayout : LinearLayout {

    constructor( context :Context,  attrs: AttributeSet) : super(context,attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var size : Int = (MeasureSpec.getSize(widthMeasureSpec)*0.857).toInt()
        var mode = MeasureSpec.getMode(widthMeasureSpec)
        var height = MeasureSpec.makeMeasureSpec( size,mode)
        setMeasuredDimension(widthMeasureSpec,height)

    }


}