package com.nadin.climewatch.presentation.features.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nadin.climewatch.R
import com.nadin.climewatch.data.model.Units
import com.nadin.climewatch.presentation.features.settings.UnitOption

@Composable
fun UnitsSettingsCard(
    selectedUnits: Units,
    onUnitsSelected: (Units) -> Unit
) {
    val options = listOf(
        UnitOption(
            Units.METRIC, Icons.Default.Thermostat,
            stringResource(R.string.metric), stringResource(R.string.c_m_s_mm)
        ),
        UnitOption(
            Units.IMPERIAL, Icons.Default.WbSunny,
            stringResource(R.string.imperial), stringResource(R.string.f_mph_in)
        ),
        UnitOption(
            Units.STANDARD, Icons.Default.Science,
            stringResource(R.string.standard), stringResource(R.string.k_m_s_mm)
        )
    )

    Column {
        options.forEachIndexed { index, (unit, icon, title, subtitle) ->
            SettingsOptionRow(
                icon = icon,
                title = title,
                selected = selectedUnits == unit,
                onClick = { onUnitsSelected(unit) },
                subtitle = subtitle,
            )
            if (index < options.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            }
        }
    }
}