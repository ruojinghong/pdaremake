package com.bigoffs.pdaremake.ui.adapter

import android.view.View
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.ext.setAdapterAnimation
import com.bigoffs.pdaremake.app.util.SettingUtil
import com.bigoffs.pdaremake.data.model.bean.InStoreBean
import com.bigoffs.pdaremake.data.model.bean.StocktakingListBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *User:Kirito
 *Time:2021/6/23  0:14
 *Desc:盘点列表adapter
 */
class StocktakingAdapter(data: MutableList<StocktakingListBean>)
    : BaseQuickAdapter<StocktakingListBean,BaseViewHolder>(R.layout.item_stocktaking,data) {
    override fun convert(holder: BaseViewHolder, item: StocktakingListBean) {
        item.run {
            holder.setText(R.id.tv_no,id.toString())
            holder.setText(R.id.tv1,"盘点标题：${name}")
            holder.setText(R.id.tv2,"盘点类型：${st_type_name}")
            holder.setText(R.id.tv3,"盘点时间：${start_time}")
            holder.setText(R.id.tv_count,"共${book_stock_total}件，已入${real_stock_total}件")
        }

    }

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    fun setCollectClick(inputCollectAction: (item: InStoreBean, v: View, position: Int) -> Unit) {
        this.clickAction = inputCollectAction

    }


    private var clickAction: (item: InStoreBean, v: View, position: Int) -> Unit =
        { _: InStoreBean, _: View, _: Int -> }
}