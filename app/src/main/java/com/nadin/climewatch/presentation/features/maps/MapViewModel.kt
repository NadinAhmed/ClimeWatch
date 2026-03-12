package com.nadin.climewatch.presentation.features.maps

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.data.features.weather.model.City
import com.nadin.climewatch.data.features.weather.model.FavoriteLocation
import com.nadin.climewatch.presentation.utils.states.ResultState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class MapViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _selectedLocation = MutableStateFlow<LatLng?>(null)
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _locationName = MutableStateFlow<String>("")
    val locationName = _locationName.asStateFlow()

    private val _cityState = MutableStateFlow<ResultState<City>>(ResultState.Loading)
    val cityState = _cityState.asStateFlow()

    private val _isSaveEnabled = MutableStateFlow(false)
    val isSaveEnabled = _isSaveEnabled.asStateFlow()

    private val _mapEvent = MutableSharedFlow<MapEvent>()
    val mapEvent = _mapEvent.asSharedFlow()

    fun onLocationSelected(geoCode: LatLng) {
        _selectedLocation.value = geoCode
        _isSaveEnabled.value = false
        fetchCityName(geoCode)
    }

    private fun fetchCityName(geoCode: LatLng) {
        viewModelScope.launch {
            _cityState.value = ResultState.Loading

            repository.getCityByGeoCode(
                geoCode
            ).onSuccess { city ->
                _cityState.value = ResultState.Success(city)
                _isSaveEnabled.value = true

            }.onFailure {
                _cityState.value = ResultState.Error(it.message ?: "Unknown error")
                _isSaveEnabled.value = true
            }
        }
    }

    fun saveLocation() {
        val location = _selectedLocation.value ?: return
        val cityResult = _cityState.value
        if (cityResult is ResultState.Success) {
            val city = cityResult.data
            viewModelScope.launch {
                repository.insertLocationToFav(
                    FavoriteLocation(
                        city = city.name,
                        country = city.country,
                        lat = location.latitude,
                        lon = location.longitude
                    )
                )
                _mapEvent.emit(MapEvent.GoBackToFav)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
class MapViewModelFactory(
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}