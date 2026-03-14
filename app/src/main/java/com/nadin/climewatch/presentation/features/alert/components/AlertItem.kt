package com.nadin.climewatch.presentation.features.alert.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nadin.climewatch.R
import com.nadin.climewatch.data.features.weather.entites.Alert
import com.nadin.climewatch.data.features.weather.entites.AlertType
import com.nadin.climewatch.presentation.ui.theme.LabelLightColor
import com.nadin.climewatch.presentation.ui.theme.SecondaryTextColor
import com.nadin.climewatch.presentation.utils.Date
import com.nadin.climewatch.presentation.utils.components.Spacers

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlertItem(
    alert: Alert,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = LabelLightColor,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    if (alert.alertType == AlertType.ALARM) R.drawable.alarm_icon
                    else R.drawable.notification_icon
                ),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(40.dp)
            )

            Spacers.HorizontalSpacer(12.dp)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${Date.formatTime(alert.startTime)} → ${Date.formatTime(alert.endTime)}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacers.VerticalSpacer(4.dp)
                Text(
                    text = if (alert.alertType == AlertType.ALARM) stringResource(R.string.alarm)
                    else stringResource(R.string.notification),
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryTextColor
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Alert",
                    tint = Color.Red.copy(alpha = 0.3f)
                )
            }
        }
    }
}