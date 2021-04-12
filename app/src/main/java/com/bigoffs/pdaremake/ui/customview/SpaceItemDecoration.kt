package com.bigoffs.pdaremake.ui.customview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(s: Int) : RecyclerView.ItemDecoration() {
    private var space = s
    private var leftAnRightSpace = s/2


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        outRect.bottom  = space

        if (parent.getChildLayoutPosition(view) %2==0) {
            outRect.right = leftAnRightSpace

        }else{
            outRect.left = leftAnRightSpace
        }
    }
}