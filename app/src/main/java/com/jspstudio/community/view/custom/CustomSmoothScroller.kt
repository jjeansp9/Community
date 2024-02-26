package com.jspstudio.community.view.custom

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class CustomSmoothScroller(context: Context, delay : Float) : LinearSmoothScroller(context) {
    private val mDelay = delay
    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
        // 1초 동안 스크롤해야 하는 픽셀 당 속도를 계산합니다.
        // 예: 1000ms / displayMetrics.densityDpi = 스크롤 속도 조절
        return mDelay / displayMetrics.densityDpi
    }
}