package com.nadin.climewatch.presentation.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nadin.climewatch.R
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.data.features.weather.model.Weather
import com.nadin.climewatch.presentation.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: WeatherRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Weather>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getCurrentWeather()
    }

    fun getCurrentWeather() {

        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = repository.getCurrentWeather(40.7128, -74.0060)
            result.onSuccess { weather ->

                _uiState.value = UiState.Success(weather)

            }.onFailure {

                _uiState.value = UiState.Error(
                    it.message ?: R.string.unknown_error_message.toString()
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