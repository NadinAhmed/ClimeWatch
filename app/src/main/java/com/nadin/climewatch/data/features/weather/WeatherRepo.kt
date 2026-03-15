package com.nadin.climewatch.data.features.weather

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import com.nadin.climewatch.data.features.weather.datasource.local.alert.AlertLocalDataSource
import com.nadin.climewatch.data.features.weather.datasource.local.alert.IAlertLocalDataSource
import com.nadin.climewatch.data.features.weather.datasource.local.favlocations.FavLocationLocalDataSource
import com.nadin.climewatch.data.features.weather.datasource.local.favlocations.IFavLocationLocalDataSource
import com.nadin.climewatch.data.features.weather.datasource.remote.IWeatherRemoteDatasource
import com.nadin.climewatch.data.features.weather.datasource.remote.WeatherRemoteDatasource
import com.nadin.climewatch.data.features.weather.dto.toForecast
import com.nadin.climewatch.data.features.weather.dto.toModel
import com.nadin.climewatch.data.features.weather.entites.Alert
import com.nadin.climewatch.data.model.City
import com.nadin.climewatch.data.features.weather.entites.FavoriteLocation
import com.nadin.climewatch.data.local.SettingsDataStore
import com.nadin.climewatch.data.model.Forecast
import com.nadin.climewatch.data.model.Weather
import com.nadin.climewatch.presentation.utils.states.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

@RequiresApi(Build.VERSION_CODES.O)
class WeatherRepository(
    private val remoteDataSource: IWeatherRemoteDatasource,
    private val favLocalDataSource: IFavLocationLocalDataSource,
    private val alertLocalDataSource: IAlertLocalDataSource
): IWeatherRepo {

    companion object {
        private var INSTANCE: WeatherRepository? = null
        fun getInstance(context: Context): WeatherRepository {
            if (INSTANCE == null) {
                val remoteDataSource = WeatherRemoteDatasource(SettingsDataStore(context))
                val favLocalDataSource = FavLocationLocalDataSource(
                    context
                )
                val alertLocalDataSource = AlertLocalDataSource(context)
                INSTANCE =
                    WeatherRepository(remoteDataSource, favLocalDataSource, alertLocalDataSource)
            }
            return INSTANCE!!
        }
    }

    override suspend fun getWeatherByGeoCode(
        lat: Double, lon: Double
    ): Result<Weather> {
        return try {
            val dto = remoteDataSource.getWeatherByGeoCode(lat, lon)
            val model = dto.toModel()
            Result.success(model)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWeatherByCity(
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

    override fun getForecastByGeoCode(
        lat: Double, lon: Double
    ): Flow<ResultState<Forecast>> = flow {

        emit(ResultState.Loading)
        try {
            val response = remoteDataSource.getForecastByGeoCode(lat, lon)
            emit(ResultState.Success(response.toForecast()))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }

    override fun getForecastByCity(
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

    override suspend fun getCityByGeoCode(
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

    override suspend fun insertLocationToFav(location: FavoriteLocation) {
        favLocalDataSource.insertLocation(location)
    }

    override suspend fun deleteLocationFromFav(location: FavoriteLocation) {
        favLocalDataSource.deleteLocation(location)
    }

    override fun getAllFavLocations(): Flow<ResultState<List<FavoriteLocation>>> =
        favLocalDataSource.getAllLocations()
            .map { locations -> ResultState.Success(locations) as ResultState<List<FavoriteLocation>> }
            .onStart { emit(ResultState.Loading) }
            .catch { e -> emit(ResultState.Error(e.message ?: "Unknown error")) }


    override fun getSuggestionCities(query: String): Flow<ResultState<List<City>>> = flow {
        if (query.length < 2) {
            emit(ResultState.Success(emptyList()))
            return@flow
        }

        emit(ResultState.Loading)
        try {
            val response = remoteDataSource.getSuggestionCities(query)
            emit(ResultState.Success(response.map { it.toModel() }))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }

    }

    override suspend fun insertAlert(alert: Alert): Int {
        return alertLocalDataSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        alertLocalDataSource.deleteAlert(alert)
    }

    override fun getAllAlerts(): Flow<ResultState<List<Alert>>> = alertLocalDataSource.getAllAlerts()
        .map { alerts -> ResultState.Success(alerts) as ResultState<List<Alert>> }
        .onStart { emit(ResultState.Loading) }
        .catch { e -> emit(ResultState.Error(e.message ?: "Unknown error")) }
}
