package com.nadin.climewatch.presentation.features.settings

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nadin.climewatch.MainActivity
import com.nadin.climewatch.R
import com.nadin.climewatch.data.local.SettingsDataStore
import com.nadin.climewatch.presentation.features.settings.components.LanguageSettingsCard
import com.nadin.climewatch.presentation.features.settings.components.LocationSettingsCard
import com.nadin.climewatch.presentation.features.settings.components.SettingsSection
import com.nadin.climewatch.presentation.features.settings.components.UnitsSettingsCard
import com.nadin.climewatch.presentation.utils.components.LoadingScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(
    onOpenMapPicker: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as MainActivity
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(
            settingsDataStore = SettingsDataStore(context),
        )
    )

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SettingsEvent.OpenMapPicker -> onOpenMapPicker()
                is SettingsEvent.RestartApp -> activity.restartActivity()
            }
        }
    }


    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingScreen()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingsSection(title = "Location") {
                    LocationSettingsCard(
                        selectedMode = state.locationMode,
                        selectedLat = state.selectedLat,
                        selectedLon = state.selectedLon,
                        onModeSelected = { viewModel.onLocationModeSelected(it) }
                    )
                }

                SettingsSection(title = "Units") {
                    UnitsSettingsCard(
                        selectedUnits = state.units,
                        onUnitsSelected = { viewModel.onUnitsSelected(it) }
                    )
                }

                SettingsSection(title = "Language") {
                    LanguageSettingsCard(
                        selectedLanguage = state.language,
                        onLanguageSelected = { viewModel.onLanguageSelected(it) }
                    )
                }
            }
        }
    }
}
