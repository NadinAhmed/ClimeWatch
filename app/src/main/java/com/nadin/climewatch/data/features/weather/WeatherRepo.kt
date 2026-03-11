package com.nadin.climewatch.data.features.weather

import android.os.Build
import androidx.annotation.RequiresApi
import com.nadin.climewatch.data.features.weather.datasource.remote.WeatherRemoteDatasource
import com.nadin.climewatch.data.features.weather.dto.toForecast
import com.nadin.climewatch.data.features.weather.dto.toModel
import com.nadin.climewatch.data.features.weather.model.Forecast
import com.nadin.climewatch.data.features.weather.model.Weather
import com.nadin.climewatch.presentation.utils.states.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@RequiresApi(Build.VERSION_CODES.O)
class WeatherRepository() {
    private val remoteDataSource: WeatherRemoteDatasource = WeatherRemoteDatasource()
    suspend fun getWeatherByGeoCode(
        lat: Double,
        lon: Double
    ): Result<Weather> {
        return try {
            val dto = remoteDataSource.getWeatherByGeoCode(lat, lon)
            val model = dto.toModel()
            Result.success(model)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWeatherByCity(
        city: String
    ): Result<Weather> {
        return try {
            val dto = remoteDataSource.getWeatherByCity(city)
            val model = dto.toModel()
            Result.success(model)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getForecastByGeoCode(
        lat: Double,
        lon: Double
    ): Flow<ResultState<Forecast>> = flow {

        emit(ResultState.Loading)
        try {
            val response = remoteDataSource.getForecastByGeoCode(lat, lon)
            emit(ResultState.Success(response.toForecast()))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }

    fun getForecastByCity(
        city: String
    ): Flow<ResultState<Forecast>> = flow {

        emit(ResultState.Loading)
        try {
            val response = remoteDataSource.getForecastByCity(city)
            emit(ResultState.Success(response.toForecast()))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }
}