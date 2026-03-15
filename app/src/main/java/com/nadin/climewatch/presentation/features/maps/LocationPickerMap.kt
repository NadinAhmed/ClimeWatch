package com.nadin.climewatch.presentation.features.maps

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.nadin.climewatch.R
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.data.local.SettingsDataStore
import com.nadin.climewatch.data.model.City
import com.nadin.climewatch.presentation.ui.theme.AppGradient
import com.nadin.climewatch.presentation.ui.theme.ButtonLightColor
import com.nadin.climewatch.presentation.ui.theme.PrimaryColor
import com.nadin.climewatch.presentation.ui.theme.PrimaryDarkColor
import com.nadin.climewatch.presentation.ui.theme.SecondaryTextColor
import com.nadin.climewatch.presentation.utils.components.Spacers
import com.nadin.climewatch.presentation.utils.states.ResultState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LocationPickerMap(
    navController: NavController,
    mapSource: MapSource
) {
    val context = LocalContext.current
    val settingsDataStore = remember { SettingsDataStore(context) }
    val viewModel: MapViewModel = viewModel(
        factory = MapViewModelFactory(
            repository = WeatherRepository.getInstance(context),
            settingsDataStore = settingsDataStore,
            mapSource = mapSource
        )
    )

    val selectedLocation by viewModel.selectedLocation.collectAsState()
    val locationName by viewModel.locationName.collectAsState()
    val isSaveEnabled by viewModel.isSaveEnabled.collectAsState()
    val cityState by viewModel.cityState.collectAsState()
    val suggestionsState by viewModel.suggestionsState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val cairo = LatLng(30.0444, 31.2357)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            cairo,
            10f
        )
    }

    LaunchedEffect(selectedLocation) {
        selectedLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, 10f)
            )
        }
    }

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

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng -> viewModel.onLocationSelected(latLng) },
        ) {
            selectedLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = if (cityState is ResultState.Success)
                        (cityState as ResultState.Success<City>).data.name else "",
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.map_marker)
                )
            }
        }

        // Search Bar + Suggestions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.search_for_city), color = PrimaryColor) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "search")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                textStyle = LocalTextStyle.current.copy(color = PrimaryColor),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SecondaryTextColor,
                    unfocusedContainerColor = SecondaryTextColor,
                    focusedTextColor = PrimaryColor,
                    unfocusedTextColor = PrimaryColor,
                    focusedBorderColor = ButtonLightColor,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                )
            )

            Spacers.VerticalSpacer(4.dp)

            // Suggestions Dropdown
            when (val state = suggestionsState) {
                is ResultState.Loading -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = SecondaryTextColor)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = ButtonLightColor
                            )
                        }
                    }
                }

                is ResultState.Success -> {
                    if (state.data.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = SecondaryTextColor)
                        ) {
                            state.data.forEach { city ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.onSuggestionSelected(city) }
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = ButtonLightColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacers.HorizontalSpacer(8.dp)
                                    Column {
                                        Text(
                                            text = city.name,
                                            color = PrimaryDarkColor,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = city.country,
                                            color = PrimaryDarkColor.copy(alpha = 0.7f),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                                if (city != state.data.last()) {
                                    HorizontalDivider(
                                        color = Color.White.copy(alpha = 0.3f),
                                        thickness = 1.dp
                                    )
                                }
                            }
                        }
                    }
                }

                else -> {}
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
