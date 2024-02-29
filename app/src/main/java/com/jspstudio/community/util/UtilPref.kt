package com.jspstudio.community.util

import android.content.Context
import android.content.SharedPreferences
import com.jspstudio.community.R
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.user.UserData

object UtilPref {
    private const val PREFERENCE_NAME = "com.jspstudio.community.util.preferences"

    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_GENDER = "user_gender"
    private const val KEY_USER_BIRTH = "user_birth"
    private const val KEY_USER_PROFILE = "user_profile"
    private const val KEY_USER_MBTI = "user_mbti"
    private const val KEY_USER_ABOUT_ME = "user_about_me"
    private const val KEY_USER_LOGIN_TYPE = "user_login_type"
    private const val KEY_USER_START_DATE = "user_start_date"

    private fun pref(context: Context) : SharedPreferences { return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) }

    fun getInt(context: Context, key: String): Int { return pref(context).getInt(key, 0) }
    fun setInt(context: Context, key: String, value: Int) { pref(context).edit().putInt(key, value).apply() }

    fun getLong(context: Context, key: String): Long { return pref(context).getLong(key, 0L) }
    fun setLong(context: Context, key: String, value: Long) { pref(context).edit().putLong(key, value).apply() }

    fun getString(context: Context, key: String): String? { return pref(context).getString(key, "") }
    fun setString(context: Context, key: String, value: String) { pref(context).edit().putString(key, value).apply() }

    fun getBoolean(context: Context, key: String): Boolean { return pref(context).getBoolean(key, false) }
    fun setBoolean(context: Context, key: String, value: Boolean) { pref(context).edit().putBoolean(key, value).apply() }

    fun getFloat(context: Context, key: String): Float { return pref(context).getFloat(key, 0f) }
    fun setFloat(context: Context, key: String, value: Float) { pref(context).edit().putFloat(key, value).apply() }

    fun getNewMember(context: Context, key: String): Boolean { return pref(context).getBoolean(key, true) }
    fun setNewMember(context: Context, key: String, value: Boolean) { pref(context).edit().putBoolean(key, value).apply() }

    fun setUserData(context: Context) {
        pref(context).edit().putString(KEY_USER_ID, UserData.id).apply()
        pref(context).edit().putString(KEY_USER_NAME, UserData.name).apply()
        pref(context).edit().putString(KEY_USER_GENDER, UserData.gender).apply()
        pref(context).edit().putString(KEY_USER_BIRTH, UserData.birth).apply()
        pref(context).edit().putString(KEY_USER_MBTI, UserData.mbti).apply()
        pref(context).edit().putString(KEY_USER_ABOUT_ME, UserData.aboutMe).apply()
        pref(context).edit().putString(KEY_USER_PROFILE, UserData.profile).apply()
        pref(context).edit().putString(KEY_USER_LOGIN_TYPE, UserData.loginType).apply()
        pref(context).edit().putString(KEY_USER_START_DATE, UserData.startDate).apply()
        LogMgr.e("id : ", UserData.id)
    }

    fun getUserData(context: Context) {
        UserData.id = pref(context).getString(KEY_USER_ID, "")
        UserData.name = pref(context).getString(KEY_USER_NAME, "")
        UserData.gender = if (pref(context).getString(KEY_USER_GENDER, "") == "female") context.getString(R.string.female)
                          else context.getString(R.string.male)
        UserData.birth = pref(context).getString(KEY_USER_BIRTH, "")
        UserData.mbti = pref(context).getString(KEY_USER_MBTI, "")
        UserData.aboutMe = pref(context).getString(KEY_USER_ABOUT_ME, "")
        UserData.profile = pref(context).getString(KEY_USER_PROFILE, "")
        UserData.loginType = pref(context).getString(KEY_USER_LOGIN_TYPE, "")
        UserData.startDate = pref(context).getString(KEY_USER_START_DATE, "")
        LogMgr.e("id : ", UserData.id)
    }

    fun clearUserData(context: Context) {
        pref(context).edit().putString(KEY_USER_ID, "").apply()
        pref(context).edit().putString(KEY_USER_NAME, "").apply()
        pref(context).edit().putString(KEY_USER_GENDER, "").apply()
        pref(context).edit().putString(KEY_USER_BIRTH, "").apply()
        pref(context).edit().putString(KEY_USER_MBTI, "").apply()
        pref(context).edit().putString(KEY_USER_ABOUT_ME, "").apply()
        pref(context).edit().putString(KEY_USER_PROFILE, "").apply()
        pref(context).edit().putString(KEY_USER_LOGIN_TYPE, "").apply()
        pref(context).edit().putString(KEY_USER_START_DATE, "").apply()
        UserData.id = ""
        UserData.name = ""
        UserData.gender = ""
        UserData.birth = ""
        UserData.mbti = ""
        UserData.aboutMe = ""
        UserData.profile = ""
        UserData.loginType = ""
        UserData.startDate = ""
    }
}