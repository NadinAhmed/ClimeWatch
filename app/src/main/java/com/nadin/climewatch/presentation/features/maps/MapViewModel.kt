package com.nadin.climewatch.presentation.features.maps

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.data.model.City
import com.nadin.climewatch.data.features.weather.entites.FavoriteLocation
import com.nadin.climewatch.data.local.SettingsDataStore
import com.nadin.climewatch.presentation.utils.states.ResultState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class MapViewModel(
    private val repository: WeatherRepository,
    private val settingsDataStore: SettingsDataStore,
    private val mapSource: MapSource
) : ViewModel() {
    private val _selectedLocation = MutableStateFlow<LatLng?>(null)
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _locationName = MutableStateFlow<String>("")
    val locationName = _locationName.asStateFlow()

    private val _cityState = MutableStateFlow<ResultState<City>>(ResultState.Loading)
    val cityState = _cityState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _suggestionsState =
        MutableStateFlow<ResultState<List<City>>>(ResultState.Success(emptyList()))
    val suggestionsState = _suggestionsState.asStateFlow()

    private val _isSaveEnabled = MutableStateFlow(false)
    val isSaveEnabled = _isSaveEnabled.asStateFlow()

    private val _mapEvent = MutableSharedFlow<MapEvent>()
    val mapEvent = _mapEvent.asSharedFlow()

    private var isSelectingSuggestion = false

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (isSelectingSuggestion) {
                        isSelectingSuggestion = false
                        return@collectLatest
                    }
                    if (query.length >= 2) fetchSuggestions(query)
                    else _suggestionsState.value = ResultState.Success(emptyList())
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun fetchSuggestions(query: String) {
        viewModelScope.launch {
            repository.getSuggestionCities(query).collect {
                _suggestionsState.value = it
            }
        }
    }

    fun onSuggestionSelected(city: City) {
        isSelectingSuggestion = true
        _selectedLocation.value = LatLng(city.lat, city.lon)
        _isSaveEnabled.value = true
        _cityState.value = ResultState.Success(city)
        _searchQuery.value = "${city.name}, ${city.country}"
        _suggestionsState.value = ResultState.Success(emptyList())
    }

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
                when (mapSource) {
                    is MapSource.FromFavorites -> {
                        repository.insertLocationToFav(
                            FavoriteLocation(
                                city = city.name,
                                country = city.country,
                                lat = location.latitude,
                                lon = location.longitude
                            )
                        )
                    }

                    is MapSource.FromSettings -> {
                        settingsDataStore.saveLocationMode("map")
                        settingsDataStore.saveSelectedLocation(
                            location.latitude,
                            location.longitude
                        )
                    }
                }
                _mapEvent.emit(MapEvent.GoBackToFav)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
class MapViewModelFactory(
    private val repository: WeatherRepository,
    private val settingsDataStore: SettingsDataStore,
    private val mapSource: MapSource,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(repository, settingsDataStore, mapSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}