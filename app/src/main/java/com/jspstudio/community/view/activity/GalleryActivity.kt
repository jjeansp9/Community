package com.jspstudio.community.view.activity

import android.content.ContentUris
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityChatBinding
import com.jspstudio.community.databinding.ActivityGalleryBinding
import com.jspstudio.community.model.ImageData
import com.jspstudio.community.user.MyData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.Util
import com.jspstudio.community.view.adapter.ChatAdapter
import com.jspstudio.community.view.adapter.GalleryAdapter
import com.jspstudio.community.view.custom.GridSpaceItemDecoration

class GalleryActivity : AppCompatActivity() {

    private lateinit var adapter : GalleryAdapter
    private val binding: ActivityGalleryBinding by lazy { ActivityGalleryBinding.inflate(layoutInflater) }
    private val mList = mutableListOf<ImageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = GalleryAdapter(this, mList, onItemClick = {  })
        binding.recycler.adapter = adapter
        binding.recycler.itemAnimator = null
        binding.recycler.addItemDecoration(GridSpaceItemDecoration(4, Util.fromDpToPx(2).toInt()))
        getAlbumType()
    }

    private fun getAlbumType() {
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 앨범 이름
            MediaStore.Images.Media.BUCKET_ID // 앨범의 고유 ID
        )

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val cursor = contentResolver.query(
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
        val albumList = ArrayList(albumNames)

        ArrayAdapter(
            this, // Context
            R.layout.layout_custom_spinner_item, // Custom layout
            albumList // 데이터 소스
        ).also { it ->
            it.setDropDownViewResource(R.layout.layout_custom_spinner_dropdown_item)
            binding.spinnerFolder.adapter = it
        }

        binding.spinnerFolder.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // 선택된 항목의 값을 가져옴
                val selItem = parent?.getItemAtPosition(position).toString()
                if (position == 0) getAlbum(null) // 전체
                else getAlbum(selItem)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 처리
            }
        }
    }

    private fun getAlbum(selFolder : String?) {
        if (mList.size > 0) mList.clear()
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

        val cursor = contentResolver.query(
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

                mList.add(ImageData(uri, name, size, mimeType))
            }
        }

        adapter.submitList(mList.reversed())
        binding.recycler.scrollToPosition(0)
    }
}