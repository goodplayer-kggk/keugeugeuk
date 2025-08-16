package com.goodplayer.keugeugeuk.data.exchange.model

data class RewardItem(
    val id: String,
    val name: String,
    val description: String,
    val costPoints: Int,
    val vendor: String,
    val imageRes: Int // drawable 리소스 id
)
