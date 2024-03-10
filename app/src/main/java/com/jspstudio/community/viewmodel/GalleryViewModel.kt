package com.jspstudio.community.viewmodel

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseViewModel
import com.jspstudio.community.model.ImageData
import com.jspstudio.community.util.LogMgr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel  : BaseViewModel("GalleryViewModel"){

    val _fileList = MutableLiveData<MutableList<ImageData>>()
    val fileList: LiveData<MutableList<ImageData>> get() = _fileList

    val _map = LinkedHashMap<String, ImageData>()

    private val _sendFileList = MutableLiveData<MutableList<ImageData>>()
    val sendFileList: LiveData<MutableList<ImageData>> get() = _sendFileList

    private val _selFiles = MutableLiveData<List<ImageData>>()
    val selFiles: LiveData<List<ImageData>> = _selFiles

    val getFile = mutableListOf<ImageData>()

    fun updateFiles(updatedFiles: MutableList<ImageData>) { _selFiles.value = updatedFiles }
    fun addFile(file: ImageData) { getFile.add(file) }
    fun removeFile(file: ImageData) { getFile.remove(file) }
    fun getFile() : MutableList<ImageData> { return getFile }
    fun sendFile() { _sendFileList.value = getFile }

    suspend fun getAlbumType(context: Context, dataListener: ((response: ArrayList<String>) -> Unit)) {
        withContext(Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 앨범 이름
                MediaStore.Images.Media.BUCKET_ID // 앨범의 고유 ID
            )

            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val cursor = context.contentResolver.query(
                uri,
                projection,
                null, // 필터 없음
                null,
                "${MediaStore.Images.Media.DATE_TAKEN} DESC" // 최신 이미지부터 정렬
            )

            val albumNames = HashSet<String>()
            cursor?.use {
                val bucketDisplayNameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

                val albumCountMap = mutableMapOf<String, Int>()

                while (it.moveToNext()) {
                    val albumName = it.getString(bucketDisplayNameColumn)
                    val currentCount = albumCountMap[albumName] ?: 0
                    albumCountMap[albumName] = currentCount + 1
                    albumNames.add(albumName)
                }

                albumCountMap.forEach { (albumName, count) ->
                    LogMgr.e("getAlbum", "Album: $albumName, Count: $count")
                }
            }
            val albumList = arrayListOf<String>()
            albumList.add(context.getString(R.string.all))
            albumNames.forEach { albumList.add(it) }
            dataListener(albumList)
        }
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
    }

    suspend fun getAlbumImage(context: Context, selFolder: String?) {

        val item : MutableList<ImageData> = mutableListOf()

        val imagesSelection = if (selFolder != null) "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?" else null
        val imagesSelectionArgs = if (selFolder != null) arrayOf("%$selFolder%") else null

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.MIME_TYPE
        )

        val collectionUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            imagesSelection,
            imagesSelectionArgs,
            null
        )

        cursor?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

            while (cursor.moveToNext()) {
                val uri = ContentUris.withAppendedId(collectionUri, cursor.getLong(idColumn))
                val name = cursor.getString(displayNameColumn)
                val size = cursor.getLong(sizeColumn)
                val mimeType = cursor.getString(mimeTypeColumn)

                LogMgr.e("image", "name : $name, mimeType : $mimeType")

                item.add(ImageData(uri, name, size, mimeType, index = cursor.position, isCheck = false))
                if (_map!![item[cursor.position].name] == null) _map?.set(item[cursor.position].name, item[cursor.position])
            }
        }
        cursor!!.close()

        withContext(Dispatchers.Main) {
            if (item.isNotEmpty()) {
                _fileList.postValue(item)
            }
        }
    }

    suspend fun getAlbumVideo() {

    }
}