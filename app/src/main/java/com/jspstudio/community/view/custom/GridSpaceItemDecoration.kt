package com.jspstudio.community.view.custom

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jspstudio.community.util.Util

class GridSpaceItemDecoration(private val spanCount: Int, private val space: Int) : RecyclerView.ItemDecoration() {

//    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//        val position = parent.getChildAdapterPosition(view) // 항목의 위치
//        val column = position % spanCount // 현재 항목의 열 위치
//        val itemCount = parent.adapter?.itemCount ?: 0 // 전체 항목 수
//
//        // 각 열에 대한 좌우 여백 설정
//        outRect.left = space / 2
//        outRect.right = space / 2
//
//        // 첫 번째 행에만 상단 여백 추가
//        if (position < spanCount) {
//            outRect.top = space
//        }
//
//        // 모든 항목에 하단 여백 추가
//        outRect.bottom = space
//
//        // 마지막 행의 항목에 추가적인 하단 여백을 주려면 아래 코드를 활용
//        // 전체 행 수 계산
//        val totalRows = Math.ceil(itemCount.toDouble() / spanCount.toDouble()).toInt()
//        // 현재 항목의 행 위치
//        val row = position / spanCount + 1
//        // 마지막 행에만 추가적인 하단 여백 설정
//        if (row == totalRows) {
//            outRect.bottom += space // 마지막 행의 하단 여백 추가
//        }
//    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//        val position = parent.getChildAdapterPosition(view) // 항목의 위치
//        val itemCount = parent.adapter?.itemCount ?: 0 // 전체 항목 수

        val position = parent.getChildAdapterPosition(view) // 항목의 위치
        val column = position % spanCount // 현재 항목이 속한 열

        // 좌우 여백 기본 설정
        outRect.left = space
        outRect.right = space

        if (column != 1) { // spanCount가 3일 때, 1은 가운데 열을 의미
            outRect.top += space // 가운데 항목에 추가 상단 여백
            outRect.bottom += space // 가운데 항목에 추가 하단 여백
        }
        // 첫 번째 행에만 상단 여백 추가
        //if (position < spanCount) outRect.top = space / 2
//
//
//        // 모든 항목에 하단 여백 추가
//        outRect.bottom = space
    }
}
