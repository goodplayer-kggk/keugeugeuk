package com.goodplayer.keugeugeuk.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.goodplayer.keugeugeuk.R

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val etUsername: EditText = findViewById(R.id.etUsername)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = UserManager.signup(username, password)
            if (success) {
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                // ✅ 회원가입된 아이디를 LoginActivity로 전달
                val resultIntent = Intent().apply {
                    putExtra("username", username)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "이미 존재하는 아이디입니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}