package com.nadin.climewatch.presentation.features.home

import LocationProvider
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.presentation.utils.Location
import com.nadin.climewatch.presentation.utils.Location.enableLocationServices
import com.nadin.climewatch.presentation.utils.Location.isLocEnabled
import kotlinx.coroutines.launch

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

            is HomeUiState.Loading -> {
                CircularProgressIndicator()
            }

            is HomeUiState.Success -> {
                Text("Temperature: ${uiState.weather.temperature}")
            }

            is HomeUiState.Error -> {
                Text(uiState.message)
            }
        }
    }
}