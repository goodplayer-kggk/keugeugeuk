package com.goodplayer.keugeugeuk.ui.scratch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.goodplayer.keugeugeuk.MainActivity
import com.goodplayer.keugeugeuk.R

class ScratchResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scratch_result)

        val ivResultIcon = findViewById<ImageView>(R.id.ivResultIcon)
        val tvResultMessage = findViewById<TextView>(R.id.tvResultMessage)
        val btnShareResult = findViewById<Button>(R.id.btnShareResult)
        val btnGoHome = findViewById<Button>(R.id.btnGoHome)

        // 인텐트로 전달받은 결과 데이터
        val resultMessage = intent.getStringExtra("result_message") ?: "결과가 없습니다."
        val resultIconRes = intent.getIntExtra("result_icon", R.drawable.ic_launcher_foreground) //ic_reward 샘플이미지

        tvResultMessage.text = resultMessage
        ivResultIcon.setImageResource(resultIconRes)

        // 공유 버튼 동작
        btnShareResult.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "긁긁긁 결과")
                putExtra(Intent.EXTRA_TEXT, "내 긁긁긁 결과: $resultMessage")
            }
            startActivity(Intent.createChooser(shareIntent, "결과 공유하기"))
        }

        // 홈으로 이동 버튼
        btnGoHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}