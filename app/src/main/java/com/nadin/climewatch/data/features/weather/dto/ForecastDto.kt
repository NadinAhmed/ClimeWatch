package com.nadin.climewatch.data.features.weather.dto

data class ForecastDto(
    val dt: Long,
    val dt_txt: String,
    val main: MainDto,
    val weather: List<WeatherDto>,
    val wind: WindDto
)