package com.jspstudio.community.view.custom

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jspstudio.community.util.Util

class GridSpaceItemDecoration(private val spanCount: Int, private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        /**
        * spanCount가 1일때, 2이상일 때 균일한 margin설정을 위해 각각 다르게 설정
        * */
        if (spanCount <= 1) {
            val position = parent.getChildAdapterPosition(view)

            if (position < spanCount) outRect.top = space + Util.fromDpToPx(4).toInt() // 첫 번째 행에 상단 여백 적용
            outRect.bottom = space + Util.fromDpToPx(4).toInt() // 모든 아이템에 하단 여백 적용

            // 모든 아이템에 동일한 좌우 여백 적용
            outRect.left = space
            outRect.right = space

        } else {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount      // 0부터 시작

            if (position < spanCount) outRect.top = space
            outRect.bottom = space

            // 좌측 여백
            when (column) {
                0 -> {
                    outRect.left = space
                    outRect.right = space / 2
                }
                // 우측 여백
                spanCount - 1 -> {
                    outRect.left = space / 2
                    outRect.right = space
                }
                // 중간 여백
                else -> {
                    outRect.left = space / 2
                    outRect.right = space / 2
                }
            }
        }
    }
}
