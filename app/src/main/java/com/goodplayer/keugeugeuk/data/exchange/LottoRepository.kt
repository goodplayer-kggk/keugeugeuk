package com.goodplayer.keugeugeuk.data.exchange

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
    val numbers: List<Int> // size = 6, values 1..45
)

interface LottoApi {
    @GET("common.do?method=getLottoNumber")
    suspend fun getLottoNumbers(@Query("drwNo") drwNo: Int): LottoResponse
}

object LottoRecommender {

    fun recommendFrom(draws: List<LottoDraw>): List<Int> {
        val freq = IntArray(46) // 1..45
        draws.forEach { d -> d.numbers.forEach { n -> freq[n]++ } }

        val g34   = mutableListOf<Int>() // 3~4회
        val g57   = mutableListOf<Int>() // 5~7회
        val lowHi = mutableListOf<Int>() // 1~2회 + 8회이상

        for (n in 1..45) {
            when (freq[n]) {
                in 3..4   -> g34 += n
                in 5..7   -> g57 += n
                else      -> if (freq[n] in 1..2 || freq[n] >= 8) lowHi += n
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

        pickFrom(g34, 2)   // 3~4회에서 2개
        pickFrom(g57, 4)   // 5~7회에서 2개 + (다 못 뽑으면 다음 단계에서 보충)
        while (pick.size < 4 && g57.isNotEmpty()) {
            val n = g57.random()
            pick += n
            g57.remove(n)
        }
        while (pick.size < 4 && g34.isNotEmpty()) { // 보충
            val n = g34.random()
            pick += n
            g34.remove(n)
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

    suspend fun fetchLast100Results(latestDraw: Int): List<LottoResponse> =
        withContext(Dispatchers.IO) {
            val results = mutableListOf<LottoResponse>()
            for (i in (latestDraw - 99)..latestDraw) {
                try {
                    val res = api.getLottoNumbers(i)
                    if (res.returnValue == "success") results.add(res)
                } catch (_: Exception) { }
            }
            results
        }

    fun recommendNumbers(results: List<LottoResponse>): List<Int> {
        var draws = mutableListOf<LottoDraw>()

        results.forEach { r ->
            val nums = mutableListOf<Int>()
            nums += r.drwtNo1
            nums += r.drwtNo2
            nums += r.drwtNo3
            nums += r.drwtNo4
            nums += r.drwtNo5
            nums += r.drwtNo6

            val draw = LottoDraw(r.drwNo, nums)
            draws += draw
        }
        return LottoRecommender.recommendFrom(draws)
    }
}
