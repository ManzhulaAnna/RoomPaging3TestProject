package com.test.ui.fragments.favorites.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class FavItemDecoration(
    private val horizontalMarginInPx: Int,
    private val verticalMarginInPx: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = horizontalMarginInPx
        outRect.left = horizontalMarginInPx
        outRect.top = verticalMarginInPx
        outRect.bottom = verticalMarginInPx
    }
}