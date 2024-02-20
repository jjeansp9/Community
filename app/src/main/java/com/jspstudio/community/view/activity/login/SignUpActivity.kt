package com.jspstudio.community.view.activity.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivitySignUpBinding
import com.jspstudio.community.firebase.FireStoreMgr
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.user.UserData
import com.jspstudio.community.user.UserInfoCheck
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.view.activity.MainActivity
import com.jspstudio.community.view.adapter.SignUpVPAdapter
import com.jspstudio.community.view.custom.CustomToast
import com.jspstudio.community.view.fragment.signup.BirthFragment
import com.jspstudio.community.view.fragment.signup.GenderFragment
import com.jspstudio.community.view.fragment.signup.MbtiFragment
import com.jspstudio.community.view.fragment.signup.NameFragment
import com.jspstudio.community.view.util.Constant
import com.jspstudio.community.viewmodel.LoginViewModel


class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up, "SignUpActivity") {

    private var pagerAdapter : SignUpVPAdapter? = null
    private var fragment: Fragment? = null

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
        //ViewPagerUtil.setScrollDuration(binding.viewPager, 2000)
        // ViewPager2에 페이지 변경 리스너 설정
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val brandColor = ContextCompat.getColor(this@SignUpActivity, R.color.brand_color)
                val grayColor = ContextCompat.getColor(this@SignUpActivity, R.color.gray_light)

                if (binding.viewPager.currentItem == 0) {
                    val currentFragmentTag = "f" + binding.viewPager.currentItem
                    fragment = supportFragmentManager.findFragmentByTag(currentFragmentTag)
                }

                when(position) {
                    Constant.SIGN_UP_NAME -> {
                        (fragment as? NameFragment)?.showNameKeyboard()
                        binding.btnNext.text = getString(R.string.next)
                        binding.stack1.setBackgroundResource(R.color.brand_color)
                        binding.stack2.setBackgroundResource(R.color.gray_light)
                        binding.stack3.setBackgroundResource(R.color.gray_light)
                        binding.stack4.setBackgroundResource(R.color.gray_light)
//                        ViewCompat.setBackgroundTintList(binding.stack1, ColorStateList.valueOf(brandColor))
//                        ViewCompat.setBackgroundTintList(binding.stack2, ColorStateList.valueOf(grayColor))
//                        ViewCompat.setBackgroundTintList(binding.stack3, ColorStateList.valueOf(grayColor))
                    }
                    Constant.SIGN_UP_GENDER -> {
                        binding.btnNext.text = getString(R.string.next)
                        binding.stack2.setBackgroundResource(R.color.brand_color)
                        binding.stack3.setBackgroundResource(R.color.gray_light)
//                        ViewCompat.setBackgroundTintList(binding.stack2, ColorStateList.valueOf(brandColor))
//                        ViewCompat.setBackgroundTintList(binding.stack3, ColorStateList.valueOf(grayColor))
                    }
                    Constant.SIGN_UP_BIRTH -> {
                        binding.btnNext.text = getString(R.string.next)
                        binding.stack3.setBackgroundResource(R.color.brand_color)
                        binding.stack4.setBackgroundResource(R.color.gray_light)
//                        ViewCompat.setBackgroundTintList(binding.stack2, ColorStateList.valueOf(brandColor))
//                        ViewCompat.setBackgroundTintList(binding.stack3, ColorStateList.valueOf(grayColor))
                    }
                    Constant.SIGN_UP_MBTI -> {
                        binding.btnNext.text = getString(R.string.complete)
                        binding.stack4.setBackgroundResource(R.color.brand_color)
                        //ViewCompat.setBackgroundTintList(binding.stack3, ColorStateList.valueOf(brandColor))
                    }
                }
            }
        })
    }

    private fun click() {
        binding.btnNext.setOnClickListener {
            val nextItem = binding.viewPager.currentItem + 1
            val currentFragmentTag = "f" + binding.viewPager.currentItem
            fragment = supportFragmentManager.findFragmentByTag(currentFragmentTag)
            when(nextItem - 1) {
                Constant.SIGN_UP_NAME -> nameConfirm(nextItem)
                Constant.SIGN_UP_GENDER -> genderConfirm(nextItem)
                Constant.SIGN_UP_BIRTH -> birthConfirm(nextItem)
                Constant.SIGN_UP_MBTI -> mbtiConfirmAndFinish()
            }


        }
    }

    private fun observe() {

    }
    private fun nameConfirm(nextItem: Int) {
        if (UserData.name == null || UserData.name!!.isEmpty() || UserData.name!!.replace(" ", "").isEmpty()) {
            (fragment as? NameFragment)?.showNameKeyboard()
            CustomToast(this, "닉네임을 입력해주세요")

        } else if (!UserInfoCheck.nameCheckSpace(UserData.name!!)) {
            (fragment as? NameFragment)?.showNameKeyboard()
            CustomToast(this, "영문,한글 외에는 사용할 수 없습니다")

        } else if (UserData.name!!.length < Constant.NAME_MIN_LENGTH) {
            (fragment as? NameFragment)?.showNameKeyboard()
            CustomToast(this, "닉네임은 ${Constant.NAME_MIN_LENGTH}글자 이상이어야 합니다")

        } else {
            binding.viewPager.currentItem = nextItem
        }
    }

    private fun genderConfirm(nextItem: Int) {
        val isCheck = (fragment as? GenderFragment)?.checkGender()
        if (!isCheck!!) {
            CustomToast(this, "성별을 선택해주세요")
        } else {
            binding.viewPager.currentItem = nextItem
        }
    }

    private fun birthConfirm(nextItem: Int) {
        if (UserData.birth == null || UserData.birth!!.isEmpty()) {
            (fragment as BirthFragment).showYearPicker()
            CustomToast(this, "태어난 연도를 선택해주세요")
        } else {
            binding.viewPager.currentItem = nextItem
        }
    }

    private fun mbtiConfirmAndFinish() {
        if (UserData.mbti == null || UserData.mbti!!.isEmpty()) {
            (fragment as? MbtiFragment)?.showMbtiPicker()
            CustomToast(this, "MBTI를 선택해주세요")
        } else {
            binding.btnNext.setOnClickListener(null)
            FireStoreMgr.addUser(this) {
                when(it) {
                    ResponseCode.SUCCESS -> {
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            CustomToast(this, "회원가입 완료")
                            finish()
                        }, 1000)
                    }
                    ResponseCode.FAIL -> {
                        CustomToast(this, "다시 시도해주세요")
                        click()
                    }
                }
            }
        }
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.viewPager.currentItem == 0) {
                finish()
            } else {
                binding.viewPager.currentItem = binding.viewPager.currentItem - 1
            }
        }
    }
}