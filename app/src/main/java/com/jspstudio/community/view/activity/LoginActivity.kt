package com.jspstudio.community.view.activity

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityLoginBinding
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.sns.KakaoLoginMgr
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.view.util.Constant
import com.jspstudio.community.viewmodel.LoginViewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login, "LoginActivity") {

    private lateinit var auth: FirebaseAuth

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

        auth = FirebaseAuth.getInstance()
        click()
        observe()
        //var keyHash = Utility.getKeyHash(this)
    }

    private fun click() {
        binding.btnKakao.setOnClickListener { binding.vmLogin?.kakaoLogin() } // Kakao Login
        binding.btnNaver.setOnClickListener { binding.vmLogin?.naverLogin() } // Naver Login
        binding.btnGoogle.setOnClickListener { binding.vmLogin?.googleLogin(auth) } // Google Login
        binding.btnGuest.setOnClickListener { binding.vmLogin?.guestLogin() } // Guest Login
        binding.btnEmail.setOnClickListener { binding.vmLogin?.emailLogin(auth, "jjeansp9@gmail.com", "13132424", this) } // Email Login
    }

    private fun observe() {
        binding.vmLogin?.resultCode?.observe(this){
            when(it){
                ResponseCode.SUCCESS -> {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                ResponseCode.NOT_FOUND -> {
                    Toast.makeText(this, "회원가입 화면으로 ㄱㄱ", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SignUpActivity::class.java))
                }
            }
        }
    }
}