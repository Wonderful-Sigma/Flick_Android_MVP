package com.sigma.data.utils

import android.content.Context
import android.content.SharedPreferences
import com.sigma.main.model.user.UserResponseModel

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(FLICK, Context.MODE_PRIVATE)

    var autoLogin: Boolean
        get() = prefs.getBoolean(AUTO_LOGIN_KEY, false)
        set(value) = prefs.edit().putBoolean(AUTO_LOGIN_KEY, value).apply()


    // todo : accessToken이랑 refreshToken은 어떻게 재발급 받음?

    var accessToken: String
        get() = prefs.getString(ACCESS_TOKEN, "").toString()
        set(value) = prefs.edit().putString(ACCESS_TOKEN, value).apply()

    var refreshToken: String
        get() = prefs.getString(REFRESH_TOKEN, "").toString()
        set(value) = prefs.edit().putString(REFRESH_TOKEN, value).apply()

    fun deleteToken() {
        prefs.edit().remove(AUTO_LOGIN_KEY).apply()
        prefs.edit().remove(ACCESS_TOKEN).apply()
        prefs.edit().remove(REFRESH_TOKEN).apply()
    }

    companion object {
        const val FLICK = "FLICK"
        const val AUTO_LOGIN_KEY = "AUTO_LOGIN_KEY"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
    }
}