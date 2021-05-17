package com.bigoffs.pdaremake.data.model.bean

import com.chad.library.adapter.base.entity.SectionEntity
import com.chad.library.adapter.base.entity.node.BaseNode

/**
 *User:Kirito
 *Time:2021/5/12  0:30
 *Desc:
 */
 class NewInStoreErrorBean(var unique:String = "",private var childNodes: MutableList<BaseNode> = mutableListOf<BaseNode>()) : BaseNode() {
    override val childNode: MutableList<BaseNode>
        get() = childNodes
}