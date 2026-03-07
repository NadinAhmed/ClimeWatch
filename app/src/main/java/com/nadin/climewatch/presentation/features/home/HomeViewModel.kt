package com.nadin.climewatch.presentation.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nadin.climewatch.data.features.weather.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: WeatherRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    fun getCurrentWeather(lat: Double, lon: Double) {

        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            val result = repository.getCurrentWeather(lat, lon)
            result.onSuccess { weather ->

                _uiState.value = HomeUiState.Success(weather)

            }.onFailure {

                _uiState.value = HomeUiState.Error(
                    it.message ?: "Unknown error"
                )
            }
        }
    }
}

class FavViewModelFactory(private val weatherRepository: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(weatherRepository) as T
    }
}