package com.bigoffs.pdaremake.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.bigoffs.pdaremake.R
import org.w3c.dom.Text


class ExplainLinearLayout : LinearLayout {
    /**
     * 0是收缩状态 ，1是展开状态
     */
   var isFold = true;


    constructor( context :Context,  attrs: AttributeSet) : super(context,attrs){

        addView(LayoutInflater.from(context).inflate(R.layout.component_good,null))
        var llContent = findViewById<LinearLayout>(R.id.content)
        var tv = findViewById<TextView>(R.id.tv_fold)
        findViewById<LinearLayout>(R.id.ll_fold).setOnClickListener(){

            if(isFold){
                //收缩状态
                isFold = false
                llContent.visibility = View.VISIBLE
                tv.text = "收缩"
                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,ResourcesCompat.getDrawable(context.resources,R.mipmap.fold,null),null)

            }else{
                //展开状态
                isFold = true
                llContent.visibility = View.GONE
                tv.text = "展开"
                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,ResourcesCompat.getDrawable(context.resources,R.mipmap.unfold,null),null)
            }

        }




    }



}