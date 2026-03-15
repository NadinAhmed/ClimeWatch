package com.nadin.climewatch.data.local

import kotlinx.coroutines.flow.Flow

interface ISettingsDataStore {
    val locationMode: Flow<String>
    suspend fun saveLocationMode(mode: String)


    val selectedLat : Flow<Double>
    val selectedLon : Flow<Double>
    suspend fun saveSelectedLocation(lat: Double, lon: Double)


    val units : Flow<String>
    suspend fun saveUnits(units: String)


    val language: Flow<String>
    suspend fun saveLanguage(language: String)
}