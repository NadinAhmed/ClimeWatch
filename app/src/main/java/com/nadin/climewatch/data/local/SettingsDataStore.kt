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

class SettingsDataStore(private val context: Context): ISettingsDataStore {
    companion object {
        // Keys
        val LOCATION_MODE_KEY = stringPreferencesKey("location_mode")   // "gps" | "map"
        val SELECTED_LAT_KEY = doublePreferencesKey("selected_lat")
        val SELECTED_LON_KEY = doublePreferencesKey("selected_lon")
        val UNITS_KEY =
            stringPreferencesKey("units")            // "metric" | "imperial" | "standard"
        val LANGUAGE_KEY = stringPreferencesKey("language")         // "en" | "ar"
    }

    override val locationMode: Flow<String> = context.dataStore.data
        .map { it[LOCATION_MODE_KEY] ?: "gps" }

    override suspend fun saveLocationMode(mode: String) {
        context.dataStore.edit { it[LOCATION_MODE_KEY] = mode }
    }


    override val selectedLat : Flow<Double> = context.dataStore.data
        .map { it[SELECTED_LAT_KEY] ?: 0.0 }

    override val selectedLon : Flow<Double> = context.dataStore.data
        .map { it[SELECTED_LON_KEY] ?: 0.0 }

    override suspend fun saveSelectedLocation(lat: Double, lon: Double) {
        context.dataStore.edit {
            it[SELECTED_LAT_KEY] = lat
            it[SELECTED_LON_KEY] = lon
        }
    }


    override val units : Flow<String> = context.dataStore.data
        .map { it[UNITS_KEY] ?: "metric" }

    override suspend fun saveUnits(units: String) {
        context.dataStore.edit { it[UNITS_KEY] = units }
    }


    override val language: Flow<String> = context.dataStore.data
        .map { it[LANGUAGE_KEY] ?: "en" }

    override suspend fun saveLanguage(language: String) {
        context.dataStore.edit { it[LANGUAGE_KEY] = language }
    }
}