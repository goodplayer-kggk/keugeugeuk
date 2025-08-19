package com.goodplayer.keugeugeuk.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.goodplayer.keugeugeuk.MainActivity
import com.goodplayer.keugeugeuk.R

class LoginActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText

    // ✅ 회원가입 Activity 호출 후 결과 받기
    private val signUpLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val username = result.data?.getStringExtra("username")
                etUsername.setText(username)// 아이디 자동 입력
                etPassword.requestFocus()
                Toast.makeText(this, "회원가입 완료. 로그인 해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.editTextId)
        etPassword = findViewById(R.id.editTextPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnSignUp: Button = findViewById(R.id.btnSignup)
        val btnGuest = findViewById<Button>(R.id.btnGuest)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = UserManager.login(username, password)
            if (success) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "아이디 또는 비밀번호가 잘못되었습니다", Toast.LENGTH_SHORT).show()
            }
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            signUpLauncher.launch(intent)
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