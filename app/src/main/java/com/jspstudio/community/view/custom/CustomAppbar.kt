package com.jspstudio.community.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.appbar.AppBarLayout
import com.jspstudio.community.databinding.LayoutCustomAppbarBinding

class CustomAppbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppBarLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutCustomAppbarBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        // 필요한 초기화 코드를 여기에 추가
    }

    fun setOnBackClick(listener: OnClickListener) { binding.imgBack.setOnClickListener(listener) }
    fun setTitle(title: String) { binding.title.text = title }

}