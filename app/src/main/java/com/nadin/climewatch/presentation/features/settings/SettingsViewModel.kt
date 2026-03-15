package com.nadin.climewatch.presentation.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nadin.climewatch.data.local.ISettingsDataStore
import com.nadin.climewatch.data.local.SettingsDataStore
import com.nadin.climewatch.data.model.AppLanguage
import com.nadin.climewatch.data.model.LocationMode
import com.nadin.climewatch.data.model.SettingsState
import com.nadin.climewatch.data.model.Units
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsDataStore: ISettingsDataStore
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<SettingsEvent>()
    val event: SharedFlow<SettingsEvent> = _event.asSharedFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            combine(
                settingsDataStore.language,
                settingsDataStore.units,
                settingsDataStore.locationMode,
                settingsDataStore.selectedLat,
                settingsDataStore.selectedLon
            ) { language, units, locationMode, lat, lon ->
                SettingsState(
                    locationMode = when (locationMode) {
                        "gps" -> LocationMode.GPS
                        "map" -> LocationMode.MAP
                        else -> LocationMode.GPS
                    },
                    selectedLat = lat,
                    selectedLon = lon,
                    units = when (units) {
                        "metric" -> Units.METRIC
                        "imperial" -> Units.IMPERIAL
                        else -> Units.STANDARD
                    },
                    language = when (language) {
                        "en" -> AppLanguage.ENGLISH
                        "ar" -> AppLanguage.ARABIC
                        else -> AppLanguage.ENGLISH
                    },
                    isLoading = false
                )
            }.collect {
                _state.value = it
            }
        }
    }

    fun onLocationModeSelected(mode: LocationMode) {
        viewModelScope.launch {
            settingsDataStore.saveLocationMode(
                when (mode) {
                    LocationMode.GPS -> "gps"
                    LocationMode.MAP -> "map"
                }
            )
            if (mode == LocationMode.MAP) {
                _event.emit(SettingsEvent.OpenMapPicker)
            }
        }
    }

    fun onUnitsSelected(units: Units) {
        viewModelScope.launch {
            settingsDataStore.saveUnits(
                units.apiValue
            )
        }
    }

    fun onLanguageSelected(language: AppLanguage) {
        viewModelScope.launch {
            settingsDataStore.saveLanguage(
                language.code
            )
            _event.emit(SettingsEvent.RestartApp)
        }
    }
}

class SettingsViewModelFactory(private val settingsDataStore: SettingsDataStore) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(settingsDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
