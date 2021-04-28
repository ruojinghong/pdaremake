package com.bigoffs.pdaremake.ui.adapter

import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.data.model.bean.FindSame
import com.bigoffs.pdaremake.data.model.bean.StockMap
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *User:Kirito
 *Time:2021/4/29  0:05
 *Desc:
 */
class FindGoodlAdapter(data: ArrayList<FindSame.StockMap>)
    : BaseQuickAdapter<FindSame.StockMap,BaseViewHolder>( R.layout.item_three_row,data) {
    override fun convert(holder: BaseViewHolder, item: FindSame.StockMap) {
       holder.setText(R.id.tv1,item.shelf_code)
       holder.setText(R.id.tv2,item.spec_list[0]?.spec_value+"")
       holder.setText(R.id.tv3,item.stock_num.toString())
    }
}