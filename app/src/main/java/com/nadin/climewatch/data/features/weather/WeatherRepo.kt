package com.nadin.climewatch.data.features.weather

import com.nadin.climewatch.data.features.weather.datasource.remote.WeatherRemoteDatasource
import com.nadin.climewatch.data.features.weather.dto.toModel
import com.nadin.climewatch.data.features.weather.model.Weather
import java.io.IOException

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

        } catch (e: IOException) {
            Result.failure(Exception("No internet connection"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}