package com.nadin.climewatch.presentation.features.home

import com.nadin.climewatch.data.features.weather.model.Weather

sealed class HomeUiState {

    object Loading : HomeUiState()

    data class Success(
        val weather: Weather
    ) : HomeUiState()

    data class Error(
        val message: String
    ) : HomeUiState()
}