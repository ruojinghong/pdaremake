package com.bigoffs.pdaremake.ui.adapter

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.data.model.bean.CheckBoxBean
import com.bigoffs.pdaremake.data.model.bean.InStoreBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreNormalBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xujiaji.library.RippleCheckBox

/**
 *Author:sxy
 *Time:2021/5/19  4:16 下午
 *Desc:
 */
class CheckBoxlAdapter(data : MutableList<CheckBoxBean>) : BaseQuickAdapter<CheckBoxBean,BaseViewHolder>(R.layout.pad_collect_item,data) {
    override fun convert(holder: BaseViewHolder, item: CheckBoxBean) {

        holder.getView<RippleCheckBox>(R.id.checkbox).let {
            it.isEnableClick = holder.adapterPosition != 0
            item.checkbox = it
        }



        holder.getView<ImageView>(R.id.iv_delete).setOnClickListener(){
            clickAction.invoke(item,it,holder.adapterPosition)
        }
        item.edittext = holder.getView<EditText>(R.id.et_code)

    }

    private var clickAction: (item: CheckBoxBean, v: View, position: Int) -> Unit =
        { _: CheckBoxBean, _: View, _: Int -> }

    fun setDeleteClick(inputCollectAction: (item: CheckBoxBean, v: View, position: Int) -> Unit) {
        this.clickAction = inputCollectAction
    }

}