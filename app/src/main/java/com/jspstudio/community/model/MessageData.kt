package com.jspstudio.community.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageData(
    val id : String = "",
    val name : String = "",
    val gender : String = "",
    val birth : String = "",
    val mbti : String = "",
    val profile : String = "",
    val message : String = "",
    val msgInsertDate : String = ""
) : Parcelable
