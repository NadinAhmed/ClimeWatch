package com.nadin.climewatch.presentation.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.data.features.weather.model.Weather
import com.nadin.climewatch.presentation.utils.UiState

@Composable
fun HomeScreen() {

    val repository = WeatherRepository()
    val viewModel: HomeViewModel = viewModel(
        factory = FavViewModelFactory(repository)
    )
    val state = viewModel.uiState.collectAsState()

/*
    val locationClient = remember {
        LocationProvider(
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(
                context
            )
        )
    }

    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted =
                permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true

            if (granted) {
                if (isLocEnabled(context)) {
                    lifecycleScope.launch {
                        val location = locationClient.getCurrentLocation()
                        viewModel.getCurrentWeather()
                    }
                } else {
                    enableLocationServices(context)
                }
            }
        }

    LaunchedEffect(Unit) {
        if (Location.checkLocationPermission(context)) {
            if (isLocEnabled(context)) {
                val location = locationClient.getCurrentLocation()
                viewModel.getCurrentWeather()
            } else {
                enableLocationServices(context)
            }
        } else {
            permissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
*/

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val uiState = state.value) {

            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Success<Weather> -> {
                Text("Temperature: ${uiState.data.temperature}")
            }

            is UiState.Error -> {
                Text(uiState.message)
            }
        }
    }
}