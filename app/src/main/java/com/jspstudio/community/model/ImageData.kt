package com.jspstudio.community.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageData(
    val uri: Uri? = null,
    val name: String = "",
    val size: Long = 0L,
    val mimeType: String = "",
    val duration: Int = -1,
    var isCheck: Boolean = false,
    var num: Int? = null,
    var index: Int = 0,
): Parcelable
