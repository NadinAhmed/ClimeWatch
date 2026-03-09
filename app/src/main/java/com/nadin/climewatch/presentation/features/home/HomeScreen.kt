package com.nadin.climewatch.presentation.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nadin.climewatch.R
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.data.features.weather.model.Weather
import com.nadin.climewatch.presentation.features.home.components.ProprietariesItem
import com.nadin.climewatch.presentation.features.home.components.TodayForecastItem
import com.nadin.climewatch.presentation.features.home.components.WeeklyForecastItem
import com.nadin.climewatch.presentation.ui.theme.AppGradient
import com.nadin.climewatch.presentation.ui.theme.IconBackgroundColor
import com.nadin.climewatch.presentation.ui.theme.LabelLightColor
import com.nadin.climewatch.presentation.ui.theme.PrimaryColor
import com.nadin.climewatch.presentation.utils.components.ErrorScreen
import com.nadin.climewatch.presentation.utils.components.LoadingScreen
import com.nadin.climewatch.presentation.utils.components.Spacers
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
    HomeContent(state.value)
}

@Composable
fun HomeContent(state: UiState<Weather>, modifier: Modifier = Modifier) {
    when (state) {
        is UiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is UiState.Error -> ErrorScreen(state.message, modifier = modifier.fillMaxSize())
        is UiState.Success<Weather> -> HomeSuccessContent(weather = state.data, modifier = modifier)
    }
}

@Composable
private fun HomeSuccessContent(weather: Weather, modifier: Modifier = Modifier) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(35.dp)
                        .background(
                            Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = LabelLightColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color.White,
                    )
                }
            }
            Spacers.VerticalSpacer(25.dp)
            Text(
                "${weather.city}, ${weather.country}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacers.VerticalSpacer(18.dp)
            Image(
                painter = painterResource(id = R.drawable.clear_n),
                contentDescription = "Weather Image",
                modifier = Modifier.size(120.dp)
            )
            Spacers.VerticalSpacer(28.dp)
            Text(
                "${weather.temperature}°C",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacers.VerticalSpacer(4.dp)
            Text(
                weather.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacers.VerticalSpacer(18.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .background(
                        Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = LabelLightColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(vertical = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProprietariesItem(
                        iconRes = R.drawable.pressure_icon,
                        data = weather.pressure,
                        label = stringResource(R.string.pressure)
                    )
                    ProprietariesItem(
                        iconRes = R.drawable.humaditiy_icon,
                        data = weather.humidity,
                        label = stringResource(R.string.humidity)
                    )
                    ProprietariesItem(
                        iconRes = R.drawable.wind_icon,
                        data = weather.windSpeed.toInt(),
                        label = stringResource(R.string.wind_speed)
                    )
                }
            }
            Spacers.VerticalSpacer(30.dp)
            Text(
                stringResource(R.string.today),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacers.VerticalSpacer(12.dp)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(10) {
                    TodayForecastItem(
                        hour = "12 PM",
                        temperature = "28°C",
                        weatherIconRes = R.drawable.clear_n
                    )
                }
            }
            Spacers.VerticalSpacer(30.dp)
            Text(
                stringResource(R.string.weekly_forecast),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacers.VerticalSpacer(12.dp)
        }
        items(count = 10) { index ->
            WeeklyForecastItem(
                day = "Monday",
                weatherDescription = "Clear sky",
                temperature = "28°C",
                weatherIconRes = R.drawable.clear_n
            )
            if (index < 9) {
                Spacers.VerticalSpacer(8.dp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF352163)
@Composable
private fun HomePrev() {
    HomeContent(
        UiState.Success(
            Weather(
                city = "Cairo",
                country = "Egypt",
                temperature = 30.0,
                humidity = 20,
                windSpeed = 90.0,
                pressure = 1000,
                clouds = 0,
                description = "Clear sky",
                icon = R.drawable.logo
            )
        ),
    )
}
