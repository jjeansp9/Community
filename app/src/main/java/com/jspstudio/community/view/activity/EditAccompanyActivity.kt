package com.jspstudio.community.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityEditAccompanyBinding
import com.jspstudio.community.databinding.ActivityMainBinding
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.view.custom.CustomToast
import com.jspstudio.community.viewmodel.AccompanyViewModel
import com.jspstudio.community.viewmodel.MainViewModel

class EditAccompanyActivity : BaseActivity<ActivityEditAccompanyBinding>(R.layout.activity_edit_accompany, "MainActivity") {
    private val viewModel: AccompanyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmAccompany = viewModel
        binding.lifecycleOwner= this
        onClick()
        observe()
    }

    private fun onClick() {
        binding.appBarComplete.setOnClickListener { binding.vmAccompany?.addBoard(this) }
    }

    private fun observe() {
        binding.vmAccompany?.responseCode?.observe(this) {
            when(it) {
                ResponseCode.SUCCESS -> {
                    CustomToast(this, "글 작성 완료")
                    setResult(RESULT_OK)
                    finish()
                }
                else -> {

                }
            }
        }
    }
}