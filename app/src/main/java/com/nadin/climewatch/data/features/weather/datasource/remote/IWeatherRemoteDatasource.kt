package com.nadin.climewatch.data.features.weather.datasource.remote

import com.google.android.gms.maps.model.LatLng
import com.nadin.climewatch.data.features.weather.dto.CityDto
import com.nadin.climewatch.data.features.weather.dto.ForecastResponseDto
import com.nadin.climewatch.data.features.weather.dto.WeatherResponseDto

interface IWeatherRemoteDatasource {
    suspend fun getWeatherByGeoCode(
        lat: Double,
        lon: Double
    ): WeatherResponseDto

    suspend fun getWeatherByCity(
        city: String
    ): WeatherResponseDto

    suspend fun getForecastByGeoCode(
        lat: Double,
        lon: Double
    ): ForecastResponseDto

    suspend fun getForecastByCity(
        city: String
    ): ForecastResponseDto

    suspend fun getCityByGeoCode(
        geoCode: LatLng
    ): CityDto

    suspend fun getSuggestionCities(
        query: String
    ): List<CityDto>
}