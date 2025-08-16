package com.goodplayer.keugeugeuk.util

import kotlinx.coroutines.delay

object AdManager {
    // simulated ad watch: waits 2s and returns true (watched)
    suspend fun simulateWatch(): Boolean {
        delay(1200) // simulate ad short load + watch
        return true
    }
}