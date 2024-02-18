package com.jspstudio.community.util

import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import android.view.View

object UtilAnim {
    fun btnClickEffect(v: View, durationValue: Long, x: Float, y: Float, push: Boolean) {
        ObjectAnimator.ofFloat(v, "scaleX", x).apply {
            duration = durationValue
            //if (!push) interpolator = OvershootInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(v, "scaleY", y).apply {
            duration = durationValue
            //if (!push) interpolator = OvershootInterpolator()
            start()
        }
    }
}