package com.nadin.climewatch.presentation.features.maps

sealed class MapEvent {
    object GoBackToFav : MapEvent()
    data class ShowError(val message: String) : MapEvent()
}