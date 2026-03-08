package com.nadin.climewatch.data.features.weather.datasource.remote

import com.nadin.climewatch.data.exptions.NetworkException
import com.nadin.climewatch.data.exptions.ServerException
import com.nadin.climewatch.data.features.weather.dto.CurrentWeatherDto
import com.nadin.climewatch.data.network.RetrofitInstance
import retrofit2.HttpException
import java.io.IOException

class WeatherRemoteDatasource(
) {
    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double
    ): CurrentWeatherDto {
        return try {
            RetrofitInstance.weatherService.getCurrentWeather(lat, lon)
        }catch (e: HttpException) {
            throw ServerException()
        }catch (e: IOException) {
            throw NetworkException()
        }
    }
}