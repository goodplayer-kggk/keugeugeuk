package com.goodplayer.keugeugeuk.data.exchange.model

data class LottoDraw(
    val round: Int,
    val numbers: List<Int>, // size = 6, values 1..45
    val bonus: Int
)

