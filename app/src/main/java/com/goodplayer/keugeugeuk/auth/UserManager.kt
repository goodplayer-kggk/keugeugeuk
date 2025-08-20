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

object UserManager {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USERNAME = "username"
    private const val KEY_PASSWORD = "password"
    private const val KEY_POINT = "point"
    private const val KEY_SOCIAL_PROVIDER = "social_provider"
    private const val KEY_USER_TOKEN = "user_token"

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
//            val token = withContext(Dispatchers.IO) {
//                suspendCoroutine<String?> { cont ->
//                    UserApiClient.instance.loginWithKakaoAccount(activity) { token, error ->
//                        if (error != null) cont.resume(null)
//                        else cont.resume(token?.accessToken)
//                    }
//                }
//            } ?: return false
//
//            saveLoginState("kakao", token)
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
        if(!provider.isNullOrBlank()){
            when(provider) {
                "google" -> {
                    val credentialManager = CredentialManager.create(activity.applicationContext)
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            credentialManager.clearCredentialState(request = ClearCredentialStateRequest())
                            Log.d("UserManager", "requested to googel to clear credential")
                        } catch (e: Exception) {
                            Log.d("UserManager", "clearCredential error: ${e.message}")
                        }
                    }
                }
                "kakao" -> {

                }
            }
            prefs.edit().remove(KEY_SOCIAL_PROVIDER)
            prefs.edit().remove(KEY_USER_TOKEN)
        }

        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, false).apply()
    }

    fun saveLoginState(provider: String, idToken: String) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_USER_TOKEN, idToken)
            .putString(KEY_SOCIAL_PROVIDER, provider)
            .apply()
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