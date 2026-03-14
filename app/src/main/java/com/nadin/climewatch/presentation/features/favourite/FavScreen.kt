package com.nadin.climewatch.presentation.features.favourite

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nadin.climewatch.R
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.presentation.features.favourite.components.FavCard
import com.nadin.climewatch.presentation.ui.theme.AppGradient
import com.nadin.climewatch.presentation.utils.NavigationRoutes
import com.nadin.climewatch.presentation.utils.components.EmptyScreen
import com.nadin.climewatch.presentation.utils.components.ErrorScreen
import com.nadin.climewatch.presentation.utils.components.LoadingScreen
import com.nadin.climewatch.presentation.utils.components.Spacers
import com.nadin.climewatch.presentation.utils.states.ResultState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavScreen(navController: NavController) {

    val context = LocalContext.current
    val viewModel: FavViewModel = viewModel(
        factory = FavViewModelFactory(
            repository = WeatherRepository(context)
        )
    )

    val favorites by viewModel.favorites.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.favEvent.collect { event ->
            when (event) {
                is FavEvent.GoToMap -> {
                    navController.navigate(NavigationRoutes.MapPicker.route)
                }

                is FavEvent.GoToHome -> {
                    navController.navigate(
                        NavigationRoutes.Home.createRoute(event.lat, event.lon)
                    )
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        brush = AppGradient.buttonLinearGradient
                    )
                    .clickable { viewModel.onFabClicked() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Location",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = favorites) {
                is ResultState.Loading -> {
                    LoadingScreen()
                }

                is ResultState.Success -> {
                    if (state.data.isEmpty()) {
                        EmptyScreen(
                            R.drawable.heart,
                            stringResource(R.string.no_favourite_locations_yet),
                            stringResource(R.string.tap_to_add_one)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Text(
                                    text = stringResource(R.string.favourite_locations),
                                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = stringResource(R.string.favourite_locations_decsription),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }
                            items(state.data, key = { it.id }) { location ->
                                FavCard(
                                    modifier = Modifier.animateItem(),
                                    location = location,
                                    onClick = { viewModel.onFavCardClicked(location) },
                                    onDelete = { viewModel.deleteLocation(location) }
                                )
                            }
                        }
                    }
                }

                is ResultState.Error -> {
                    ErrorScreen(state.message)
                }
            }
        }
    }
}