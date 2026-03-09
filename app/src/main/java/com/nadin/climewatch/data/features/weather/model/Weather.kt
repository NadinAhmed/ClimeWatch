package com.nadin.climewatch.data.features.weather.model

data class Weather(
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int,
    val clouds: Int,
    val description: String,
    val icon: Int,
    val city: String,
    val country: String,
)