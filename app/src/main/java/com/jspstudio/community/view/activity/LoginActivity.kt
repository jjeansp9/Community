package com.jspstudio.community.view.activity

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityLoginBinding
import com.jspstudio.community.viewmodel.LoginViewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding.vmLogin = viewModel
        binding.lifecycleOwner= this

        val brightness = 0.5f

        val colorMatrix = ColorMatrix(floatArrayOf(
            brightness, 0f, 0f, 0f, 0f,
            0f, brightness, 0f, 0f, 0f,
            0f, 0f, brightness, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        ))

        binding.img.colorFilter = ColorMatrixColorFilter(colorMatrix)

        click()
        //var keyHash = Utility.getKeyHash(this)
    }

    private fun click() {
        binding.btnKakao.setOnClickListener { kakaoLogin() }
    }

    private fun kakaoLogin() {

    }
}