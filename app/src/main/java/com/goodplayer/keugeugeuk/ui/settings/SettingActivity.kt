package com.goodplayer.keugeugeuk.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.goodplayer.keugeugeuk.databinding.ActivitySettingBinding
import com.goodplayer.keugeugeuk.R

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "설정"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val items = listOf(
            SettingItem.Header("사운드"),
            SettingItem.Normal(R.drawable.ic_sound, "긁는 소리 변경"),
            SettingItem.Normal(R.drawable.ic_music, "배경 음악 변경"),
            SettingItem.Divider,
            SettingItem.Header("계정"),
            SettingItem.Normal(R.drawable.ic_user, "회원정보 수정"),
            SettingItem.Normal(R.drawable.ic_logout, "로그아웃"),
            SettingItem.Normal(R.drawable.ic_delete, "회원 탈퇴"),
            SettingItem.Divider,
            SettingItem.Header("앱"),
            SettingItem.Normal(R.drawable.ic_notice, "공지사항"),
            SettingItem.Switch(R.drawable.ic_notification, "알림 설정", true),
            SettingItem.Normal(R.drawable.ic_help, "도움말 / FAQ"),
            SettingItem.Normal(R.drawable.ic_info, "앱 버전")
        )

        val adapter = SettingAdapter(items) { item ->
            when(item) {
                is SettingItem.Normal -> Toast.makeText(this, "${item.title} 클릭됨", Toast.LENGTH_SHORT).show()
                is SettingItem.Switch -> Toast.makeText(this, "${item.title} : ${item.isChecked}", Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}