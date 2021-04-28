package com.bigoffs.pdaremake.ui.adapter

import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.data.model.bean.StockMap
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *User:Kirito
 *Time:2021/4/29  0:05
 *Desc:
 */
class BarcodeDetailAdapter(data: ArrayList<StockMap>)
    : BaseQuickAdapter<StockMap,BaseViewHolder>( R.layout.item_two_row,data) {
    override fun convert(holder: BaseViewHolder, item: StockMap) {
       holder.setText(R.id.tv1,item.shelf_code)
       holder.setText(R.id.tv2,item.stock_num.toString())
    }
}