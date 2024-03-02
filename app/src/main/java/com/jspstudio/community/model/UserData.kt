package com.jspstudio.community.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class UserData(
    var id: String = "",
    var name: String = "",
    var gender: String = "",
    var birth: String = "",
    var mbti: String = "",
    var profile: String = "",
    var loginType: String = "",
    var startDate: String = "",
    var aboutMe: String = "",
    var nationality: Locale? = null
) : Parcelable