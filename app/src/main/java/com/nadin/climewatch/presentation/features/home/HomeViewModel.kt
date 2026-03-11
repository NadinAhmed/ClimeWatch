package com.nadin.climewatch.presentation.features.home

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.data.features.weather.model.Forecast
import com.nadin.climewatch.data.features.weather.model.Weather
import com.nadin.climewatch.presentation.utils.Location
import com.nadin.climewatch.presentation.utils.states.LocationSource
import com.nadin.climewatch.presentation.utils.states.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val repository: WeatherRepository,
    private val app: Application
) : ViewModel() {

    private val _weatherState = MutableStateFlow<ResultState<Weather>>(ResultState.Loading)
    val weatherState = _weatherState.asStateFlow()

    private val _forecastState = MutableStateFlow<ResultState<Forecast>>(ResultState.Loading)
    val forecastState = _forecastState.asStateFlow()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(app)

    fun loadWeatherForCurrentLocation() {
        viewModelScope.launch {
            fetchDataBySource(LocationSource.CurrentLocation)
        }
    }
    fun loadWeatherForSpecificLocation(cityName: String) {
        viewModelScope.launch {
            fetchDataBySource(LocationSource.SpecificLocationWithCity(cityName))
        }
    }

    fun loadWeatherForSpecificLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            fetchDataBySource(LocationSource.SpecificLocationWithGeoCode(lat, lon))
        }
    }
    @SuppressLint("MissingPermission")
    private suspend fun fetchDataBySource(source: LocationSource) {
        _weatherState.value = ResultState.Loading
        _forecastState.value = ResultState.Loading

        try {
            when (source) {
                is LocationSource.CurrentLocation -> {
                    val location = Location.getCurrentLocation(fusedLocationClient)
                    fetchWeatherAndForecast(location.latitude, location.longitude)
                }
                is LocationSource.SpecificLocationWithCity -> {
                    fetchWeatherAndForecastByCity(source.cityName)
                }

                is LocationSource.SpecificLocationWithGeoCode -> {
                    fetchWeatherAndForecast(source.lat, source.lon)
                }
            }
        } catch (e: Exception) {
            val error = ResultState.Error(e.message ?: "Unknown error")
            _weatherState.value = error
            _forecastState.value = error
        }
    }

    private suspend fun fetchWeatherAndForecast(lat: Double, lon: Double) {
        repository.getWeatherByGeoCode(lat, lon)
            .onSuccess { _weatherState.value = ResultState.Success(it) }
            .onFailure { _weatherState.value = ResultState.Error(it.message ?: "Unknown error") }

        repository.getForecastByGeoCode(lat, lon)
            .collect { _forecastState.value = it }
    }

    private suspend fun fetchWeatherAndForecastByCity(cityName: String) {
        repository.getWeatherByCity(cityName)
            .onSuccess { _weatherState.value = ResultState.Success(it) }
            .onFailure { _weatherState.value = ResultState.Error(it.message ?: "Unknown error") }

        repository.getForecastByCity(cityName)
            .collect { _forecastState.value = it }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(weatherRepository, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
