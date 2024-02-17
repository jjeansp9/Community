package com.jspstudio.community.view.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivitySignUpBinding
import com.jspstudio.community.user.UserData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.ViewPagerUtil
import com.jspstudio.community.view.adapter.SignUpVPAdapter
import com.jspstudio.community.view.custom.CustomToast
import com.jspstudio.community.view.util.Constant
import com.jspstudio.community.viewmodel.LoginViewModel

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up, "SignUpActivity") {

    var pagerAdapter : SignUpVPAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmLogin = LoginViewModel()
        binding.lifecycleOwner= this

        setView()
        click()
        observe()

        // 닉네임, 성별/생년월일, mbti
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun setView() {
        pagerAdapter = SignUpVPAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = pagerAdapter
        ViewPagerUtil.setScrollDuration(binding.viewPager, 100)
    }

    private fun click() {
        binding.btnNext.setOnClickListener {
            val nextItem = binding.viewPager.currentItem + 1
            when(nextItem - 1) {
                Constant.SIGN_UP_NAME -> nameConfirm(nextItem)
                Constant.SIGN_UP_GENDER_AND_BIRTH -> genderAndBirthConfirm(nextItem)
                Constant.SIGN_UP_MBTI -> mbtiConfirm()
                Constant.SIGN_UP_FINISH -> finishSignUp()
            }
        }
    }

    private fun observe() {

    }

    private fun nameConfirm(nextItem: Int) {
        LogMgr.e(TAG, UserData.name)
        if (UserData.name == null) return
        if (UserData.name!!.isEmpty()) {
            CustomToast(this, "닉네임을 입력해주세요")
            return
        }
        binding.viewPager.currentItem = nextItem
    }

    private fun genderAndBirthConfirm(nextItem: Int) {
        binding.viewPager.currentItem = nextItem
    }

    private fun mbtiConfirm() {
        binding.btnNext.text = getString(R.string.complete)
    }

    private fun finishSignUp() {
        finish()
        CustomToast(this, "회원가입 완료됨")
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.viewPager.currentItem == 0) {
                finish()
            } else {
                binding.viewPager.currentItem = binding.viewPager.currentItem - 1
                val btnText = binding.btnNext.text.toString()
                if (btnText == getString(R.string.complete)) {
                    binding.btnNext.text = getString(R.string.next)
                }
            }
        }
    }
}