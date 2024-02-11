package com.jspstudio.community.util

import android.content.Context
import android.content.SharedPreferences

object UtilPref {
    private const val PREFERENCE_NAME = "com.jspstudio.community.util.preferences"

    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_PROFILE = "user_profile"

    private const val KEY_LOGIN_TYPE = "login_type"
    private const val KEY_LOGIN_ID = "login_id"
    private const val KEY_LOGIN_EMAIL = "login_email"

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
}