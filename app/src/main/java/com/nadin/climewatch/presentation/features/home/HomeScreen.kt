package com.nadin.climewatch.presentation.features.home

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nadin.climewatch.R
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.data.features.weather.model.Forecast
import com.nadin.climewatch.data.features.weather.model.Weather
import com.nadin.climewatch.presentation.features.home.components.ProprietariesItem
import com.nadin.climewatch.presentation.features.home.components.TodayForecastItem
import com.nadin.climewatch.presentation.features.home.components.WeeklyForecastItem
import com.nadin.climewatch.presentation.ui.theme.LabelLightColor
import com.nadin.climewatch.presentation.utils.IconsMapper.getWeatherIcon
import com.nadin.climewatch.presentation.utils.Location
import com.nadin.climewatch.presentation.utils.Location.enableLocationServices
import com.nadin.climewatch.presentation.utils.Location.isLocEnabled
import com.nadin.climewatch.presentation.utils.states.ResultState
import com.nadin.climewatch.presentation.utils.components.ErrorScreen
import com.nadin.climewatch.presentation.utils.components.LoadingScreen
import com.nadin.climewatch.presentation.utils.components.Spacers

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(cityName: String? = null, lat: Double? = null, lon: Double? = null) {

    val context = LocalContext.current
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(
            weatherRepository = WeatherRepository(context),
            app = context.applicationContext as Application
        )
    )

    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()
    val forecastState by viewModel.forecastState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            if (isLocEnabled(context)) {
                viewModel.loadWeatherForCurrentLocation()
            } else {
                enableLocationServices(context)
                viewModel.loadWeatherForCurrentLocation()
            }
        }
    }


    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (cityName != null) {
                    viewModel.loadWeatherForSpecificLocation(cityName)
                } else if (lat != null && lon != null) {
                    viewModel.loadWeatherForSpecificLocation(lat, lon)
                } else {
                    if (Location.checkLocationPermission(context)) {
                        if (isLocEnabled(context)) {
                            viewModel.loadWeatherForCurrentLocation()
                        } else {
                            enableLocationServices(context)
                            viewModel.loadWeatherForCurrentLocation()
                        }
                    } else {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    HomeContent(weatherState, forecastState)
}

@Composable
fun HomeContent(
    weatherState: ResultState<Weather>,
    forecastState: ResultState<Forecast>,
    modifier: Modifier = Modifier
) {
    when {
        weatherState is ResultState.Loading || forecastState is ResultState.Loading -> {
            LoadingScreen(modifier = modifier.fillMaxSize())
        }

        weatherState is ResultState.Error -> {
            ErrorScreen(weatherState.message, modifier = modifier.fillMaxSize())
        }

        forecastState is ResultState.Error -> {
            ErrorScreen(forecastState.message, modifier = modifier.fillMaxSize())
        }

        weatherState is ResultState.Success && forecastState is ResultState.Success -> {
            HomeSuccessContent(
                weather = weatherState.data,
                forecast = forecastState.data,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun HomeSuccessContent(
    weather: Weather,
    forecast: Forecast,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
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
                painter = painterResource(id = getWeatherIcon(weather.icon)),
                contentDescription = "Weather Image",
                modifier = Modifier.size(120.dp)
            )
            Spacers.VerticalSpacer(28.dp)
            Text(
                "${weather.temperature} °",
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
                items(forecast.hourly.size) { index ->
                    val hourly = forecast.hourly[index]
                    TodayForecastItem(
                        hour = hourly.time,
                        temperature = hourly.temperature,
                        weatherIconRes = getWeatherIcon(hourly.icon)
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
        items(forecast.weekly.size) { index ->
            val daily = forecast.weekly[index]
            WeeklyForecastItem(
                day = daily.day,
                weatherDescription = daily.description,
                temperature = daily.temperature,
                weatherIconRes = getWeatherIcon(daily.icon)
            )
            Spacers.VerticalSpacer(8.dp)
        }
    }
}
