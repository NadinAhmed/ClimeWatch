package com.nadin.climewatch.presentation.utils

sealed class NavigationRoutes(val route: String) {
    object Splash : NavigationRoutes("splash")
    object Home : NavigationRoutes("home")
    object Favourite : NavigationRoutes("favourite")
    object Alerts : NavigationRoutes("alerts")
    object Settings : NavigationRoutes("settings")
    object MapPicker : NavigationRoutes("map_picker")
}