package com.sigma.flick.feature.tabs.home.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GroupBankBookItemDecoration: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val count = state.itemCount
        if(position == count-1) {
            outRect.top = 6
        } else if(position == 0){
            outRect.bottom = 6
        } else {
            outRect.top = 6
            outRect.bottom = 6
        }
    }
}