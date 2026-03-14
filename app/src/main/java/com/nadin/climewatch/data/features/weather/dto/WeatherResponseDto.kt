package com.nadin.climewatch.data.features.weather.dto

import com.google.gson.annotations.SerializedName
import com.nadin.climewatch.data.model.Weather

data class WeatherResponseDto(
    val weather: List<WeatherDto>,
    @SerializedName("main")
    val mainInfo: MainDto,
    val wind: WindDto,
    val clouds: CloudsDto,
    @SerializedName("dt")
    val dateTime: Long,
    val sys: SysDto,
    val timezone: Int,
    val id: Long,
    @SerializedName("name")
    val city: String,
)

fun WeatherResponseDto.toModel(): Weather {
    return Weather(
        temperature = mainInfo.temp,
        humidity = mainInfo.humidity,
        windSpeed = wind.speed,
        pressure = mainInfo.pressure,
        clouds = clouds.all,
        description = weather.firstOrNull()?.description ?: "",
        icon = weather.firstOrNull()?.icon ?: "",
        city = city,
        country = sys.country
    )
}
