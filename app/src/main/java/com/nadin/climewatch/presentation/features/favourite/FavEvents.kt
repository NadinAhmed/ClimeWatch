package com.nadin.climewatch.presentation.features.favourite

sealed class FavEvent {
    object GoToMap : FavEvent()
    data class GoToHome(val lat: Double, val lon: Double) : FavEvent()
}