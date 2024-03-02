package com.jspstudio.community.viewmodel

import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseViewModel
import com.jspstudio.community.firebase.board.accompany.FireStoreAccompany
import com.jspstudio.community.firebase.chat.FireStoreChat
import com.jspstudio.community.model.AccompanyData
import com.jspstudio.community.model.MessageData
import com.jspstudio.community.model.UserData
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.util.LogMgr

class MessageViewModel : BaseViewModel("MessageViewModel") {
    val _messageItem = MutableLiveData<MutableList<MessageData>?>()
    val messageItem : LiveData<MutableList<MessageData>?> = _messageItem

    var _responseCode = MutableLiveData<Int>()
    var responseCode : LiveData<Int> = _responseCode

    var _msg = MutableLiveData<String>()
    var msg : LiveData<String> = _msg

    fun etMsg(s: String){ _msg.value = s.ifEmpty { "" } }

    fun getMsgList(context: Context) {
        FireStoreChat.getMsgList(context) {
            if (it.size > 0) {
                _messageItem.postValue(it)
                LogMgr.e(TAG, it.size.toString())
                _responseCode.value = ResponseCode.SUCCESS
            }
        }
    }

    fun sendMsg(context: Context, user: UserData) {
        if (_msg.value != null && _msg.value!!.isNotEmpty()) {
            FireStoreChat.sendMsg(context, user, _msg.value!!)
        }
    }

    fun getMsg(context: Context, user: UserData) {
        FireStoreChat.getMsg(context, user) {
            if (it.size > 0) {
                _messageItem.postValue(it)
                _responseCode.value = ResponseCode.SUCCESS
            }
        }
    }
}