package com.nadin.climewatch.presentation.features.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nadin.climewatch.R
import com.nadin.climewatch.data.model.LocationMode

@Composable
fun LocationSettingsCard(
    selectedMode: LocationMode,
    selectedLat: Double,
    selectedLon: Double,
    onModeSelected: (LocationMode) -> Unit
) {
    Column {
        // GPS Option
        SettingsOptionRow(
            icon = Icons.Default.MyLocation,
            title = stringResource(R.string.gps),
            subtitle = stringResource(R.string.use_current_location),
            selected = selectedMode == LocationMode.GPS,
            onClick = { onModeSelected(LocationMode.GPS) }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )

        // Map Option
        SettingsOptionRow(
            icon = Icons.Default.Map,
            title = stringResource(R.string.choose_from_map),
            subtitle = if (selectedMode == LocationMode.MAP && selectedLat != 0.0)
                "%.4f, %.4f".format(selectedLat, selectedLon)
            else
                stringResource(R.string.pick_a_location_on_the_map),
            selected = selectedMode == LocationMode.MAP,
            onClick = { onModeSelected(LocationMode.MAP) }
        )
    }
}