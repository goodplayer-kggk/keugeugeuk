package com.goodplayer.keugeugeuk.auth

import android.content.Context
import android.content.SharedPreferences

object UserManager {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USERNAME = "username"
    private const val KEY_PASSWORD = "password"
    private const val KEY_POINT = "point"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun login(username: String, password: String): Boolean {
        val savedUsername = prefs.getString(KEY_USERNAME, null)
        val savedPassword = prefs.getString(KEY_PASSWORD, null)

        return if (username == savedUsername && password == savedPassword) {
            prefs.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()
            true
        } else false
    }

    fun signup(username: String, password: String): Boolean {
        val savedUser = prefs.getString(KEY_USERNAME, null)
        return if (savedUser == null) {
            prefs.edit()
                .putString(KEY_USERNAME, username)
                .putString(KEY_PASSWORD, password)
                .apply()
            true
        } else {
            false // 이미 아이디가 존재
        }
    }

    fun logout() {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, false).apply()
    }

    fun deleteAccount() {
        prefs.edit().clear().apply()
    }

    // 포인트 관리
    fun getPoint(): Int = prefs.getInt(KEY_POINT, 0)

    fun addPoint(amount: Int) {
        val current = getPoint()
        prefs.edit().putInt(KEY_POINT, current + amount).apply()
    }

    fun resetPoint() {
        prefs.edit().putInt(KEY_POINT, 0).apply()
    }
}