package com.bigoffs.pdaremake.ui.adapter

import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.data.model.bean.NewInStoreErrorBean
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *User:Kirito
 *Time:2021/5/17  23:22
 *Desc:
 */
class InStoreErrorAdapter(data:ArrayList<NewInStoreErrorBean>)
    : BaseSectionQuickAdapter<NewInStoreErrorBean, BaseViewHolder>(R.layout.item_instore,R.layout.item_instore,data) {
    override fun convert(holder: BaseViewHolder, item: NewInStoreErrorBean) {
        TODO("Not yet implemented")
    }

    override fun convertHeader(helper: BaseViewHolder, item: NewInStoreErrorBean) {
        TODO("Not yet implemented")
    }
}