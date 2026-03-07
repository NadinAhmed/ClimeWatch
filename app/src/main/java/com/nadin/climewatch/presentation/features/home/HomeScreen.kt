package com.nadin.climewatch.presentation.features.home

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.presentation.utils.Location
import com.nadin.climewatch.presentation.utils.Location.enableLocationServices
import com.nadin.climewatch.presentation.utils.Location.isLocEnabled

@Composable
fun HomeScreen() {

    val context = LocalContext.current
    val repository = WeatherRepository()
    val viewModel : HomeViewModel = viewModel(
        factory = FavViewModelFactory(repository)
    )
    val state = viewModel.uiState.collectAsState()

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted =
                permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true

            if (granted) {
                if (isLocEnabled(context)) {
                    // fetch weather
                } else {
                    enableLocationServices(context)
                }
            }
        }

    LaunchedEffect(Unit) {
        if (Location.checkLocationPermission(context)) {
            if (isLocEnabled(context)) {
                // Fetch weather data based on location from view model and update UI
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