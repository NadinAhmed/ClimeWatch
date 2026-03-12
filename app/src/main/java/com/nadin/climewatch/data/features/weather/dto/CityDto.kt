package com.nadin.climewatch.data.features.weather.dto

import com.nadin.climewatch.data.features.weather.model.City

data class CityDto(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
)

fun CityDto.toModel(): City{
    return City(
        name = name,
        lat = lat,
        lon = lon,
        country = country
    )
}