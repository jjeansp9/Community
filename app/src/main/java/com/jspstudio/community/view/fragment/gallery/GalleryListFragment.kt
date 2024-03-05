package com.jspstudio.community.view.fragment.gallery

import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseFragment
import com.jspstudio.community.common.IntentKey
import com.jspstudio.community.databinding.FragmentAccountBinding
import com.jspstudio.community.databinding.FragmentGalleryListBinding
import com.jspstudio.community.model.ImageData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.Util
import com.jspstudio.community.view.adapter.GalleryAdapter
import com.jspstudio.community.view.custom.GridSpaceItemDecoration
import com.jspstudio.community.viewmodel.GalleryViewModel
import com.jspstudio.community.viewmodel.MainViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GalleryListFragment : BaseFragment<FragmentGalleryListBinding>("GalleryListFragment") {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter : GalleryAdapter
    private val mList = mutableListOf<ImageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryListBinding.inflate(inflater, container, false)
        binding.vmGal = ViewModelProvider(mContext)[GalleryViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GalleryAdapter(mContext, mList, onItemClick = { it, position ->
            val bundle = bundleOf(
                IntentKey.GALLERY_DATA to mList,
                "position" to position
            )
            findNavController().navigate(R.id.action_first_to_detail, bundle)
        })
        binding.recycler.adapter = adapter
        binding.recycler.itemAnimator = null
        binding.recycler.addItemDecoration(GridSpaceItemDecoration(3, Util.fromDpToPx(1).toInt()))
        getAlbumType()
    }

    private fun getAlbumType() {
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 앨범 이름
            MediaStore.Images.Media.BUCKET_ID // 앨범의 고유 ID
        )

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val cursor = mContext.contentResolver.query(
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

//        ArrayAdapter(
//            this, // Context
//            R.layout.layout_custom_spinner_item, // Custom layout
//            albumList // 데이터 소스
//        ).also { it ->
//            it.setDropDownViewResource(R.layout.layout_custom_spinner_dropdown_item)
//            binding.spinnerFolder.adapter = it
//        }
//
//        binding.spinnerFolder.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                // 선택된 항목의 값을 가져옴
//                val selItem = parent?.getItemAtPosition(position).toString()
//                if (position == 0) getAlbumAll() // 전체
//                else getAlbumImage(selItem)
//            }
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
        getAlbumAll()
    }

    private fun getAlbumAll() {
        if (mList.size > 0) mList.clear()
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

        val cursor = mContext.contentResolver.query(
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
                    mList.add(ImageData(Uri.parse(getData), name, size, mimeType, duration))
                }
            } while (cursor.moveToNext())
        }

        cursor!!.close()

        adapter.submitList(mList)
        binding.recycler.scrollToPosition(0)
    }

    private fun getAlbumImage(selFolder: String?) {
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

        val cursor = mContext.contentResolver.query(
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

    private fun getAlbumVideo() {

    }
}