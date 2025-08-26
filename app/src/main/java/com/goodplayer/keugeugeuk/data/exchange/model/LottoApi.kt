package com.goodplayer.keugeugeuk.data.exchange.model

import retrofit2.http.GET
import retrofit2.http.Query

interface LottoApi {
    @GET("common.do?method=getLottoNumber")
    suspend fun getLottoNumbers(@Query("drwNo") drwNo: Int): LottoResponse
}