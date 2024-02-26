package com.jspstudio.community.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccompanyData(
    val id : String = "",
    val name : String = "",
    val gender : String = "",
    val birth : String = "",
    val mbti : String = "",
    val profile : String = "",
    val title : String = "",
    val content : String = "",
    val insertDate : String = ""
) : Parcelable
