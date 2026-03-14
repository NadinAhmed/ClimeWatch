package com.nadin.climewatch.presentation.features.alert.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.nadin.climewatch.R
import com.nadin.climewatch.presentation.ui.theme.PrimaryColor
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherTimePicker(
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
        initialMinute = Calendar.getInstance().get(Calendar.MINUTE),
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(timePickerState.hour, timePickerState.minute) }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        text = {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = Color.White.copy(alpha = 0.2f),
                    selectorColor = PrimaryColor,
                    clockDialSelectedContentColor = Color.White,
                    clockDialUnselectedContentColor = Color.White,
                    timeSelectorSelectedContainerColor = PrimaryColor,
                    timeSelectorUnselectedContainerColor = Color.White.copy(alpha = 0.2f),
                    timeSelectorSelectedContentColor = Color.White,
                    timeSelectorUnselectedContentColor = Color.White,
                    periodSelectorSelectedContainerColor = PrimaryColor,
                    periodSelectorUnselectedContainerColor = Color.White.copy(alpha = 0.2f),
                )
            )
        }
    )
}