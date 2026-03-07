package com.nadin.climewatch.data.features.weather.datasource.remote

import com.nadin.climewatch.data.features.weather.dto.CurrentWeatherDto
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService{
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): CurrentWeatherDto
}