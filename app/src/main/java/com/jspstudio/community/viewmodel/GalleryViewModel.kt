package com.jspstudio.community.viewmodel

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jspstudio.community.base.BaseViewModel
import com.jspstudio.community.model.ImageData
import com.jspstudio.community.util.LogMgr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GalleryViewModel  : BaseViewModel("GalleryViewModel"){

    val _fileList = MutableLiveData<MutableList<ImageData>>()
    val fileList: LiveData<MutableList<ImageData>> get() = _fileList

    val _map = LinkedHashMap<String, ImageData>()

    private val _selFileList = MutableLiveData<MutableList<ImageData>>()
    val selFileList: LiveData<MutableList<ImageData>> get() = _selFileList

    private val _files = MutableLiveData<List<ImageData>>()
    val files: LiveData<List<ImageData>> = _files

    val getFile = mutableListOf<ImageData>()

    fun updateFiles(updatedFiles: List<ImageData>) {
        _files.value = updatedFiles
    }

    fun addFile(file: ImageData) {
        getFile.add(file)
    }

    fun removeFile(file: ImageData) {
        getFile.remove(file)
    }
    fun getFile() : MutableList<ImageData> {
        return getFile
    }

    fun putFile() {
        _selFileList.value = getFile
        LogMgr.e(TAG, getFile.size.toString())
    }

    suspend fun getAlbumAll(context: Context) {
        withContext(Dispatchers.IO) {

            val item : MutableList<ImageData> = mutableListOf()

            val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                    + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.DURATION
            )
            val collectionUri = MediaStore.Files.getContentUri("external")

            val cursor = context.contentResolver.query(
                collectionUri,
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
            )

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val idIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
                    val displayNameColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
                    val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
                    val mediaTypeIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)
                    val durationIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DURATION)
                    val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

                    if (idIndex != -1 && mediaTypeIndex != -1 && durationIndex != -1) {
                        val uri = ContentUris.withAppendedId(collectionUri, cursor.getLong(idIndex))
                        val id = cursor.getLong(idIndex)
                        val mediaType = cursor.getInt(mediaTypeIndex)
                        val duration = cursor.getInt(durationIndex)
                        val name = cursor.getString(displayNameColumn)
                        val size = cursor.getLong(sizeColumn)
                        val mimeType = cursor.getString(mimeTypeColumn)
                        var getData = ""
                        if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                            getData = "content://media/external/images/media/$id"
                        } else if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                            getData = "content://media/external/video/media/$id"
                        }
                        item.add(ImageData(Uri.parse(getData), name, size, mimeType, duration, index = cursor.position, isCheck = false))
                        if (_map!![item[cursor.position].name] == null) _map?.set(item[cursor.position].name, item[cursor.position])
                    }
                } while (cursor.moveToNext())
            }
            cursor!!.close()

            withContext(Dispatchers.Main) {
                if (item.isNotEmpty()) {
                    _fileList.postValue(item)
                }
            }
        }

//        adapter.submitList(mList)
//        mList.forEach { map[it.name] = it }
//        binding.recycler.scrollToPosition(0)
    }
}