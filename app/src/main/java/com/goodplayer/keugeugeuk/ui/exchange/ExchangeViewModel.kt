package com.goodplayer.keugeugeuk.ui.exchange

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goodplayer.keugeugeuk.auth.UserManager
import com.goodplayer.keugeugeuk.data.exchange.ExchangeRepository
import com.goodplayer.keugeugeuk.data.exchange.model.ExchangeResult
import com.goodplayer.keugeugeuk.data.exchange.model.RewardItem
import kotlinx.coroutines.launch

class ExchangeViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ExchangeRepository()

    private val _rewards = MutableLiveData<List<RewardItem>>()
    val rewards: LiveData<List<RewardItem>> = _rewards

    private val _points = MutableLiveData<Int>(UserManager.getPoints())
    val points: LiveData<Int> = _points

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private val _exchangeResult = MutableLiveData<ExchangeResult?>()
    val exchangeResult: LiveData<ExchangeResult?> = _exchangeResult

    fun loadRewards() = viewModelScope.launch {
        _loading.value = true
        _rewards.value = repo.getRewards()
        _loading.value = false
    }

    fun refreshPoints() {
        _points.value = UserManager.getPoints()
    }

    fun exchange(item: RewardItem) = viewModelScope.launch {
        // 포인트 확인 & 차감
        if(UserManager.getPoints() < item.costPoints) {
            _exchangeResult.value = ExchangeResult(false, message = "포인트가 부족합니다.")
            return@launch
        }

        // (샘플) 서버 교환 호출
        _loading.value = true
        val result = repo.exchangeReward(item.id)
        _loading.value = false

        // 성공 시 포인트 차감 및 History 추가
        if (result.success) {
            result.couponCode?.let { UserManager.deductPoints(item.costPoints, result.message + " - " + it) }
            _points.value = UserManager.getPoints()
        }

        _exchangeResult.value = result
    }

    fun clearExchangeResult() {
        _exchangeResult.value = null
    }
}