package com.goodplayer.keugeugeuk.data

import kotlin.random.Random

object RewardGenerator {
    fun generateReward(): Int {
        // 랜덤 2..10, 기본 균일 분포. 필요 시 가중치로 변경 가능
        return Random.nextInt(2, 11)
    }
}