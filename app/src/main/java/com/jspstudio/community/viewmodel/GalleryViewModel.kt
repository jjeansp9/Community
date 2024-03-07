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
    }

    fun removeFile(file: ImageData) {
        getFile.remove(file)
    }
    fun getFile() : MutableList<ImageData> {
        return getFile
    }

//    0 1 2 3 4 5
//      2 0   1
//
//    2 4 1
    fun putFile() {
        _fileList.value = getFile
    }
}