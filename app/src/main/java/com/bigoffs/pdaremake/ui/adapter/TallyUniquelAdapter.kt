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
class TallyUniquelAdapter(data : MutableList<TallyBean>) : BaseQuickAdapter<TallyBean,BaseViewHolder>(R.layout.item_two_row,data) {
    override fun convert(holder: BaseViewHolder, item: TallyBean) {
        holder.setText(R.id.tv1,item.shelf_code)
        holder.setText(R.id.tv2,item.goods_code)
    }
}