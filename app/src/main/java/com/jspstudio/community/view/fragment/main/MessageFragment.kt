package com.jspstudio.community.view.fragment.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.jspstudio.community.base.BaseFragment
import com.jspstudio.community.common.IntentKey
import com.jspstudio.community.databinding.FragmentMessageBinding
import com.jspstudio.community.model.MessageData
import com.jspstudio.community.user.MyData
import com.jspstudio.community.view.activity.ChatActivity
import com.jspstudio.community.view.adapter.ChatAdapter
import com.jspstudio.community.view.adapter.MessageAdapter
import com.jspstudio.community.viewmodel.MainViewModel
import com.jspstudio.community.viewmodel.MessageViewModel

class MessageFragment: BaseFragment<FragmentMessageBinding>("MessageFragment") {

    private lateinit var adapter : MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(inflater, container, false)
        binding.vmMain = ViewModelProvider(mContext)[MainViewModel::class.java]
        binding.vmMsg = ViewModelProvider(mContext)[MessageViewModel::class.java]

        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vmMain?.setTitle("message")
        initView()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initData() {
        binding.vmMsg?.getMsgList(mContext)
    }

    private fun initView() {
        adapter = MessageAdapter(mContext, onItemClick = { startChatActivity(it) })
        binding.recycler.adapter = adapter
        binding.recycler.itemAnimator = null
    }
    private fun initObserver() {
        binding.vmMsg?.messageItem?.observe(mContext) {
            val newList = ArrayList(it ?: emptyList())
            adapter.submitList(newList)
        }
    }
    private fun startChatActivity(item: MessageData) {
        val intent = Intent(mContext, ChatActivity::class.java)
        intent.putExtra(IntentKey.USER_DATA, item.user)
        startActivity(intent)
    }
}