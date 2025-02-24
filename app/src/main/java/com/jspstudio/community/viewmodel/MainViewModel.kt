package com.jspstudio.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jspstudio.community.base.BaseViewModel
import com.jspstudio.community.user.MyData

class MainViewModel : BaseViewModel("MainViewModel") {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> get() = _title

    private val _userData = MutableLiveData<MyData>()
    val userData: LiveData<MyData> get() = _userData

    fun setTitle(title: String) { _title.value = title } // appbar title
    fun setUserInfo() { _userData.value = MyData } // user info
}