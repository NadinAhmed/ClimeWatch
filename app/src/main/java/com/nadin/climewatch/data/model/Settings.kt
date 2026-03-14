package com.nadin.climewatch.data.model

enum class LocationMode { GPS, MAP }

enum class Units(val apiValue: String) {
    METRIC("metric"),
    IMPERIAL("imperial"),
    STANDARD("standard")
}

enum class AppLanguage(val code: String) {
    ENGLISH("en"),
    ARABIC("ar")
}

data class SettingsState(
    val locationMode: LocationMode = LocationMode.GPS,
    val selectedLat: Double = 0.0,
    val selectedLon: Double = 0.0,
    val units: Units = Units.METRIC,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val isLoading: Boolean = true
)