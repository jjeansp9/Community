package com.jspstudio.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jspstudio.community.base.BaseViewModel

class MainViewModel : BaseViewModel("MainViewModel") {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> get() = _title

    fun setTitle(title: String) {
        _title.value = title
    }
}