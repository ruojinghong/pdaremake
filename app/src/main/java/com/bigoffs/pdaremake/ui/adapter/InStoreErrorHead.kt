package com.bigoffs.pdaremake.ui.adapter

import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.data.model.bean.NewInSoreErrorNode
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *User:Kirito
 *Time:2021/5/18  1:06
 *Desc:
 */
class InStoreErrorHead :BaseNodeProvider() {
    override val itemViewType: Int
        get() = 1
    override val layoutId: Int
        get() = R.layout.item_instore_error_head

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        setArrowSpin(helper, item, false)
    }

    override fun convert(helper: BaseViewHolder, item: BaseNode, payloads: List<Any>) {
        for (payload in payloads) {
            if (payload is Int && payload == EXPAND_COLLAPSE_PAYLOAD) {
                // 增量刷新，使用动画变化箭头
                setArrowSpin(helper, item, true)
            }
        }
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        super.onClick(helper, view, data, position)
        // 这里使用payload进行增量刷新（避免整个item刷新导致的闪烁，不自然）

        // 这里使用payload进行增量刷新（避免整个item刷新导致的闪烁，不自然）
        getAdapter()!!.expandOrCollapse(
            position,
            true,
            true,
            EXPAND_COLLAPSE_PAYLOAD
        )
    }


    private fun setArrowSpin(helper: BaseViewHolder, data: BaseNode, isAnimate: Boolean) {
        val entity: NewInSoreErrorNode = data as NewInSoreErrorNode
        val imageView = helper.getView<ImageView>(R.id.iv)
        if (entity.isExpanded) {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                    .setInterpolator(DecelerateInterpolator())
                    .rotation(0f)
                    .start()
            } else {
                imageView.rotation = 0f
            }
        } else {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                    .setInterpolator(DecelerateInterpolator())
                    .rotation(90f)
                    .start()
            } else {
                imageView.rotation = 90f
            }
        }
    }
}