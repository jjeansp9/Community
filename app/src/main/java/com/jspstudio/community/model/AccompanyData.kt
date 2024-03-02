package com.jspstudio.community.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccompanyData(
    val user : UserData = UserData(),
    val title : String = "",
    val content : String = "",
    val startDate : String = "",
    val endDate : String = "",
    val insertDate : String = ""
) : Parcelable
