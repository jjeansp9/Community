package com.jspstudio.community.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.common.IntentKey
import com.jspstudio.community.databinding.ActivityChatBinding
import com.jspstudio.community.model.AccompanyData
import com.jspstudio.community.user.UserData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.Util
import com.jspstudio.community.view.adapter.AccompanyAdapter
import com.jspstudio.community.view.adapter.ChatAdapter
import com.jspstudio.community.view.custom.GridSpaceItemDecoration
import com.jspstudio.community.viewmodel.MessageViewModel

class ChatActivity : BaseActivity<ActivityChatBinding>(R.layout.activity_chat, "ChatActivity") {

    private val viewModel: MessageViewModel by viewModels()
    private lateinit var adapter : ChatAdapter
    private var item : AccompanyData = AccompanyData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmMsg = viewModel
        binding.lifecycleOwner= this
        initView()
        initData()
        initObserve()
        onClick()
    }

    private fun initView() {
        adapter = ChatAdapter(this, UserData.id!!, onItemClick = {  })
        binding.recycler.adapter = adapter
        binding.recycler.itemAnimator = null
    }

    private fun initData() {
        item = Util.getParcelableExtra(intent, IntentKey.ACCOMPANY_DATA, AccompanyData::class.java)!!
        binding.vmMsg?.getMsg(this, item.id)
    }

    private fun onClick() {
        binding.layoutSend.setOnClickListener { binding.vmMsg?.sendMsg(this, item.id, binding.etMsg.text.toString()) }
    }

    private fun initObserve() {
        binding.vmMsg?.messageItem?.observe(this) { itemList ->
            val newList = ArrayList(itemList ?: emptyList())
            adapter.submitList(newList) {
                if (newList.isNotEmpty()) binding.recycler.scrollToPosition(newList.size - 1)
            }
        }
    }
}