package com.yenaly.circularrevealswitch

import android.content.Context

/**
 * @project CircularRevealSwitch
 * @author Yenaly Liew
 * @time 2024/04/28 028 17:26
 */
object Prefs {

    fun Context.sharedPrefs() = getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun Context.savePref(key: String, value: Any) {
        val editor = sharedPrefs().edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
        editor.apply()
    }

    inline fun <reified T> Context.getPref(key: String, defaultValue: T): T {
        return when (T::class) {
            String::class -> sharedPrefs().getString(key, defaultValue as String) as T
            Int::class -> sharedPrefs().getInt(key, defaultValue as Int) as T
            Boolean::class -> sharedPrefs().getBoolean(key, defaultValue as Boolean) as T
            Float::class -> sharedPrefs().getFloat(key, defaultValue as Float) as T
            Long::class -> sharedPrefs().getLong(key, defaultValue as Long) as T
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
}