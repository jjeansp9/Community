package com.jspstudio.community.model

import android.content.ContentUris
import android.net.Uri

data class ImageData(
    val uri: Uri? = null,
    val name: String = "",
    val size: Long = 0L,
    val mimeType: String = ""
)
