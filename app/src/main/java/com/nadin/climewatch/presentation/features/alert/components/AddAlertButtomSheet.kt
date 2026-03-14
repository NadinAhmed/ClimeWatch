package com.nadin.climewatch.presentation.features.alert.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nadin.climewatch.R
import com.nadin.climewatch.data.features.weather.entites.Alert
import com.nadin.climewatch.data.features.weather.entites.AlertType
import com.nadin.climewatch.presentation.ui.theme.PrimaryColor
import com.nadin.climewatch.presentation.utils.Date.convertToTimestamp
import com.nadin.climewatch.presentation.utils.Date.formatTime
import com.nadin.climewatch.presentation.utils.components.Spacers

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlertBottomSheet(
    onDismiss: () -> Unit,
    onSave: (Alert, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var startTime by remember { mutableStateOf<Long?>(null) }
    var endTime by remember { mutableStateOf<Long?>(null) }
    var alertType by remember { mutableStateOf(AlertType.NOTIFICATION) }

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
        ) {
            Text(
                text = stringResource(R.string.new_weather_alert),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacers.VerticalSpacer(20.dp)
            OutlinedButton(
                onClick = { showStartTimePicker = true },
                border = BorderStroke(1.dp, color = PrimaryColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacers.HorizontalSpacer(8.dp)
                Text(
                    text = if (startTime != null) stringResource(R.string.from) + formatTime(
                        startTime!!
                    )
                    else stringResource(R.string.select_start_time)
                )
            }

            OutlinedButton(
                onClick = { showEndTimePicker = true },
                border = BorderStroke(1.dp, color = PrimaryColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacers.HorizontalSpacer(8.dp)
                Text(
                    text = if (endTime != null) stringResource(R.string.to) + formatTime(endTime!!)
                    else stringResource(R.string.select_end_time)
                )
            }
            Spacers.VerticalSpacer(20.dp)
            Text(
                text = stringResource(R.string.alert_type),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacers.VerticalSpacer(4.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = alertType == AlertType.NOTIFICATION,
                    onClick = { alertType = AlertType.NOTIFICATION },
                    label = {
                        Text(
                            text = stringResource(R.string.notification),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryColor,
                        selectedLabelColor = Color.White,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = alertType == AlertType.NOTIFICATION,
                        borderColor = PrimaryColor,
                        selectedBorderColor = PrimaryColor,
                        borderWidth = 1.dp,
                        selectedBorderWidth = 1.dp
                    ),
                    modifier = Modifier.weight(1f)
                )

                FilterChip(
                    selected = alertType == AlertType.ALARM,
                    onClick = { alertType = AlertType.ALARM },
                    label = {
                        Text(
                            stringResource(R.string.alarm),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryColor,
                        selectedLabelColor = Color.White,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = alertType == AlertType.ALARM,
                        borderColor = PrimaryColor,
                        selectedBorderColor = PrimaryColor,
                        borderWidth = 1.dp,
                        selectedBorderWidth = 1.dp
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacers.VerticalSpacer(20.dp)
            Button(
                onClick = {
                    if (startTime != null && endTime != null) {
                        onSave(
                            Alert(
                                startTime = startTime!!,
                                endTime = endTime!!,
                                alertType = alertType
                            ),
                            "Cairo"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = startTime != null && endTime != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor,
                    contentColor = Color.White,
                    disabledContainerColor = PrimaryColor.copy(alpha = 0.4f),
                    disabledContentColor = Color.White.copy(alpha = 0.4f)
                )
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }

    // Time Pickers
    if (showStartTimePicker) {
        WeatherTimePicker(
            onDismiss = { showStartTimePicker = false },
            onConfirm = { hour, minute ->
                startTime = convertToTimestamp(hour, minute)
                showStartTimePicker = false
            }
        )
    }

    if (showEndTimePicker) {
        WeatherTimePicker(
            onDismiss = { showEndTimePicker = false },
            onConfirm = { hour, minute ->
                endTime = convertToTimestamp(hour, minute)
                showEndTimePicker = false
            }
        )
    }
}