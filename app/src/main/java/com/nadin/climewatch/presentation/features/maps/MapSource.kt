package com.nadin.climewatch.presentation.features.maps

sealed class MapSource {
    object FromFavorites : MapSource()
    object FromSettings  : MapSource()
}