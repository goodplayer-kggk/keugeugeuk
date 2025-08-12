package com.goodplayer.keugeugeuk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.goodplayer.keugeugeuk.databinding.ActivityTipDetailBinding

class TipDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTipDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent 데이터 받기
        val title = intent.getStringExtra("tip_title") ?: "제목 없음"
        val description = intent.getStringExtra("tip_description") ?: "내용 없음"
        val imageRes = intent.getIntExtra("tip_image", R.drawable.ic_launcher_foreground) // 샘플이미지

        // UI 반영
        binding.tvTitle.text = title
        binding.tvDescription.text = description
        binding.ivTipImage.setImageResource(imageRes)

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 공유 버튼
        binding.btnShare.setOnClickListener {
            val shareText = "📌 $title\n\n$description"
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "생활 팁 공유하기"))
        }
    }
}