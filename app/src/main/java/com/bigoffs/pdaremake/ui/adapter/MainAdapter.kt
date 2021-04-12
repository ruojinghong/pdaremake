package com.bigoffs.pdaremake.ui.adapter

import com.bigoffs.pdaremake.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class MainAdapter(data: ArrayList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(
        R.layout.item_main, data
    ) {


    override fun convert(holder: BaseViewHolder, item: String) {
        when (item) {
            "入库作业" -> {
                holder.setBackgroundResource(R.id.
                sll, R.mipmap.ruku)
            }
            "拣货作业" -> {
                holder.setBackgroundResource(R.id.sll, R.mipmap.jianhuo)
            }
            "理货作业" -> {
                holder.setBackgroundResource(R.id.sll, R.mipmap.lihuo)
            }
            "盘点作业" -> {
                holder.setBackgroundResource(R.id.sll, R.mipmap.pandian)
            }
            "查询货品" -> {
                holder.setBackgroundResource(R.id.sll, R.mipmap.chaxun)
            }
            "采集工具" -> {
                holder.setBackgroundResource(R.id.sll, R.mipmap.caiji)
            }
            else -> {
                holder.setBackgroundResource(R.id.sll, R.mipmap.ruku)
            }

        }
            holder.setText(R.id.tv_desc,item)
    }

}