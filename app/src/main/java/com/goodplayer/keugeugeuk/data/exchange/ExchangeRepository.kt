package com.goodplayer.keugeugeuk.data.exchange

import com.goodplayer.keugeugeuk.R
import com.goodplayer.keugeugeuk.data.exchange.model.ExchangeResult
import com.goodplayer.keugeugeuk.data.exchange.model.RewardItem
import kotlinx.coroutines.delay
import kotlin.random.Random

class ExchangeRepository {

    // 데모용 인메모리 재고 (앱 실행 시 초기화)
    private val inventory = mutableMapOf(
        "coffee_americano" to 50,
        "cvs_5000" to 30,
        "movie_ticket" to 20
    )

    // 데모용 상품 목록
    suspend fun getRewards(): List<RewardItem> {
        delay(250) // 로딩 감
        return listOf(
            RewardItem(
                id = "coffee_americano",
                name = "아메리카노",
                description = "메가MGC/이디야/파스쿠찌 중 택1 (예시)",
                costPoints = 300,
                vendor = "커피 제휴",
                imageRes = R.drawable.ic_reward_coffee
            ),
            RewardItem(
                id = "cvs_5000",
                name = "편의점 5,000원",
                description = "GS25/CU 사용 가능 (예시)",
                costPoints = 1200,
                vendor = "편의점 제휴",
                imageRes = R.drawable.ic_reward_gift
            ),
            RewardItem(
                id = "movie_ticket",
                name = "영화 예매권",
                description = "CGV/롯데시네마 (예시)",
                costPoints = 1500,
                vendor = "영화 제휴",
                imageRes = R.drawable.ic_reward_movie
            )
        )
    }

    // 데모용 교환 API 시뮬레이션
    suspend fun exchangeReward(rewardId: String): ExchangeResult {
        delay(500) // 네트워크 지연 흉내
        val stock = inventory[rewardId] ?: 0
        if (stock <= 0) {
            return ExchangeResult(false, message = "재고가 없습니다.")
        }
        // 재고 차감
        inventory[rewardId] = stock - 1

        // 쿠폰 코드 생성 (샘플)
        val code = buildString {
            append(rewardId.take(4).uppercase())
            append('-')
            append(Random.nextInt(1000, 9999))
            append('-')
            append(Random.nextInt(1000, 9999))
        }
        return ExchangeResult(true, couponCode = code, message = "교환 완료")
    }
}