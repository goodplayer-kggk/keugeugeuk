package com.goodplayer.keugeugeuk.data.exchange

import com.goodplayer.keugeugeuk.data.exchange.model.LottoApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.goodplayer.keugeugeuk.data.exchange.model.LottoResponse
import com.goodplayer.keugeugeuk.data.exchange.model.LottoDraw
import com.goodplayer.keugeugeuk.data.exchange.model.LottoRecommender

class LottoRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.dhlottery.co.kr/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(LottoApi::class.java)
    private val noOfDraws = 52

    suspend fun fetchLatestDrawNo(): Int {
        var latest = LottoManager.getLastFetchedDrawNo()
        var next = latest

        while (true) {
            val response = api.getLottoNumbers(next)
            if (response.returnValue == "fail") {
                next--
                break
            }
            next++
        }
        return next
    }

    suspend fun fetchLastNResults(latestDraw: Int, savedDraw: Int): List<LottoResponse> =
        withContext(Dispatchers.IO) {
            val results = mutableListOf<LottoResponse>()
            var diff = latestDraw - savedDraw
            diff = if(diff <= noOfDraws ) diff else noOfDraws

            for (i in (latestDraw - diff + 1) .. latestDraw ) {
                try {
                    val res = api.getLottoNumbers(i)
                    if (res.returnValue == "success") results.add(res)
                } catch (_: Exception) { }
            }

            if(results.size == noOfDraws) {
                results
            } else {
                var recLottoResponse = LottoManager.loadRecent100Data()
                val jsonRecLottoResponse = Gson().fromJson<List<LottoResponse>>(
                    recLottoResponse,
                    object : TypeToken<List<LottoResponse>>() {}.type
                )

                for(j in jsonRecLottoResponse) {
                    results.add(j)
                    if (results.size >= noOfDraws)
                        break
                }
                results
            }
        }

    fun converterResponseToDraw(results: List<LottoResponse>): List<LottoDraw> {
        var draws = mutableListOf<LottoDraw>()

        results.forEach { r ->
            val nums = mutableListOf<Int>()
            nums += r.drwtNo1
            nums += r.drwtNo2
            nums += r.drwtNo3
            nums += r.drwtNo4
            nums += r.drwtNo5
            nums += r.drwtNo6

            val draw = LottoDraw(r.drwNo, nums, r.bnusNo)
            draws += draw
        }
        return draws
    }

    fun recommendNumbers(results: List<LottoResponse>): List<Int> {
        return LottoRecommender.recommendFrom(converterResponseToDraw(results))
    }
}
