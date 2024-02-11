package com.jspstudio.community.view.activity

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityLoginBinding
import com.jspstudio.community.sns.KakaoLoginMgr
import com.jspstudio.community.view.util.Constant
import com.jspstudio.community.viewmodel.LoginViewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login, "LoginActivity") {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding.vmLogin = LoginViewModel(this)
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
        observe()
        //var keyHash = Utility.getKeyHash(this)
    }

    private fun click() {
        binding.btnKakao.setOnClickListener { binding.vmLogin?.kakaoLogin() } // Kakao Login
        binding.btnGuest.setOnClickListener { binding.vmLogin?.guestLogin() } // Guest Login
    }

    private fun observe() {

    }

    private fun guestLogin() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}