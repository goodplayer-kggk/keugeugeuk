package com.goodplayer.keugeugeuk.data.exchange.model

data class ExchangeResult(
    val success: Boolean,
    val couponCode: String? = null,
    val message: String = ""
)