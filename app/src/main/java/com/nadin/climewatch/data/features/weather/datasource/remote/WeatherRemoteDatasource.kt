package com.nadin.climewatch.data.features.weather.datasource.remote

import com.google.android.gms.maps.model.LatLng
import com.nadin.climewatch.data.exptions.NetworkException
import com.nadin.climewatch.data.exptions.ServerException
import com.nadin.climewatch.data.features.weather.dto.WeatherResponseDto
import com.nadin.climewatch.data.features.weather.dto.ForecastResponseDto
import com.nadin.climewatch.data.features.weather.dto.CityDto
import com.nadin.climewatch.data.local.SettingsDataStore
import com.nadin.climewatch.data.network.RetrofitInstance
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

class WeatherRemoteDatasource(
    private val settingsDataStore: SettingsDataStore
) {

    suspend fun getWeatherByGeoCode(
        lat: Double,
        lon: Double
    ): WeatherResponseDto {
        return try {
            val units = settingsDataStore.units.first()
            val lang = settingsDataStore.language.first()
            RetrofitInstance.weatherService.getWeather(lat, lon, units, lang)
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
            val units = settingsDataStore.units.first()
            val lang = settingsDataStore.language.first()
            RetrofitInstance.weatherService.getWeather(city, units, lang)
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
            val units = settingsDataStore.units.first()
            val lang = settingsDataStore.language.first()
            RetrofitInstance.weatherService.getForecast(lat, lon, units, lang)
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
            val units = settingsDataStore.units.first()
            val lang = settingsDataStore.language.first()
            RetrofitInstance.weatherService.getForecast(city, units, lang)
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
            val units = settingsDataStore.units.first()
            val lang = settingsDataStore.language.first()
            val response =
                RetrofitInstance.weatherService.getCityByGeoCode(
                    geoCode.latitude,
                    geoCode.longitude,
                    units = units, lang = lang
                )
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
            val units = settingsDataStore.units.first()
            val lang = settingsDataStore.language.first()
            RetrofitInstance.weatherService.getSuggestionCities(query, units = units, lang = lang)
        } catch (e: HttpException) {
            throw ServerException()
        } catch (e: IOException) {
            throw NetworkException()
        }
    }
}