package com.goodplayer.keugeugeuk.ui.settings


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.goodplayer.keugeugeuk.databinding.ActivitySettingBinding
import com.goodplayer.keugeugeuk.R
import com.goodplayer.keugeugeuk.auth.LoginActivity
import com.goodplayer.keugeugeuk.auth.UserManager
import com.goodplayer.keugeugeuk.data.exchange.LottoManager

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
                is SettingItem.Normal -> {
                    when (item.title) {
                        "긁는 소리 변경" -> Toast.makeText(this, "${item.title} 클릭됨", Toast.LENGTH_SHORT).show()
                        "배경 음악 변경" -> Toast.makeText(this, "${item.title} 클릭됨", Toast.LENGTH_SHORT).show()
                        "회원정보 수정" -> Toast.makeText(this, "${item.title} 클릭됨", Toast.LENGTH_SHORT).show()
                        "로그아웃" -> {
                            Toast.makeText(this, "${item.title} 클릭됨", Toast.LENGTH_SHORT).show()
                            UserManager.logout(this@SettingActivity)
                            LottoManager.clearData()
                            navigateToLogin()
                        }
                        "회원 탈퇴" -> {
                            AlertDialog.Builder(this)
                                .setTitle("회원 탈퇴")
                                .setMessage("정말로 탈퇴하시겠습니까?\n탈퇴 시 모든 데이터가 삭제됩니다.")
                                .setPositiveButton("탈퇴") { _, _ ->
                                    Toast.makeText(this, "회원 탈퇴 처리 중...", Toast.LENGTH_SHORT).show()
                                    UserManager.deleteAccount(this@SettingActivity)
                                    LottoManager.clearData()
                                    navigateToLogin()
                                }
                                .setNegativeButton("취소", null)
                                .show()
                        }
                        "공지사항" -> Toast.makeText(this, "${item.title} 클릭됨", Toast.LENGTH_SHORT).show()
                        "도움말 / FAQ" -> Toast.makeText(this, "${item.title} 클릭됨", Toast.LENGTH_SHORT).show()
                        "앱 버전" -> Toast.makeText(this, "${item.title} 클릭됨", Toast.LENGTH_SHORT).show()
                    }
                }
                is SettingItem.Switch -> Toast.makeText(this, "${item.title} : ${item.isChecked}", Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}