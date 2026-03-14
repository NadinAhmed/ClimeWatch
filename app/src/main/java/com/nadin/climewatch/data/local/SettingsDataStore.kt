package com.nadin.climewatch.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "weather_settings")

class SettingsDataStore(private val context: Context) {
    companion object {
        // Keys
        val LOCATION_MODE_KEY = stringPreferencesKey("location_mode")   // "gps" | "map"
        val SELECTED_LAT_KEY = doublePreferencesKey("selected_lat")
        val SELECTED_LON_KEY = doublePreferencesKey("selected_lon")
        val UNITS_KEY =
            stringPreferencesKey("units")            // "metric" | "imperial" | "standard"
        val LANGUAGE_KEY = stringPreferencesKey("language")         // "en" | "ar"
    }

    val locationMode: Flow<String> = context.dataStore.data
        .map { it[LOCATION_MODE_KEY] ?: "gps" }

    suspend fun saveLocationMode(mode: String) {
        context.dataStore.edit { it[LOCATION_MODE_KEY] = mode }
    }


    val selectedLat : Flow<Double> = context.dataStore.data
        .map { it[SELECTED_LAT_KEY] ?: 0.0 }

    val selectedLon : Flow<Double> = context.dataStore.data
        .map { it[SELECTED_LON_KEY] ?: 0.0 }

    suspend fun saveSelectedLocation(lat: Double, lon: Double) {
        context.dataStore.edit {
            it[SELECTED_LAT_KEY] = lat
            it[SELECTED_LON_KEY] = lon
        }
    }


    val units : Flow<String> = context.dataStore.data
        .map { it[UNITS_KEY] ?: "metric" }

    suspend fun saveUnits(units: String) {
        context.dataStore.edit { it[UNITS_KEY] = units }
    }


    val language: Flow<String> = context.dataStore.data
        .map { it[LANGUAGE_KEY] ?: "en" }

    suspend fun saveLanguage(language: String) {
        context.dataStore.edit { it[LANGUAGE_KEY] = language }
    }
}