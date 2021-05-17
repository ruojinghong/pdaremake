package com.bigoffs.pdaremake.data.model.bean

import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode

/**
 *User:Kirito
 *Time:2021/5/18  1:12
 *Desc:
 */
class NewInSoreErrorNode(private var childNodes: MutableList<BaseNode> = mutableListOf<BaseNode>()) : BaseExpandNode() {

    override val childNode: MutableList<BaseNode>
        get() = childNodes
}