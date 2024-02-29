package com.jspstudio.community.view.activity.accompany

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.common.IntentKey
import com.jspstudio.community.databinding.ActivityAccompanyDetailBinding
import com.jspstudio.community.model.AccompanyData
import com.jspstudio.community.util.Util
import com.jspstudio.community.viewmodel.AccompanyViewModel

class AccompanyDetailActivity : BaseActivity<ActivityAccompanyDetailBinding>(R.layout.activity_accompany_detail, "AccompanyDetailActivity") {

    private val viewModel: AccompanyViewModel by viewModels()
    private var item : AccompanyData = AccompanyData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmAccompany = viewModel
        binding.lifecycleOwner= this
        initData()
    }

    private fun initData() {
        item = Util.getParcelableExtra(intent, IntentKey.ACCOMPANY_DATA, AccompanyData::class.java)!!
        binding.vmAccompany?.getBoardDetail(item)
    }
}