package com.bigoffs.pdaremake.data.model.bean

import com.bigoffs.pdaremake.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *Author:sxy
 *Time:2021/5/19  4:16 下午
 *Desc:
 */
class Sheet1Adapter(data : ArrayList<Sheet1>) : BaseQuickAdapter<Sheet1,BaseViewHolder>(R.layout.item_sheet1,data) {
    override fun convert(holder: BaseViewHolder, item: Sheet1) {
        holder.setText(R.id.tv_unique,item.unique)
    }
}