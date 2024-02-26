package com.jspstudio.community.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jspstudio.community.base.BaseViewModel
import com.jspstudio.community.firebase.board.accompany.FireStoreAccompany
import com.jspstudio.community.model.AccompanyData
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.util.LogMgr

class AccompanyViewModel : BaseViewModel("AccompanyViewModel") {
    val _accompanyItem = MutableLiveData<MutableList<AccompanyData>?>()
    val accompanyItem : LiveData<MutableList<AccompanyData>?> = _accompanyItem

    var _boardTitle = MutableLiveData<String>()
    var boardTitle : LiveData<String> = _boardTitle

    var _boardContent = MutableLiveData<String>()
    var boardContent : LiveData<String> = _boardContent

    var _responseCode = MutableLiveData<Int>()
    var responseCode : LiveData<Int> = _responseCode

    fun setBoardTitle(s: String){ _boardTitle.value = if (s.isEmpty()) "" else s }
    fun setBoardContent(s: String){ _boardContent.value = if (s.isEmpty()) "" else s }

    fun addBoard(context: Context) {
        if (_boardTitle.value == null || _boardTitle.value?.isEmpty()!!) {
            _responseCode.value = ResponseCode.BINDING_ERROR_TITLE

        } else if (_boardContent.value == null || _boardContent.value?.isEmpty()!!) {
            _responseCode.value = ResponseCode.BINDING_ERROR_CONTENT

        } else {
            val item = AccompanyData(
                title = _boardTitle.value!!,
                content = _boardContent.value!!
            )
            FireStoreAccompany.addBoard(context, item) {
                when(it) {
                    ResponseCode.SUCCESS -> _responseCode.value = ResponseCode.SUCCESS
                    ResponseCode.BINDING_ERROR -> _responseCode.value = ResponseCode.BINDING_ERROR
                }
            }
        }
    }

    fun getBoard(context : Context) {
        LogMgr.e(TAG, "item size : " + _accompanyItem.value?.size.toString())
        FireStoreAccompany.getBoard(context) {
            if (it.size > 0) {
                _accompanyItem.postValue(it)
                _responseCode.value = ResponseCode.SUCCESS
            }
        }
    }
}