package com.openweatherapp

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(private val _context: Context) {
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val PRIVATE_MODE = 0
    fun setKeyValue(key: String?, value: String?) {
        editor.putString(key, value)
        editor.commit()
        editor.apply()
    }

    fun getValue(key: String?, defaultValue: String?): String? {
        return pref.getString(key, defaultValue)
    }

    companion object {
        const val CITY = "city"
        const val STATE = "state"
        const val DIST = "dist"
        const val PINCODE = "pincode"
        const val COUNTRY = "COUNTRY"
        private const val PREF_NAME = "WeartherAPP"
    }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}