package com.jspstudio.community.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityLoginBinding
import com.jspstudio.community.databinding.ActivitySignUpBinding
import com.jspstudio.community.view.adapter.SignUpVPAdapter
import com.jspstudio.community.viewmodel.LoginViewModel

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up, "SignUpActivity") {
    var pagerAdapter : SignUpVPAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmLogin = LoginViewModel(this)
        binding.lifecycleOwner= this

        setView()
        click()
        observe()

        // 닉네임, 성별, 생년월일, mbti
    }

    private fun setView() {
        pagerAdapter = SignUpVPAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = pagerAdapter
    }

    private fun click() {
        binding.btnNext.setOnClickListener {
            val nextItem = binding.viewPager.currentItem + 1
            if (nextItem < pagerAdapter!!.itemCount) {
                binding.viewPager.currentItem = nextItem
            }
        }
    }

    private fun observe() {

    }

    override fun onBackPressed() {
        if (binding.viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            binding.viewPager.currentItem = binding.viewPager.currentItem - 1
        }
    }
}