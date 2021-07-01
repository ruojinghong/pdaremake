package com.bigoffs.pdaremake.ui.adapter

import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.data.model.bean.AbData
import com.bigoffs.pdaremake.data.model.bean.Data
import com.bigoffs.pdaremake.data.model.bean.NewInStoreErrorBean
import com.bigoffs.pdaremake.data.model.bean.TallyNormalData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *Author:sxy
 *Time:2021/5/19  4:16 下午
 *Desc:
 */
class TallyAbNormalDataAdapter(data : MutableList<AbData>)
    : BaseQuickAdapter<AbData,BaseViewHolder>(R.layout.item_sheet1,data) {
    override fun convert(holder: BaseViewHolder, item: AbData) {
        holder.setText(R.id.tv_unique,item.code)
        holder.setText(R.id.tv2,item.exception)
    }
}