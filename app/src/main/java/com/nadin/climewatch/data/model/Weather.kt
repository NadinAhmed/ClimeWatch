package com.nadin.climewatch.data.model

data class Weather(
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int,
    val clouds: Int,
    val description: String,
    val icon: String,
    val city: String,
    val country: String,
)