package com.nadin.climewatch.data.features.weather.datasource.remote

import com.google.android.gms.maps.model.LatLng
import com.nadin.climewatch.data.exptions.NetworkException
import com.nadin.climewatch.data.exptions.ServerException
import com.nadin.climewatch.data.features.weather.dto.WeatherResponseDto
import com.nadin.climewatch.data.features.weather.dto.ForecastResponseDto
import com.nadin.climewatch.data.features.weather.dto.CityDto
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

    suspend fun getCityByGeoCode(
        geoCode: LatLng
    ): CityDto {
        return try {
            val response =
                RetrofitInstance.weatherService.getCityByGeoCode(geoCode.latitude, geoCode.longitude)
            response.firstOrNull() ?: CityDto(
                name = "${"%.4f".format(geoCode.latitude)}, ${"%.4f".format(geoCode.longitude)}",
                lat = geoCode.latitude,
                lon = geoCode.longitude,
                country = ""
            )
        } catch (e: HttpException) {
            throw ServerException()
        } catch (e: IOException) {
            throw NetworkException()
        }
    }

    suspend fun getSuggestionCities(
        query: String
    ): List<CityDto> {
        return try {
            RetrofitInstance.weatherService.getSuggestionCities(query)
        } catch (e: HttpException) {
            throw ServerException()
        } catch (e: IOException) {
            throw NetworkException()
        }
    }
}