package com.goodplayer.keugeugeuk.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.goodplayer.keugeugeuk.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "설정"

        // 뒤로가기 버튼 처리
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // TODO: 사용자 정보, 계정 탈퇴, 알림 설정 등 추가
    }
}