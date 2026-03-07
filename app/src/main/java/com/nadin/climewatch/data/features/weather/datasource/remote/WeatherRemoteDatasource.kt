package com.nadin.climewatch.data.features.weather.datasource.remote

import com.nadin.climewatch.data.features.weather.dto.CurrentWeatherDto
import com.nadin.climewatch.data.network.RetrofitInstance

class WeatherRemoteDatasource(
) {
    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double
    ): CurrentWeatherDto {
        return RetrofitInstance.weatherService.getCurrentWeather(lat, lon)
    }
}