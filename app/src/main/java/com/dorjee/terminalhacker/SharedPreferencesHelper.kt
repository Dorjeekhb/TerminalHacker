package com.dorjee.terminalhacker

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("TerminalPrefs", Context.MODE_PRIVATE)

    fun isNodeHacked(node: String): Boolean {
        return prefs.getBoolean("hacked_$node", false)
    }

    fun setNodeAsHacked(node: String) {
        prefs.edit().putBoolean("hacked_$node", true).apply()
    }

    fun setNodeTime(node: String, timeMillis: Long) {
        prefs.edit().putLong("time_$node", timeMillis).apply()
    }

    fun getNodeTime(node: String): Long {
        return prefs.getLong("time_$node", 0L)
    }
}
