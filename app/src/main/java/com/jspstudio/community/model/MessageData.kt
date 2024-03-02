package com.jspstudio.community.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageData(
    val user : UserData = UserData(),
    val message : String = "",
    val msgInsertDate : String = ""
) : Parcelable
