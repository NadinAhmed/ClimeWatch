package com.nadin.climewatch.data.features.weather.datasource.remote

import com.nadin.climewatch.data.features.weather.dto.CurrentWeatherDto
import com.nadin.climewatch.data.features.weather.dto.ForecastResponseDto
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService{
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric"
    ): CurrentWeatherDto

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric"
    ): ForecastResponseDto
}