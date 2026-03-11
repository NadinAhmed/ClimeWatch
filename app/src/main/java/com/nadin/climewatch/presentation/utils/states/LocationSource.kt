package com.nadin.climewatch.presentation.utils.states

sealed class LocationSource {
    data object CurrentLocation : LocationSource()
    data class SpecificLocationWithCity(val cityName: String) : LocationSource()
    data class SpecificLocationWithGeoCode(val lat: Double, val lon: Double) : LocationSource()
}