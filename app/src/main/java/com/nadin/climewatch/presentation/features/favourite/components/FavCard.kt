package com.nadin.climewatch.presentation.features.favourite.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nadin.climewatch.data.features.weather.model.FavoriteLocation
import com.nadin.climewatch.presentation.ui.theme.AppGradient
import com.nadin.climewatch.presentation.ui.theme.IconBackgroundColor
import com.nadin.climewatch.presentation.ui.theme.PrimaryColor
import com.nadin.climewatch.presentation.ui.theme.PrimaryDarkColor
import com.nadin.climewatch.presentation.ui.theme.PrimaryLightColor
import com.nadin.climewatch.presentation.ui.theme.SecondaryTextColor
import com.nadin.climewatch.presentation.utils.components.Spacers

@Composable
fun FavCard(
    location: FavoriteLocation,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        },
    )

    LaunchedEffect(location.id) {
        dismissState.reset()
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val progress = dismissState.progress
            val isActive = dismissState.targetValue == SwipeToDismissBoxValue.EndToStart
                    || progress > 0.05f

            val color by animateColorAsState(
                targetValue = when {
                    isActive -> Color.Red.copy(alpha = 0.3f)
                    else -> Color.Transparent
                },
                label = "swipe_color"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(color)
                    .padding(end = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (isActive) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

        }
    ) {
        Card(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(
                width = 1.dp,
                brush = AppGradient.buttonLinearGradient
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                PrimaryDarkColor.copy(alpha = 0.8f),
                                PrimaryColor.copy(alpha = 0.6f)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(color = IconBackgroundColor, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = PrimaryColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacers.HorizontalSpacer(12.dp)

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = location.city,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacers.VerticalSpacer(4.dp)
                        Text(
                            text = location.country,
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryTextColor
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${"%.2f".format(location.lat)}°N",
                            style = MaterialTheme.typography.labelSmall,
                            color = SecondaryTextColor
                        )
                        Text(
                            text = "${"%.2f".format(location.lon)}°E",
                            style = MaterialTheme.typography.labelSmall,
                            color = SecondaryTextColor
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun cardprev() {
    FavCard(
        location = FavoriteLocation(
            id = 1,
            city = "New York",
            country = "USA",
            lat = 40.7128,
            lon = -74.0060
        ),
        onClick = {},
        onDelete = {}
    )
}