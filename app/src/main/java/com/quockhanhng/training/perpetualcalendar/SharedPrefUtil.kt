package com.quockhanhng.training.perpetualcalendar

import android.content.Context
import android.content.SharedPreferences

object SharedPrefUtil {

    const val KEY_LIST_NOTE = "KEY_LIST_NOTE"
    private const val PREFS_NOTE = "PREFS_NOTE"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NOTE, Context.MODE_PRIVATE)
    }

    fun getData(key: String): String? {
        return prefs.getString(key, "")
    }

    fun setData(key: String, value: String) {
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }
}