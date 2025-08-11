package com.goodplayer.keugeugeuk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.goodplayer.keugeugeuk.databinding.ActivityTipDetailBinding

class TipDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTipDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("tip_id", 0)
        when (id) {
            0 -> {
                binding.tvTitle.text = "아침 스트레칭 5분"
                binding.tvBody.text = "상세: 가벼운 스트레칭 5가지..."
            }
            1 -> {
                binding.tvTitle.text = "냉장고 정리 꿀팁"
                binding.tvBody.text = "상세: 라벨링, 빠른 소비법 등..."
            }
            else -> {
                binding.tvTitle.text = "간단 레시피"
                binding.tvBody.text = "상세: 재료 준비와 조리법..."
            }
        }
    }
}