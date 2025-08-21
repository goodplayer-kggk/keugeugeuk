package com.goodplayer.keugeugeuk.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.*
import com.goodplayer.keugeugeuk.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kakao.sdk.user.UserApiClient
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class PointHistory(val amount: Int, val reason: String)

object UserManager {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USERNAME = "username"
    private const val KEY_USER_NICKNAME = "user_nickname"
    private const val KEY_PASSWORD = "password"
    private const val KEY_USER_POINT = "user_point"
    private const val KEY_USER_HISTORY = "user_history"
    private const val KEY_SOCIAL_PROVIDER = "social_provider"
    private const val KEY_USER_TOKEN = "user_token"

    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

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
            //TODO: Backend 서버와 Point/History 관련 동기화
            prefs.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()
            true
        } else false
    }

    suspend fun loginSocial(provider: String, activity: Activity): Boolean {
        return when (provider.lowercase()) {
            "google" -> signInWithGoogle(activity)
            "kakao" -> signInWithKakao(activity)
            else -> false
        }
    }

    // ✅ Google 로그인 처리
    private suspend fun signInWithGoogle(activity: Activity): Boolean {
        val credentialManager = CredentialManager.create(activity)
        val googleIdOption = GetSignInWithGoogleOption.Builder(
            activity.getString(R.string.web_client_id)
        ).build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(activity.applicationContext, request)
            if (result.credential is CustomCredential &&
                result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                val tokenCred = GoogleIdTokenCredential.createFrom(result.credential.data)
                val idToken = tokenCred.idToken

                //TODO: Backend 서버와 Point/History 관련 동기화
                // 서버 검증 및 저장
                saveLoginState("google", idToken)
            } else {
                Log.d("UserManager", "credential is wrong")
                return false
            }
            true
        } catch (e: Exception) {
            Log.d("UserManager", "Google login failed: ${e.message}")
            false
        }
    }

    // ✅ Kakao 로그인 처리 (SDK 사용 예시)
    private suspend fun signInWithKakao(activity: Activity): Boolean {
        return try {
            val token = withContext(Dispatchers.IO) {
                suspendCoroutine<String?> { cont ->
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
                        UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
                            if (error != null) cont.resume(null) //TODO: Backend 서버와 Point/History 관련 동기화
                            else cont.resume(token?.accessToken)
                        }
                    } else {
                        UserApiClient.instance.loginWithKakaoAccount(activity) { token, error ->
                            if (error != null) cont.resume(null) //TODO: Backend 서버와 Point/History 관련 동기화
                            else cont.resume(token?.accessToken)
                        }
                    }
                }
            } ?: return false

            saveLoginState("kakao", token)
            true
        } catch (e: Exception) {
            Log.e("UserManager", "Kakao login failed: ${e.message}")
            false
        }
    }

    // ✅ 로그아웃
    @SuppressLint("CommitPrefEdits")
    fun logout(activity: Activity) {
        val provider = prefs.getString(KEY_SOCIAL_PROVIDER, null)
        if(!provider.isNullOrBlank()) {
            when (provider) {
                "google" -> {
                    val credentialManager = CredentialManager.create(activity.applicationContext)
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            credentialManager.clearCredentialState(
                                request = ClearCredentialStateRequest()
                            )
                            Log.d("UserManager", "requested to googel to clear credential")
                        } catch (e: Exception) {
                            Log.d("UserManager", "clearCredential error: ${e.message}")
                        }
                    }
                }

                "kakao" -> {
                    UserApiClient.instance.logout { error ->
                        if (error != null) {
                            Log.e("UserManager", "Kakao logout failed: ${error.message}")
                        } else {
                            Log.d("UserManager", "Kakao logout success")
                        }
                    }
                }
            }
        }
        // 🔑 로컬 데이터 전부 삭제 (포인트, 히스토리 포함)
        prefs.edit().clear().apply()
        Log.d("UserManager", "Local prefs cleared after logout")
    }

    fun saveLoginState(provider: String, idToken: String) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_USER_TOKEN, idToken)
            .putString(KEY_USER_NICKNAME, provider + " boy") //TODO: generate nickname randomly
            .putString(KEY_SOCIAL_PROVIDER, provider)
            .apply()
    }

    fun signup(username: String, password: String): Boolean {
        val savedUser = prefs.getString(KEY_USERNAME, null)
        return if (savedUser == null) {
            prefs.edit()
                .putString(KEY_USERNAME, username)
                .putString(KEY_USER_NICKNAME, "달리는 크그극") //TODO: generate nickname randomly with username
                .putString(KEY_PASSWORD, password)
                .apply()
            true
        } else {
            false // 이미 아이디가 존재
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun deleteAccount(activity: Activity) {
        val provider = prefs.getString(KEY_SOCIAL_PROVIDER, null)
        if (!provider.isNullOrBlank()) {
            when (provider) {
                "google" -> {
                    // 구글은 별도 unlink 개념 없음, credential만 정리
                    val credentialManager = CredentialManager.create(activity.applicationContext)
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            credentialManager.clearCredentialState(
                                request = ClearCredentialStateRequest()
                            )
                            Log.d("UserManager", "Google account cleared")
                        } catch (e: Exception) {
                            Log.d("UserManager", "Google deleteAccount error: ${e.message}")
                        }
                    }
                }
                "kakao" -> {
                    UserApiClient.instance.unlink { error ->
                        if (error != null) {
                            Log.e("UserManager", "Kakao unlink failed: ${error.message}")
                        } else {
                            Log.d("UserManager", "Kakao unlink success")
                        }
                    }
                }
            }
        }

        // 🔑 서버 API 호출로 계정 삭제 (향후 구현 예정)
        // e.g., ApiClient.deleteUserAccount(serverToken)

        // 🔑 로컬 데이터 전부 삭제
        prefs.edit().clear().apply()
        Log.d("UserManager", "Local prefs cleared after account deletion")
    }

    fun getNickname(): String? = prefs.getString(KEY_USER_NICKNAME, null)
    fun getUserName(): String? = prefs.getString(KEY_USERNAME, null)

    // ----------------------
    // ✅ Point 관리
    // ----------------------
    fun getPoints(): Int = prefs.getInt(KEY_USER_POINT, 0)

    fun addPoints(amount: Int, reason: String = "Ad") {
        if (amount <= 0) return
        val newVal = getPoints() + amount
        prefs.edit().putInt(KEY_USER_POINT, newVal).apply()
        saveHistory(PointHistory(amount, reason))
    }

    fun deductPoints(amount: Int, reason: String = "Exchange"): Boolean {
        if (amount <= 0) return false
        val cur = getPoints()
        if (cur < amount) return false
        prefs.edit().putInt(KEY_USER_POINT, cur - amount).apply()
        saveHistory(PointHistory(-amount, reason))
        return true
    }

    // ----------------------
    // ✅ History 관리
    // ----------------------
    fun getHistory(): List<PointHistory> {
        val json = prefs.getString(KEY_USER_HISTORY, "[]") ?: "[]"
        val type = object: TypeToken<List<PointHistory>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveHistory(entry: PointHistory) {
        val list = getHistory().toMutableList()
        list.add(0, entry)
        prefs.edit().putString(KEY_USER_HISTORY, gson.toJson(list)).apply()
    }

    fun clearHistory() {
        prefs.edit().remove(KEY_USER_HISTORY).apply()
    }
}