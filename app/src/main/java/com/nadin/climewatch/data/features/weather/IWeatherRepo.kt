package com.nadin.climewatch.data.features.weather

import com.google.android.gms.maps.model.LatLng
import com.nadin.climewatch.data.features.weather.dto.toForecast
import com.nadin.climewatch.data.features.weather.dto.toModel
import com.nadin.climewatch.data.features.weather.entites.Alert
import com.nadin.climewatch.data.features.weather.entites.FavoriteLocation
import com.nadin.climewatch.data.model.City
import com.nadin.climewatch.data.model.Forecast
import com.nadin.climewatch.data.model.Weather
import com.nadin.climewatch.presentation.utils.states.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface IWeatherRepo {
    suspend fun getWeatherByGeoCode(
        lat: Double, lon: Double
    ): Result<Weather>

    suspend fun getWeatherByCity(
        city: String
    ): Result<Weather>

    fun getForecastByGeoCode(
        lat: Double, lon: Double
    ): Flow<ResultState<Forecast>>

    fun getForecastByCity(
        city: String
    ): Flow<ResultState<Forecast>>

    suspend fun getCityByGeoCode(
        geoCode: LatLng
    ): Result<City>

    suspend fun insertLocationToFav(location: FavoriteLocation)

    suspend fun deleteLocationFromFav(location: FavoriteLocation)

    fun getAllFavLocations(): Flow<ResultState<List<FavoriteLocation>>>

    fun getSuggestionCities(query: String): Flow<ResultState<List<City>>>

    suspend fun insertAlert(alert: Alert): Int

    suspend fun deleteAlert(alert: Alert)

    fun getAllAlerts(): Flow<ResultState<List<Alert>>>
}