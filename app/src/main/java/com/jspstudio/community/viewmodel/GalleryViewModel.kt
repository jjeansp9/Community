package com.jspstudio.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jspstudio.community.base.BaseViewModel
import com.jspstudio.community.model.ImageData

class GalleryViewModel  : BaseViewModel("GalleryViewModel"){
    private val _fileList = MutableLiveData<MutableList<ImageData>>()
    val fileList: LiveData<MutableList<ImageData>> get() = _fileList
    val getFile = mutableListOf<ImageData>()
    fun addFile(file: ImageData) {
        getFile.add(file)
        _fileList.value = getFile
    }

    fun removeFile(position: Int) {
        getFile.removeAt(position)
        _fileList.value = getFile
    }
}