package com.nadin.climewatch.presentation.features.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nadin.climewatch.R
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.data.features.weather.model.Forecast
import com.nadin.climewatch.data.features.weather.model.Weather
import com.nadin.climewatch.presentation.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherState = MutableStateFlow<ResultState<Weather>>(ResultState.Loading)
    val weatherState = _weatherState.asStateFlow()

    private val _forecastState = MutableStateFlow<ResultState<Forecast>>(ResultState.Loading)
    val forecastState = _forecastState.asStateFlow()

    init {
        getCurrentWeather()
        getForecast()
    }

    fun getCurrentWeather() {
        viewModelScope.launch {
            _weatherState.value = ResultState.Loading

            repository.getCurrentWeather(40.7128, -74.0060)
                .onSuccess { weather ->
                    _weatherState.value = ResultState.Success(weather)
                }
                .onFailure {
                    _weatherState.value = ResultState.Error(
                        it.message ?: "Unknown error"
                    )
                }
        }
    }

    fun getForecast() {
        viewModelScope.launch {
            repository.getForecast(40.7128, -74.0060)
                .collect { result ->
                    _forecastState.value = result
                }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
class FavViewModelFactory(private val weatherRepository: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(weatherRepository) as T
    }
}