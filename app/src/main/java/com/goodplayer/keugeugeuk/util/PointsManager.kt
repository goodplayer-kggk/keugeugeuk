package com.goodplayer.keugeugeuk.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.SharedPreferences

class PointsManager(private val ctx: Context) {
    private val prefs: SharedPreferences =
        ctx.getSharedPreferences("points_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getPoints(): Int = prefs.getInt(KEY_TOTAL_POINTS, 0)

    fun addPoints(amount: Int, reason: String = "Ad") {
        if (amount <= 0) return
        val newVal = getPoints() + amount
        prefs.edit().putInt(KEY_TOTAL_POINTS, newVal).apply()
        saveHistory(PointHistory(amount, reason))
    }

    /**
     * 포인트 차감. 성공 시 true
     */
    fun deductPoints(amount: Int, reason: String = "Exchange"): Boolean {
        if (amount <= 0) return false
        val cur = getPoints()
        if (cur < amount) return false
        prefs.edit().putInt(KEY_TOTAL_POINTS, cur - amount).apply()
        saveHistory(PointHistory(-amount, reason))
        return true
    }

    companion object {
        private const val KEY_TOTAL_POINTS = "total_points"
        @Volatile private var instance: PointsManager? = null

        fun get(ctx: Context): PointsManager =
            instance ?: synchronized(this) {
                instance ?: PointsManager(ctx.applicationContext).also { instance = it }
            }
    }

    fun getHistory(): List<PointHistory> {
        val json = prefs.getString("history", "[]") ?: "[]"
        val type = object: TypeToken<List<PointHistory>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveHistory(entry: PointHistory) {
        val list = getHistory().toMutableList()
        list.add(0, entry)
        prefs.edit().putString("history", gson.toJson(list)).apply()
    }
}