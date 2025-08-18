package com.goodplayer.keugeugeuk

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.goodplayer.keugeugeuk.auth.LoginActivity
import com.goodplayer.keugeugeuk.auth.UserManager

class SplashActivity : AppCompatActivity() {
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        userManager = UserManager(this)
        // 1.5초 후 실행 (로고 애니메이션 시간용)
        Handler(Looper.getMainLooper()).postDelayed({
            if (userManager.isLoggedIn()) {
                // 로그인 되어 있으면 메인으로
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // 로그인 안 되어 있으면 로그인 화면으로
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 1500) // 1500ms = 1.5초
    }
}