package com.goodplayer.keugeugeuk.auth

import android.content.Context
import android.content.SharedPreferences

class UserManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LOGGED_IN_USER = "logged_in_user"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_PASSWORD = "user_pw"
    }

    fun login(userId: String, password: String): Boolean {
        val savedId = prefs.getString(KEY_USER_ID, null)
        val savedPw = prefs.getString(KEY_USER_PASSWORD, null)

        return if (savedId == userId && savedPw == password) {
            prefs.edit().putBoolean(KEY_LOGGED_IN_USER, true).apply()
            true
        } else {
            false
        }
    }

    fun signup(userId: String, password: String): Boolean {
        if (prefs.contains(KEY_USER_ID)) {
            return false // 이미 존재하는 계정
        }
        prefs.edit()
            .putString(KEY_USER_ID, userId)
            .putString(KEY_USER_PASSWORD, password)
            .apply()
        return true
    }

    fun logout(context: Context) {
        prefs.edit().clear().apply()
    }

    fun setLoggedInUser(userId: String) {
        prefs.edit().putString(KEY_LOGGED_IN_USER, userId).apply()
    }

    fun getLoggedInUser(): String? {
        return prefs.getString(KEY_LOGGED_IN_USER, null)
    }

    fun clearUser() {
        prefs.edit().remove(KEY_LOGGED_IN_USER).apply()
    }

    fun isLoggedIn(): Boolean {
        return getLoggedInUser() != null
    }
}