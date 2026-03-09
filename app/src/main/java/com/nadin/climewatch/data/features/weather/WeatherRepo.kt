package com.nadin.climewatch.data.features.weather

import android.os.Build
import androidx.annotation.RequiresApi
import com.nadin.climewatch.data.features.weather.datasource.remote.WeatherRemoteDatasource
import com.nadin.climewatch.data.features.weather.dto.toForecast
import com.nadin.climewatch.data.features.weather.dto.toModel
import com.nadin.climewatch.data.features.weather.model.Forecast
import com.nadin.climewatch.data.features.weather.model.Weather
import com.nadin.climewatch.presentation.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepository() {
    private val remoteDataSource: WeatherRemoteDatasource = WeatherRemoteDatasource()
    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double
    ): Result<Weather> {
        return try {
            val dto = remoteDataSource.getCurrentWeather(lat, lon)
            val model = dto.toModel()
            Result.success(model)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getForecast(
        lat: Double,
        lon: Double
    ): Flow<ResultState<Forecast>> = flow {

        emit(ResultState.Loading)
        try {
            val response = remoteDataSource.getForecast(lat, lon)
            emit(ResultState.Success(response.toForecast()))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }
}