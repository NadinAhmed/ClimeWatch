package com.nadin.climewatch.data.features.weather.datasource.remote

import com.nadin.climewatch.data.features.weather.dto.WeatherResponseDto
import com.nadin.climewatch.data.features.weather.dto.ForecastResponseDto
import com.nadin.climewatch.data.features.weather.dto.CityDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService{
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
    ): WeatherResponseDto

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("units") units: String = "metric",
    ): WeatherResponseDto

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
    ): ForecastResponseDto

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("units") units: String = "metric",
    ): ForecastResponseDto

    @GET("geo/1.0/reverse")
    suspend fun getCityByGeoCode(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("limit") limit: Int = 1
    ): List<CityDto>
}