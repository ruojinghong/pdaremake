package com.bigoffs.pdaremake.ui.adapter

import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.data.model.bean.NewInStoreNormalBean
import com.bigoffs.pdaremake.data.model.bean.TallyBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *Author:sxy
 *Time:2021/5/19  4:16 下午
 *Desc:
 */
class TallyNormalAdapter(data : MutableList<TallyBean>) : BaseQuickAdapter<TallyBean,BaseViewHolder>(R.layout.item_sheet2,data) {
    override fun convert(holder: BaseViewHolder, item: TallyBean) {
        holder.setText(R.id.tv_shelf,item.shelf_code)

        holder.setText(R.id.tv_barcode,item.code)
        holder.setText(R.id.tv_unique,item.nums.toString())
    }
}