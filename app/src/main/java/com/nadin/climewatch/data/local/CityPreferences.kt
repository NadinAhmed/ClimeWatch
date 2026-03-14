package com.nadin.climewatch.data.local

import android.content.Context
import androidx.core.content.edit

object CityPreferences {
    private const val PREFS_NAME = "climewatch_prefs"
    private const val KEY_CURRENT_CITY = "key_current_city"

    fun saveCurrentCity(context: Context, cityName: String) {
        context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(KEY_CURRENT_CITY, cityName)
            }
    }

    fun getCurrentCity(context: Context): String? {
        return context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_CURRENT_CITY, null)
    }
}
