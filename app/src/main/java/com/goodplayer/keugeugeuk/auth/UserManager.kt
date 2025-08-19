package com.goodplayer.keugeugeuk.auth

import android.content.Context
import android.content.SharedPreferences

object UserManager {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_USERNAME = "username"
    private const val KEY_PASSWORD = "password"
    private const val KEY_LOGGED_IN = "logged_in"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_LOGGED_IN, false)
    }

    fun login(username: String, password: String): Boolean {
        val savedUser = prefs.getString(KEY_USERNAME, null)
        val savedPw = prefs.getString(KEY_PASSWORD, null)

        return if (savedUser == username && savedPw == password) {
            prefs.edit().putBoolean(KEY_LOGGED_IN, true).apply()
            true
        } else {
            false
        }
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
        prefs.edit().putBoolean(KEY_LOGGED_IN, false).apply()
    }
}