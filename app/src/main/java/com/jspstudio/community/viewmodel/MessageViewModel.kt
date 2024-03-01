package com.jspstudio.community.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jspstudio.community.base.BaseViewModel
import com.jspstudio.community.firebase.board.accompany.FireStoreAccompany
import com.jspstudio.community.firebase.chat.FireStoreChat
import com.jspstudio.community.model.AccompanyData
import com.jspstudio.community.model.MessageData
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.util.LogMgr

class MessageViewModel : BaseViewModel("MessageViewModel") {
    val _messageItem = MutableLiveData<MutableList<MessageData>?>()
    val messageItem : LiveData<MutableList<MessageData>?> = _messageItem

    var _responseCode = MutableLiveData<Int>()
    var responseCode : LiveData<Int> = _responseCode

    fun sendMsg(context: Context, userId: String, msg: String) {
        FireStoreChat.sendMsg(context, userId, msg) {
            when(it) {
                ResponseCode.SUCCESS -> {

                }
                ResponseCode.FAIL -> {

                }
            }
        }
    }

    fun getMsg(context: Context) {
        FireStoreChat.getMsg(context) {
            if (it.size > 0) {
                _messageItem.postValue(it)
                _responseCode.value = ResponseCode.SUCCESS
            }
        }
    }
}