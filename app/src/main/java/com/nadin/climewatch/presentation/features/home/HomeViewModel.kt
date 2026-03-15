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
import com.nadin.climewatch.data.model.Forecast
import com.nadin.climewatch.data.model.Weather
import com.nadin.climewatch.data.local.CityPreferences
import com.nadin.climewatch.data.local.SettingsDataStore
import com.nadin.climewatch.presentation.utils.Location
import com.nadin.climewatch.presentation.utils.states.LocationSource
import com.nadin.climewatch.presentation.utils.states.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val app: Application,
    private val repository: WeatherRepository,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {
    private val _locationModeState = MutableStateFlow("gps")
    val locationModeState = _locationModeState.asStateFlow()

    private var latestMode: String = "gps"
    private var latestLat: Double = 0.0
    private var latestLon: Double = 0.0
    private val _unitsState = MutableStateFlow("metric")
    val unitsState = _unitsState.asStateFlow()

    private val _weatherState = MutableStateFlow<ResultState<Weather>>(ResultState.Loading)
    val weatherState = _weatherState.asStateFlow()

    private val _forecastState = MutableStateFlow<ResultState<Forecast>>(ResultState.Loading)
    val forecastState = _forecastState.asStateFlow()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(app)

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            combine(
                settingsDataStore.locationMode,
                settingsDataStore.selectedLat,
                settingsDataStore.selectedLon,
                settingsDataStore.units
            ) { mode, lat, lon, units ->
                HomeSettingsSnapshot(mode, lat, lon, units)
            }.collect { snapshot ->
                latestMode = snapshot.mode
                latestLat = snapshot.lat
                latestLon = snapshot.lon
                _locationModeState.value = snapshot.mode
                _unitsState.value = snapshot.units
                requestWeatherForMode(snapshot.mode, snapshot.lat, snapshot.lon)
            }
        }
    }

    fun refreshWeatherBasedOnSettings() {
        requestWeatherForMode(latestMode, latestLat, latestLon)
    }

    private fun requestWeatherForMode(mode: String, lat: Double, lon: Double) {
        when (mode) {
            "gps" -> loadWeatherForCurrentLocation()
            "map" -> {
                if (lat == 0.0 && lon == 0.0) return
                loadWeatherForSpecificLocation(lat, lon)
            }
        }
    }


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
            .onSuccess {
                _weatherState.value = ResultState.Success(it)
                CityPreferences.saveCurrentCity(app, it.city)
            }
            .onFailure { _weatherState.value = ResultState.Error(it.message ?: "Unknown error") }

        repository.getForecastByGeoCode(lat, lon)
            .collect { _forecastState.value = it }
    }

    private suspend fun fetchWeatherAndForecastByCity(cityName: String) {
        repository.getWeatherByCity(cityName)
            .onSuccess {
                _weatherState.value = ResultState.Success(it)
                CityPreferences.saveCurrentCity(app, it.city)
            }
            .onFailure { _weatherState.value = ResultState.Error(it.message ?: "Unknown error") }

        repository.getForecastByCity(cityName)
            .collect { _forecastState.value = it }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    class HomeViewModelFactory(
        private val app: Application,
        private val weatherRepository: WeatherRepository,
        private val settingsDataStore: SettingsDataStore
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(app, weatherRepository, settingsDataStore) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

private data class HomeSettingsSnapshot(
    val mode: String,
    val lat: Double,
    val lon: Double,
    val units: String
)
