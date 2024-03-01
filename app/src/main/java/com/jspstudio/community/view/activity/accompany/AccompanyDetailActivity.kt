package com.jspstudio.community.view.activity.accompany

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.common.IntentKey
import com.jspstudio.community.databinding.ActivityAccompanyDetailBinding
import com.jspstudio.community.model.AccompanyData
import com.jspstudio.community.user.UserData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.Util
import com.jspstudio.community.util.UtilAnim
import com.jspstudio.community.view.activity.ChatActivity
import com.jspstudio.community.viewmodel.AccompanyViewModel

class AccompanyDetailActivity : BaseActivity<ActivityAccompanyDetailBinding>(R.layout.activity_accompany_detail, "AccompanyDetailActivity") {

    private val viewModel: AccompanyViewModel by viewModels()
    private var item : AccompanyData = AccompanyData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmAccompany = viewModel
        binding.lifecycleOwner= this
        initData()
        onClick()
    }

    private fun initData() {
        item = Util.getParcelableExtra(intent, IntentKey.ACCOMPANY_DATA, AccompanyData::class.java)!!
        if (item.id == UserData.id.toString()) binding.btnChat.visibility = View.GONE
        binding.vmAccompany?.getBoardDetail(item)
    }

    private fun onClick() {
        binding.btnChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(IntentKey.ACCOMPANY_DATA, item)
            startActivity(intent)
        }
    }
}