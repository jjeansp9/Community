package com.jspstudio.community.view.activity.accompany

import android.os.Bundle
import androidx.activity.viewModels
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityAccompanyEditBinding
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.view.custom.CustomToast
import com.jspstudio.community.viewmodel.AccompanyViewModel

class AccompanyEditActivity : BaseActivity<ActivityAccompanyEditBinding>(R.layout.activity_accompany_edit, "AccompanyEditActivity") {
    private val viewModel: AccompanyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmAccompany = viewModel
        binding.lifecycleOwner= this
        onClick()
        observe()
    }

    private fun onClick() {
        binding.btnComplete.setOnClickListener { binding.vmAccompany?.addBoard(this) }
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun observe() {
        binding.vmAccompany?.responseCode?.observe(this) {
            when(it) {
                ResponseCode.SUCCESS -> {
                    CustomToast(this, "글 작성 완료")
                    setResult(RESULT_OK)
                    finish()
                }
                ResponseCode.BINDING_ERROR_TITLE -> {
                    CustomToast(this, "제목을 입력해주세요")
                }
                ResponseCode.BINDING_ERROR_CONTENT -> {
                    CustomToast(this, "내용을 입력해주세요")
                }
                else -> {

                }
            }
        }
    }
}