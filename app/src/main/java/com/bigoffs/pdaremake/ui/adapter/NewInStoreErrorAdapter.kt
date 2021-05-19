package com.bigoffs.pdaremake.ui.adapter

import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.data.model.bean.NewInStoreErrorBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *Author:sxy
 *Time:2021/5/19  4:16 下午
 *Desc:
 */
class NewInStoreErrorAdapter(data : ArrayList<NewInStoreErrorBean>)
    : BaseQuickAdapter<NewInStoreErrorBean,BaseViewHolder>(R.layout.item_sheet1,data) {
    override fun convert(holder: BaseViewHolder, item: NewInStoreErrorBean) {
        holder.setText(R.id.tv_unique,item.unique)
    }
}