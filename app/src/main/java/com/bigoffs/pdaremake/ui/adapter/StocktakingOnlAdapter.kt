package com.bigoffs.pdaremake.ui.adapter

import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.data.model.bean.NewInStoreNormalBean
import com.bigoffs.pdaremake.data.model.bean.StocktakingOnBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *Author:sxy
 *Time:2021/5/19  4:16 下午
 *Desc:
 */
class StocktakingOnlAdapter(data : MutableList<StocktakingOnBean>) : BaseQuickAdapter<StocktakingOnBean,BaseViewHolder>(R.layout.item_sheet2,data) {
    override fun convert(holder: BaseViewHolder, item: StocktakingOnBean) {
        holder.setText(R.id.tv_shelf,item.shelf_code)
        holder.setText(R.id.tv_barcode,item.goods_code)
        holder.setText(R.id.tv_unique,item.num.toString())
    }
}