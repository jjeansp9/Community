package com.jspstudio.community.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.common.IntentKey
import com.jspstudio.community.databinding.ActivityChatBinding
import com.jspstudio.community.model.AccompanyData
import com.jspstudio.community.model.UserData
import com.jspstudio.community.user.MyData
import com.jspstudio.community.util.KeyboardVisibility
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.Util
import com.jspstudio.community.view.adapter.ChatAdapter
import com.jspstudio.community.viewmodel.MessageViewModel

class ChatActivity : BaseActivity<ActivityChatBinding>(R.layout.activity_chat, "ChatActivity") {

    private val viewModel: MessageViewModel by viewModels()
    private lateinit var adapter : ChatAdapter
    private var item : UserData = UserData()
    private lateinit var keyboardVisibilityUtils: KeyboardVisibility

    private var isEntry = true
    private var isSend = false
    private var isOnResume = false

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            // 이미지 Uri를 사용하는 로직을 여기에 구현
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmMsg = viewModel
        binding.lifecycleOwner= this
        item = Util.getParcelableExtra(intent, IntentKey.USER_DATA, UserData::class.java)!!
        initView()
        initObserve()
        onClick()
    }

    override fun onResume() {
        super.onResume()
        isOnResume = true
        initData()
    }

    private fun initView() {

        binding.appbar.setTitle(item.name)
        binding.appbar.setOnBackClick { finish() }

        adapter = ChatAdapter(this, MyData.id!!, onItemClick = {  }, onRootClick = { Util.hideKeyboard(this, binding.etMsg) })
        binding.recycler.adapter = adapter
        binding.recycler.itemAnimator = null

        binding.etMsg.setOnFocusChangeListener { v, hasFocus ->
            if (v.hasFocus()) {
                binding.layoutMsg.setBackgroundResource(R.drawable.bg_stroke_focus)
            } else {
                binding.layoutMsg.setBackgroundResource(R.drawable.bg_stroke_default)
            }
        }

        keyboardVisibilityUtils = KeyboardVisibility(this.window,
            onShowKeyboard = { // 키보드를 올렸을 때

            },
            onHideKeyboard = { // 키보드를 내렸을 때
                binding.etMsg.clearFocus()
            }
        )
    }

    private fun initData() {
        binding.vmMsg?.getMsg(this, item)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onClick() {
        binding.layoutSend.setOnClickListener {
            isSend = true
            binding.vmMsg?.sendMsg(this, item)
            binding.etMsg.setText("")
        }

        binding.imgAddImg.setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }
    }

    private fun initObserve() {
        binding.vmMsg?.messageItem?.observe(this) { itemList ->
            val newList = ArrayList(itemList ?: emptyList())
            adapter.submitList(newList) {
                if (isSend) {
                    isSend = false
                    binding.recycler.scrollToPosition(newList.size - 1)
                }
                if (isEntry) {
                    isEntry = false
                    binding.recycler.scrollToPosition(newList.size - 1)
                }
                if (isOnResume) {
                    isOnResume = false
                    binding.recycler.scrollToPosition(newList.size - 1)
                }
            }
        }

        binding.vmMsg?.msg?.observe(this) {
            if (it == null || it.isEmpty()) binding.imgSend.setImageResource(R.drawable.ic_send_gray)
            else binding.imgSend.setImageResource(R.drawable.ic_send_brand)
        }
    }
}