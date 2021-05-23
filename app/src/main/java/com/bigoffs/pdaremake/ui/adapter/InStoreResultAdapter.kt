package com.bigoffs.pdaremake.ui.adapter

import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.data.model.bean.InStoreResultBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreErrorBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *Author:sxy
 *Time:2021/5/19  4:16 下午
 *Desc:
 */
class InStoreResultAdapter(data : MutableList<InStoreResultBean>)
    : BaseQuickAdapter<InStoreResultBean,BaseViewHolder>(R.layout.item_instore_result,data) {
    override fun convert(holder: BaseViewHolder, item: InStoreResultBean) {
        holder.setText(R.id.shelf,item.id.toString())
        holder.setText(R.id.brand,item.id.toString())
        holder.setText(R.id.spec,item.id.toString())
        holder.setText(R.id.goods_id,item.id.toString())
        holder.setText(R.id.num,item.id.toString())
    }
}