package com.bigoffs.pdaremake.ui.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginRight
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.data.model.bean.QueryResultBean
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.SizeUtils
import org.w3c.dom.Text


class ExplainLinearLayout : LinearLayout {
    /**
     * 0是收缩状态 ，1是展开状态
     */
    var isFold = true;
    lateinit var tvSpuName: TextView
    lateinit var tv_spu: TextView
    lateinit var tv_sku: TextView
    lateinit var tv_num: TextView
    lateinit var tv_brand: TextView
    lateinit var tv_category: TextView
    lateinit var ll_property: LinearLayout
    lateinit var llContent: LinearLayout
    lateinit var tv: TextView


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        addView(LayoutInflater.from(context).inflate(R.layout.component_good, null, true))
        tvSpuName = findViewById(R.id.tv_name)
        tv_spu = findViewById(R.id.tv_spu)
        tv_sku = findViewById(R.id.tv_sku)
        tv_num = findViewById(R.id.tv_num)
        tv_brand = findViewById(R.id.tv_brand)
        tv_category = findViewById(R.id.tv_category)
        ll_property = findViewById(R.id.ll_property)
        llContent = findViewById<LinearLayout>(R.id.content)
        tv = findViewById<TextView>(R.id.tv_fold)
        findViewById<LinearLayout>(R.id.ll_fold).setOnClickListener() {

            foldOrUnfold()
        }


    }


    fun setContent(bean: QueryResultBean) {

        tvSpuName.text = bean.spu_name
        tv_spu.text = bean.spu_id.toString()
        tv_sku.text = bean.sku_id.toString()
        tv_num.text = bean.spu_no
        tv_brand.text = bean.brand_name
        tv_category.text = bean.category_name
        for (item in bean.property_list) {
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.layoutParams =
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dp2px(32f))
            linearLayout.gravity = Gravity.CENTER_VERTICAL

            val tvname = TextView(context)
            val param = LayoutParams(ConvertUtils.dp2px(80f), ViewGroup.LayoutParams.WRAP_CONTENT)
            tvname.layoutParams = param
            tvname.text = item.property_key
            tvname.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
            tvname.setTextColor(Color.BLACK)


            val textView = TextView(context)
            textView.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textView.text = item.property_value
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
            textView.setTextColor(Color.parseColor("#999999"))

            linearLayout.addView(tvname)
            linearLayout.addView(textView)

            ll_property.addView(linearLayout)


        }


    }

    fun foldOrUnfold() {
        if (isFold) {
            //收缩状态
            isFold = false
            llContent.visibility = View.VISIBLE
            tv.text = "收缩"
            tv.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                ResourcesCompat.getDrawable(context.resources, R.mipmap.fold, null),
                null
            )

        } else {
            //展开状态
            isFold = true
            llContent.visibility = View.GONE
            tv.text = "展开"
            tv.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                ResourcesCompat.getDrawable(context.resources, R.mipmap.unfold, null),
                null
            )
        }
    }

}