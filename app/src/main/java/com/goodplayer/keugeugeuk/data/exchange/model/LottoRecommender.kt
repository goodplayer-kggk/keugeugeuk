package com.goodplayer.keugeugeuk.data.exchange.model

object LottoRecommender {
    fun recommendFrom(draws: List<LottoDraw>): List<Int> {
        val freq = IntArray(46) // 1..45
        draws.forEach { d -> d.numbers.forEach { n -> freq[n]++ } }

        val g57   = mutableListOf<Int>() // 5~7회
        val g89   = mutableListOf<Int>() // 8~9회
        val lowHi = mutableListOf<Int>() // 1~4회 + 9회이상

        for (n in 1..45) {
            when (freq[n]) {
                in 5..7 -> g57 += n
                in 8..9 -> g89 += n
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

        pickFrom(g57, 3)   // 11~14회에서 3개
        pickFrom(g89, 5)   // 15~19회에서 2개 + (다 못 뽑으면 다음 단계에서 보충)
        while (pick.size < 5 && g89.isNotEmpty()) {
            val n = g89.random()
            pick += n
            g89.remove(n)
        }
        while (pick.size < 5 && g57.isNotEmpty()) { // 보충
            val n = g57.random()
            pick += n
            g57.remove(n)
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
