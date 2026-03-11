package com.nadin.climewatch.data.features.weather.datasource.remote

import com.nadin.climewatch.data.exptions.NetworkException
import com.nadin.climewatch.data.exptions.ServerException
import com.nadin.climewatch.data.features.weather.dto.WeatherResponseDto
import com.nadin.climewatch.data.features.weather.dto.ForecastResponseDto
import com.nadin.climewatch.data.network.RetrofitInstance
import retrofit2.HttpException
import java.io.IOException

class WeatherRemoteDatasource(
) {
    suspend fun getWeatherByGeoCode(
        lat: Double,
        lon: Double
    ): WeatherResponseDto {
        return try {
            RetrofitInstance.weatherService.getWeather(lat, lon)
        } catch (e: HttpException) {
            throw ServerException()
        } catch (e: IOException) {
            throw NetworkException()
        }
    }

    suspend fun getWeatherByCity(
        city: String
    ): WeatherResponseDto {
        return try {
            RetrofitInstance.weatherService.getWeather(city)
        } catch (e: HttpException) {
            throw ServerException()
        } catch (e: IOException) {
            throw NetworkException()
        }
    }

    suspend fun getForecastByGeoCode(
        lat: Double,
        lon: Double
    ): ForecastResponseDto {
        return try {
            RetrofitInstance.weatherService.getForecast(
                lat, lon
            )
        } catch (e: HttpException) {
            throw ServerException()
        } catch (e: IOException) {
            throw NetworkException()
        }
    }

    suspend fun getForecastByCity(
        city: String
    ): ForecastResponseDto {
        return try {
            RetrofitInstance.weatherService.getForecast(city)
        } catch (e: HttpException) {
            throw ServerException()
        } catch (e: IOException) {
            throw NetworkException()
        }
    }
}