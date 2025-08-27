package com.goodplayer.keugeugeuk.data.exchange

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

object LottoManager {
    private const val PREF_NAME = "lotto_pref"
    private const val KEY_LAST_FETCHED_DRAW_NO = "last_fetched_draw_no"
    private const val KEY_RECENT_100_DATA = "recent_100_data"
    private const val KEY_RECOMMEND_HISTORY = "recommend_history"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // ✅ 최신 회차 저장 & 불러오기
    fun getLastFetchedDrawNo(): Int {
        return prefs.getInt(KEY_LAST_FETCHED_DRAW_NO, 1087) // 기본값 1087, 100개 데이터로 초기화
    }

    fun setLastFetchedDrawNo(drawNo: Int) {
        prefs.edit().putInt(KEY_LAST_FETCHED_DRAW_NO, drawNo).apply()
    }

    // ✅ 최근 100회 데이터 저장 (JSONArray로 직렬화)
    fun saveRecent100Data(dataJson: String) {
        prefs.edit().putString(KEY_RECENT_100_DATA, dataJson).apply()
    }

    fun loadRecent100Data(): String {
        val dataJson = prefs.getString(KEY_RECENT_100_DATA, "") ?: return ""
        return dataJson
    }

    // ✅ 추천 번호 기록 (추천시 번호와 회차 저장)
    fun addRecommendHistory(drawNo: Int, numbers: List<Int>) {
        val historyJson = JSONArray(prefs.getString(KEY_RECOMMEND_HISTORY, "[]"))

        val record = JSONObject().apply {
            put("drawNo", drawNo)
            put("numbers", JSONArray(numbers))
            put("timestamp", System.currentTimeMillis())
        }

        historyJson.put(record)

        // ✅ 최대 50개까지만 유지 (앞쪽 오래된 기록 삭제)
        if (historyJson.length() > 50) {
            val trimmed = JSONArray()
            for (i in historyJson.length() - 50 until historyJson.length()) {
                trimmed.put(historyJson.get(i))
            }
            prefs.edit().putString(KEY_RECOMMEND_HISTORY, trimmed.toString()).apply()
        } else {
            prefs.edit().putString(KEY_RECOMMEND_HISTORY, historyJson.toString()).apply()
        }
    }

    fun loadRecommendHistory(): JSONArray {
        return JSONArray(prefs.getString(KEY_RECOMMEND_HISTORY, "[]"))
    }

    fun clearData(){
        prefs.edit().clear().apply()
    }
}