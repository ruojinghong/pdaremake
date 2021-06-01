package com.bigoffs.pdaremake.ui.adapter

import android.view.View
import android.widget.TextView
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.ext.setAdapterAnimation
import com.bigoffs.pdaremake.app.util.DeviceUtil
import com.bigoffs.pdaremake.app.util.SettingUtil
import com.bigoffs.pdaremake.data.model.bean.InStoreBean
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *User:Kirito
 *Time:2021/5/7  23:17
 *Desc:
 */
class InstoreAdapter(data: MutableList<InStoreBean>?,private var type:Int) :
    BaseQuickAdapter<InStoreBean, BaseViewHolder>(R.layout.item_instore,data) {


    private var clickAction: (item: InStoreBean, v: View, position: Int) -> Unit =
        { _: InStoreBean, _: View, _: Int -> }

    override fun convert(holder: BaseViewHolder, item: InStoreBean) {
       item.run {

           if(type == 1){
               //新品入庫
               holder.setText(R.id.tv_no,arrival_no)
               holder.setText(R.id.tv_supplier,"供应商：${sup_name}")
               holder.setText(R.id.tv_warehouse,"仓库：${ware_name}")
               holder.setText(R.id.tv_make_data,"生成时间：${created_at}")
               holder.setText(R.id.tv_count,"共${total_num}件，已入${final_num}件")
               holder.setVisible(R.id.tv_go_rfid_detail,false)
           }else{
               //调拨入库
               holder.setText(R.id.tv_no,order_no)
               holder.setText(R.id.tv_supplier,"调出方：${out_ware_name}")
               holder.setText(R.id.tv_warehouse,"调入方：${in_ware_name}")
               holder.setText(R.id.tv_make_data,"发货时间：${created_at}")
               holder.setText(R.id.tv_count,"共${total_num}件，已入${final_num}件")
               holder.setVisible(R.id.tv_go_rfid_detail,DeviceUtil.isRfidDevice())
           }

       }
//        holder.getView<TextView>(R.id.tv_go_detail).setOnClickListener(){
//            clickAction.invoke(item,it,holder.adapterPosition)
//        }
    }

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    fun setCollectClick(inputCollectAction: (item: InStoreBean, v: View, position: Int) -> Unit) {
        this.clickAction = inputCollectAction
    }
}