package com.bigoffs.pdaremake.ui.adapter

import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.data.model.bean.NewInStoreNormalBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *Author:sxy
 *Time:2021/5/19  4:16 下午
 *Desc:
 */
class NewInStoreNormalAdapter(data : ArrayList<NewInStoreNormalBean>) : BaseQuickAdapter<NewInStoreNormalBean,BaseViewHolder>(R.layout.item_sheet2,data) {
    override fun convert(holder: BaseViewHolder, item: NewInStoreNormalBean) {
        holder.setText(R.id.tv_shelf,item.shelf)
        holder.setText(R.id.tv_unique,item.unique)
        holder.setText(R.id.tv_barcode,item.barcode)
    }
}