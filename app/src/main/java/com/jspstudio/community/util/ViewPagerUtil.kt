package com.jspstudio.community.util

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.lang.reflect.Field

object ViewPagerUtil {

    fun setScrollDuration(viewPager2: ViewPager2, duration: Int) {
        try {
            // ViewPager2의 RecyclerView 필드에 접근
            val getDuration = duration / 10
            val recyclerViewField: Field = ViewPager2::class.java.getDeclaredField("mRecyclerView")
            recyclerViewField.isAccessible = true
            val recyclerView: RecyclerView = recyclerViewField.get(viewPager2) as RecyclerView

            // RecyclerView의 LayoutManager에 접근하여 스크롤 속도 조절
            recyclerView.layoutManager = object : LinearLayoutManager(recyclerView.context, HORIZONTAL, false) {
                override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
                    val scroller = LinearSmoothScrollerCustom(getDuration, recyclerView!!.context)
                    scroller.targetPosition = position
                    startSmoothScroll(scroller)
                }
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    // LinearSmoothScroller를 상속받아 스크롤 속도를 조절하는 클래스
    private class LinearSmoothScrollerCustom(duration: Int, context: Context) : LinearSmoothScroller(context) {
        private val duration = duration
        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return duration.toFloat() / displayMetrics.densityDpi
        }
    }
}