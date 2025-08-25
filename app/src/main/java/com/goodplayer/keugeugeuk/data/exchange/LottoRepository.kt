package com.goodplayer.keugeugeuk.data.exchange

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET
import retrofit2.http.Query

data class LottoResponse(
    val returnValue: String?,
    val drwNo: Int,
    val drwtNo1: Int,
    val drwtNo2: Int,
    val drwtNo3: Int,
    val drwtNo4: Int,
    val drwtNo5: Int,
    val drwtNo6: Int,
    val bnusNo: Int
)

data class LottoDraw(
    val round: Int,
    val numbers: List<Int>, // size = 6, values 1..45
    val bonus: Int
)

interface LottoApi {
    @GET("common.do?method=getLottoNumber")
    suspend fun getLottoNumbers(@Query("drwNo") drwNo: Int): LottoResponse
}

object LottoRecommender {

    fun recommendFrom(draws: List<LottoDraw>): List<Int> {
        val freq = IntArray(46) // 1..45
        draws.forEach { d -> d.numbers.forEach { n -> freq[n]++ } }

        val g1114   = mutableListOf<Int>() // 11~14회
        val g1519   = mutableListOf<Int>() // 15~19회
        val lowHi = mutableListOf<Int>() // 1~10회 + 19회이상

        for (n in 1..45) {
            when (freq[n]) {
                in 11..14 -> g1114 += n
                in 15..19 -> g1519 += n
                else -> if (freq[n] in 1..11 || freq[n] >= 20) lowHi += n
            }
        }

        val pick = mutableSetOf<Int>()
        fun pickFrom(src: MutableList<Int>, k: Int) {
            val available = src.toMutableList()
            available.shuffle()
            for (x in available) {
                if (pick.size >= k) break
                pick += x
            }
        }

        // 부족 시 백업: 전체에서 채우기
        fun fillAny() {
            val all = (1..45).toMutableList()
            all.shuffle()
            for (n in all) {
                if (pick.size >= 6) break
                pick += n
            }
        }

        pickFrom(g1114, 3)   // 11~14회에서 3개
        pickFrom(g1519, 5)   // 15~19회에서 2개 + (다 못 뽑으면 다음 단계에서 보충)
        while (pick.size < 5 && g1519.isNotEmpty()) {
            val n = g1519.random()
            pick += n
            g1519.remove(n)
        }
        while (pick.size < 5 && g1114.isNotEmpty()) { // 보충
            val n = g1114.random()
            pick += n
            g1114.remove(n)
        }

        // 나머지 2개: 저빈도/고빈도(=lowHi)에서
        val remainNeed = 6 - pick.size
        if (remainNeed > 0) {
            val pool = lowHi.ifEmpty { (1..45).toMutableList() }
            pool.shuffle()
            for (n in pool) {
                if (pick.size >= 6) break
                pick += n
            }
        }

        if (pick.size < 6) fillAny()

        return pick.toList().shuffled() // 매 호출 랜덤성
    }
}

class LottoRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.dhlottery.co.kr/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(LottoApi::class.java)

    suspend fun fetchLatestDrawNo(): Int {
        var latest = LottoManager.getLastFetchedDrawNo()
        var next = latest

        while (true) {
            val response = api.getLottoNumbers(next)
            if (response.returnValue == "fail") break
            next++
        }
        LottoManager.setLastFetchedDrawNo(next-1)
        return next-1
    }

    suspend fun fetchLast100Results(latestDraw: Int, savedDraw: Int): List<LottoResponse> =
        withContext(Dispatchers.IO) {
            val results = mutableListOf<LottoResponse>()
            var diff = latestDraw - savedDraw
            diff = if(diff <= 103) diff else 103

            for (i in (latestDraw - diff)..latestDraw) {
                try {
                    val res = api.getLottoNumbers(i)
                    if (res.returnValue == "success") results.add(res)
                } catch (_: Exception) { }
            }

            if(results.size == 104)
                results
            else {
                var recLottoResponse = LottoManager.loadRecent100Data()
                val jsonRecLottoResponse = Gson().fromJson<List<LottoResponse>>(
                    recLottoResponse,
                    object : TypeToken<List<LottoResponse>>() {}.type
                )
                jsonRecLottoResponse.forEach{ j ->
                    results.add(j)
                    if(results.size >= 104)
                        return@forEach
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
