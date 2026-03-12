package com.nadin.climewatch.data.features.weather

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import com.nadin.climewatch.data.features.weather.datasource.local.FavLocationLocalDataSource
import com.nadin.climewatch.data.features.weather.datasource.remote.WeatherRemoteDatasource
import com.nadin.climewatch.data.features.weather.dto.toForecast
import com.nadin.climewatch.data.features.weather.dto.toModel
import com.nadin.climewatch.data.features.weather.model.City
import com.nadin.climewatch.data.features.weather.model.FavoriteLocation
import com.nadin.climewatch.data.features.weather.model.Forecast
import com.nadin.climewatch.data.features.weather.model.Weather
import com.nadin.climewatch.presentation.utils.states.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

@RequiresApi(Build.VERSION_CODES.O)
class WeatherRepository(context: Context) {
    private val remoteDataSource: WeatherRemoteDatasource = WeatherRemoteDatasource()
    private val localDataSource: FavLocationLocalDataSource = FavLocationLocalDataSource(context)
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

    suspend fun getCityByGeoCode(
        geoCode: LatLng
    ): Result<City> {
        return try {
            val dto = remoteDataSource.getCityByGeoCode(geoCode)
            val model = dto.toModel()
            Result.success(model)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insertLocationToFav(location: FavoriteLocation) {
        localDataSource.insertLocation(location)
    }

    suspend fun deleteLocationFromFav(location: FavoriteLocation) {
        localDataSource.deleteLocation(location)
    }

    fun getAllFavLocations(): Flow<ResultState<List<FavoriteLocation>>> =
        localDataSource.getAllLocations()
            .map { locations -> ResultState.Success(locations) as ResultState<List<FavoriteLocation>> }
            .onStart { emit(ResultState.Loading) }
            .catch { e -> emit(ResultState.Error(e.message ?: "Unknown error")) }
}
