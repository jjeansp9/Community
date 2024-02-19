package com.jspstudio.community.view.activity.login

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityLoginBinding
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.sns.GoogleLoginMgr
import com.jspstudio.community.sns.KakaoLoginMgr
import com.jspstudio.community.sns.NaverLoginMgr
import com.jspstudio.community.user.UserData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.OnSingleClickListener
import com.jspstudio.community.util.UtilPref
import com.jspstudio.community.view.activity.MainActivity
import com.jspstudio.community.view.custom.CustomToast
import com.jspstudio.community.viewmodel.LoginViewModel
import com.kakao.sdk.common.util.Utility

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login, "LoginActivity") {

    private lateinit var auth: FirebaseAuth

    private var kakaoLoginMgr : KakaoLoginMgr? = null
    private var naverLoginMgr : NaverLoginMgr? = null
    private var googleLoginMgr : GoogleLoginMgr? = null

    private var btnClick = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkUser()
        enableEdgeToEdge()
        binding.vmLogin = LoginViewModel()
        binding.lifecycleOwner= this

        val brightness = 0.5f

        val colorMatrix = ColorMatrix(floatArrayOf(
            brightness, 0f, 0f, 0f, 0f,
            0f, brightness, 0f, 0f, 0f,
            0f, 0f, brightness, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        ))

        binding.img.colorFilter = ColorMatrixColorFilter(colorMatrix)

        setSnsMgr()
        auth = FirebaseAuth.getInstance()
        click()
        observe()
        var keyHash = Utility.getKeyHash(this)
        LogMgr.e(TAG, "keyhash : " + keyHash)
    }

    private fun checkUser() {
        UtilPref.getUserData(this)
        if (UserData.id != null && UserData.id!!.trim().isNotEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    private fun setSnsMgr() {
        kakaoLoginMgr = KakaoLoginMgr(this)
        naverLoginMgr = NaverLoginMgr(this)
        googleLoginMgr = GoogleLoginMgr(this)
    }

    private fun click() {
        // todo : 회원가입을 이미 한 유저라면 로그인할 때 pref에 data 저장하기
        binding.btnKakao.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                binding.vmLogin?.kakaoLogin(this@LoginActivity, kakaoLoginMgr!!)
            }
        })
        //binding.btnKakao.setOnClickListener { binding.vmLogin?.kakaoLogin(kakaoLoginMgr!!) } // Kakao Login
        binding.btnNaver.setOnClickListener { binding.vmLogin?.naverLogin(this, naverLoginMgr!!) } // Naver Login
        binding.btnGoogle.setOnClickListener { binding.vmLogin?.googleLogin(googleLoginMgr!!, auth) } // Google Login
        binding.btnGuest.setOnClickListener { binding.vmLogin?.guestLogin() } // Guest Login
        binding.btnEmail.setOnClickListener { binding.vmLogin?.emailLogin(auth, "jjeansp9@gmail.com", "13132424", this) } // Email Login
    }

    private fun observe() {
        binding.vmLogin?.resultCode?.observe(this){
            when(it){
                ResponseCode.SUCCESS -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                ResponseCode.NOT_FOUND -> {
                    startActivity(Intent(this, SignUpActivity::class.java))
                }
            }
        }
    }
}