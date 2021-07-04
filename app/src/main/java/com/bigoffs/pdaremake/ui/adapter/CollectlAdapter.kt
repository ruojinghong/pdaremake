package com.bigoffs.pdaremake.ui.adapter

import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.data.model.bean.CollectBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreNormalBean
import com.bigoffs.pdaremake.data.model.bean.TallyBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *Author:sxy
 *Time:2021/5/19  4:16 下午
 *Desc:
 */
class CollectlAdapter(data : MutableList<CollectBean>) : BaseQuickAdapter<CollectBean,BaseViewHolder>(R.layout.item_five_row,data) {
    override fun convert(holder: BaseViewHolder, item: CollectBean) {
        holder.setText(R.id.tv1,item.code1)
        holder.setText(R.id.tv2,item.code2)
        holder.setText(R.id.tv3,item.code3)
        holder.setText(R.id.tv4,item.code4)
        holder.setText(R.id.tv5,item.code5)
    }
}