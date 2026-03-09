package com.nadin.climewatch.data.features.weather.model

data class Forecast(
    val hourly: List<HourlyForecast>,
    val weekly: List<DailyForecast>
)

data class HourlyForecast(
    val time: String,
    val temperature: Double,
    val icon: String
)

data class DailyForecast(
    val day: String,
    val temperature: Double,
    val description: String,
    val icon: String
)