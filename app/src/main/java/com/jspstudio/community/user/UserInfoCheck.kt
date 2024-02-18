package com.jspstudio.community.user

object UserInfoCheck {
    // 공백 허용안함 [최소 2글자 이상]
    fun nameCheck(str: String): Boolean {
        return "^[가-힣A-Za-z]$".toRegex().containsMatchIn(str)
    }
    // 공백 허용 [최소 2글자 이상]
    fun nameCheckSpace(str: String): Boolean {
        return "^[가-힣A-Za-z\\s]{2,}$".toRegex().containsMatchIn(str)
    }
}