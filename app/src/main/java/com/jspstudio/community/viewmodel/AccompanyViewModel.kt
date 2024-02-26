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

    var _responseCode = MutableLiveData<Int>()
    var responseCode : LiveData<Int> = _responseCode

    fun setBoardTitle(s: String){ _boardTitle.value = s.ifEmpty { "" } }

//    fun test() {
//        val list = ArrayList<AccompanyData>()
//        list.add(AccompanyData(title = "테스트중입니다"))
//        list.add(AccompanyData(title = "테스트중입니다1"))
//        list.add(AccompanyData(title = "테스트중입니다2"))
//        list.add(AccompanyData(title = "테스트중입니다3"))
//        _accompanyItem.postValue(list)
//    }

    fun addBoard(context: Context) {
        if (_boardTitle.value?.isEmpty()!!) {
            _responseCode.value = ResponseCode.BINDING_ERROR
        } else {
            val item = AccompanyData(title = _boardTitle.value!!)
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