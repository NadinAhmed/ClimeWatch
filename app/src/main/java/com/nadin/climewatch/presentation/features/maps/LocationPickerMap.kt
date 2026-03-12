package com.nadin.climewatch.presentation.features.maps

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.nadin.climewatch.R
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.presentation.ui.theme.AppGradient

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LocationPickerMap(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: MapViewModel = viewModel(
        factory = MapViewModelFactory(
            repository = WeatherRepository(context)
        )
    )

    val selectedLocation by viewModel.selectedLocation.collectAsState()
    val locationName by viewModel.locationName.collectAsState()
    val isSaveEnabled by viewModel.isSaveEnabled.collectAsState()

//    val singapore = LatLng(51.52061810406676, -0.12635325270312533)
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(singapore, 10f)
//    }

    LaunchedEffect(Unit) {
        viewModel.mapEvent.collect { event ->
            when (event) {
                is MapEvent.GoBackToFav -> navController.popBackStack()
                is MapEvent.ShowError -> { /* show snackbar */
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val cairo = LatLng(30.0444, 31.2357)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                cairo,
                10f
            )
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng -> viewModel.onLocationSelected(latLng) },
        ) {
            selectedLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = locationName,
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.map_marker)
                )
            }
        }

        AnimatedVisibility(
            visible = isSaveEnabled,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp),
            enter = slideInVertically { it },
            exit = slideOutVertically { it }
        ) {
            Button(
                onClick = { viewModel.saveLocation() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(brush = AppGradient.buttonLinearGradient)
            ) {
                Text(
                    stringResource(R.string.save),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
