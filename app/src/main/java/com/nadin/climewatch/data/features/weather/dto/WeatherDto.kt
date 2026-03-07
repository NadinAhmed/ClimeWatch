package com.nadin.climewatch.data.features.weather.dto


data class WeatherDto(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)