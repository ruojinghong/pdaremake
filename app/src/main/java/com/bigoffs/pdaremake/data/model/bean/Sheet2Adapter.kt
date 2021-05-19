package com.bigoffs.pdaremake.data.model.bean

import com.bigoffs.pdaremake.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *Author:sxy
 *Time:2021/5/19  4:16 下午
 *Desc:
 */
class Sheet2Adapter(data : ArrayList<Sheet2>) : BaseQuickAdapter<Sheet2,BaseViewHolder>(R.layout.item_sheet2,data) {
    override fun convert(holder: BaseViewHolder, item: Sheet2) {
        holder.setText(R.id.tv_shelf,item.shelf)
        holder.setText(R.id.tv_unique,item.unique)
        holder.setText(R.id.tv_barcode,item.barcode)
        holder.setText(R.id.tv_count,item.num.toString())
    }
}