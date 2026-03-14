package com.nadin.climewatch.presentation.features.settings

sealed class SettingsEvent {
    object OpenMapPicker : SettingsEvent()
    object RestartApp : SettingsEvent()
}