package com.nadin.climewatch.presentation.features.alert

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nadin.climewatch.R
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.data.local.CityPreferences
import com.nadin.climewatch.presentation.features.alert.components.AddAlertBottomSheet
import com.nadin.climewatch.presentation.features.alert.components.AlertItem
import com.nadin.climewatch.presentation.ui.theme.AppGradient
import com.nadin.climewatch.presentation.utils.components.EmptyScreen
import com.nadin.climewatch.presentation.utils.components.ErrorScreen
import com.nadin.climewatch.presentation.utils.components.LoadingScreen
import com.nadin.climewatch.presentation.utils.states.ResultState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlertScreen() {

    val context = LocalContext.current
    val viewModel: AlertViewModel = viewModel(
        factory = AlertViewModelFactory(
            context.applicationContext,
            repository = WeatherRepository(context)
        )
    )

    val shouldRequestNotificationPermission =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }

    val alertsState by viewModel.alertsState.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }
    var currentCity by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        currentCity = CityPreferences.getCurrentCity(context)
    }

    LaunchedEffect(shouldRequestNotificationPermission) {
        if (!shouldRequestNotificationPermission) return@LaunchedEffect
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (!granted) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(brush = AppGradient.buttonLinearGradient)
                    .clickable { showBottomSheet = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Alert",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->

        when (alertsState) {
            is ResultState.Loading -> LoadingScreen()

            is ResultState.Success -> {
                val alerts = (alertsState as ResultState.Success).data

                if (alerts.isEmpty()) {
                    EmptyScreen(
                        R.drawable.notification_outlined,
                        stringResource(R.string.no_alerts_yet),
                        stringResource(R.string.tap_to_add_one)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(alerts, key = { it.id }) { alert ->
                            AlertItem(
                                alert = alert,
                                onDelete = { viewModel.deleteAlert(alert) }
                            )
                        }
                    }
                }
            }

            is ResultState.Error -> ErrorScreen((alertsState as ResultState.Error).message)
        }

        if (showBottomSheet) {
            AddAlertBottomSheet(
                onDismiss = { showBottomSheet = false },
                onSave = { alert, city ->
                    viewModel.insertAlert(alert, city)
                    showBottomSheet = false
                },
                currentCity = currentCity
            )
        }
    }
}
