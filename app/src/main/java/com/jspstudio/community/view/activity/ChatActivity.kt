package com.jspstudio.community.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityChatBinding
import com.jspstudio.community.viewmodel.MessageViewModel

class ChatActivity : BaseActivity<ActivityChatBinding>(R.layout.activity_chat, "ChatActivity") {
    private val viewModel: MessageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmMsg = viewModel
        binding.lifecycleOwner= this
    }
}