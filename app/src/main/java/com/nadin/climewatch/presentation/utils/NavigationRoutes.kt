package com.nadin.climewatch.presentation.utils

sealed class NavigationRoutes(val route: String) {
    object Splash : NavigationRoutes("splash")
    object Home : NavigationRoutes("home/{lat}/{lon}") {
        fun createRoute(lat: Double, lon: Double) = "home/$lat/$lon"
    }
    object Favourite : NavigationRoutes("favourite")
    object Alerts : NavigationRoutes("alerts")
    object Settings : NavigationRoutes("settings")
    object MapPicker : NavigationRoutes("map_picker")
}