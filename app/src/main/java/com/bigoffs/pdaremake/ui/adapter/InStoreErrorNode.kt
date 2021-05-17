package com.bigoffs.pdaremake.ui.adapter

import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.data.model.bean.NewInStoreErrorBean
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *User:Kirito
 *Time:2021/5/18  1:06
 *Desc:
 */
class InStoreErrorNode :BaseNodeProvider() {
    override val itemViewType: Int
        get() = 2
    override val layoutId: Int
        get() = R.layout.item_instore_error

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val entity: NewInStoreErrorBean = item as NewInStoreErrorBean
        helper.setText(R.id.tv_shelf,entity.unique)

    }


}