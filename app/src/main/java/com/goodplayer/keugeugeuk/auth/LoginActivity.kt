package com.goodplayer.keugeugeuk.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.goodplayer.keugeugeuk.MainActivity
import com.goodplayer.keugeugeuk.R

class LoginActivity : AppCompatActivity() {

    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userManager = UserManager(this)

        val editTextId = findViewById<EditText>(R.id.editTextId)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val btnGuest = findViewById<Button>(R.id.btnGuest)

        // 로그인 버튼
        btnLogin.setOnClickListener {
            val id = editTextId.text.toString().trim()
            val pw = editTextPassword.text.toString().trim()

            if (userManager.login(id, pw)) {
                Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                goToMain()
            } else {
                Toast.makeText(this, "아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 버튼
        btnSignup.setOnClickListener {
            val id = editTextId.text.toString().trim()
            val pw = editTextPassword.text.toString().trim()

            if (id.isNotEmpty() && pw.isNotEmpty()) {
                val success = userManager.signup(id, pw)
                if (success) {
                    Toast.makeText(this, "회원가입 성공! 로그인하세요.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 게스트 모드 버튼
        btnGuest.setOnClickListener {
            Toast.makeText(this, "게스트 모드 (제한적 이용)", Toast.LENGTH_SHORT).show()
            goToMain()
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}