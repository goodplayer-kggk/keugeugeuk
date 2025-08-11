package com.goodplayer.keugeugeuk

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PointsManager(private val ctx: Context) {
    private val prefs = ctx.getSharedPreferences("krgeuk_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getPoints(): Int = prefs.getInt("points", 0)

    fun addPoints(amount: Int, reason: String = "Ad") {
        val cur = getPoints() + amount
        prefs.edit().putInt("points", cur).apply()
        saveHistory(PointHistory(amount, reason))
    }

    fun consumePoints(amount: Int, reason: String = "Exchange"): Boolean {
        val cur = getPoints()
        return if (cur >= amount) {
            prefs.edit().putInt("points", cur - amount).apply()
            saveHistory(PointHistory(-amount, reason))
            true
        } else false
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
