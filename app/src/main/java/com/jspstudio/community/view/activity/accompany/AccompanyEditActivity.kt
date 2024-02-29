package com.jspstudio.community.view.activity.accompany

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityAccompanyEditBinding
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.Util
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
        initCalendar()
    }

    private fun onClick() {
        val getEtList : List<View?> = listOf(
            binding.etTitle,
            binding.etContent
        )
        binding.root.setOnClickListener { Util.hideKeyboard(this, getEtList) }
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
                    Util.showKeyboard(this, binding.etTitle)
                    CustomToast(this, "제목을 입력해주세요")
                }
                ResponseCode.BINDING_ERROR_CONTENT -> {
                    Util.showKeyboard(this, binding.etContent)
                    CustomToast(this, "내용을 입력해주세요")
                }
                ResponseCode.BINDING_ERROR_DATE -> {
                    CustomToast(this, "날짜를 선택해주세요")
                }
                else -> {

                }
            }
        }
    }
    // 24.3.15 ~ 23.10.9

    private fun initCalendar() {
        val getDate : MutableList<String> = mutableListOf()
        binding.calendarView.setOnRangeSelectedListener { widget, dates ->
            if (getDate.size > 0) getDate.clear()
            getDate.add(dates.first().date.toString())
            getDate.add(dates.last().date.toString())
            LogMgr.e(TAG, dates.first().date.toString())
            LogMgr.e(TAG, dates.last().date.toString())
            binding.vmAccompany?.setBoardDate(getDate)
        }

        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                if (getDate.size > 0) getDate.clear()
                getDate.add(date.date.toString())
                getDate.add(date.date.toString())
                binding.vmAccompany?.setBoardDate(getDate)
            } else {
                binding.vmAccompany?.setBoardDate(null)
            }
        }
    }
}