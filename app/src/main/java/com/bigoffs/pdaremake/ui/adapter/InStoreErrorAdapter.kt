package com.bigoffs.pdaremake.ui.adapter

import com.bigoffs.pdaremake.data.model.bean.NewInSoreErrorNode
import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode

/**
 *User:Kirito
 *Time:2021/5/17  23:22
 *Desc:
 */
const  val EXPAND_COLLAPSE_PAYLOAD = 110
class InStoreErrorAdapter: BaseNodeAdapter{
    constructor(){
        addNodeProvider(InStoreErrorHead());
        addNodeProvider(InStoreErrorNode());
    }

    override fun getItemType(data: List<BaseNode>, position: Int): Int {
                var node =data.get(position)
                if(node is NewInSoreErrorNode){
                        return  1
                }else{
                    return 2
                }
    }

}