package com.nadin.climewatch.presentation.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.nadin.climewatch.R

private val AppColorScheme = darkColorScheme(
    primary = PrimaryDarkColor,
    secondary = PrimaryColor,
    tertiary = PrimaryLightColor,
    surface = Color.White.copy(alpha = 0.4f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = SecondaryTextColor,
    onSurface = PrimaryColor,
    onSurfaceVariant = PrimaryColor,
    surfaceContainer = SecondaryTextColor,
    surfaceContainerLow = SecondaryTextColor,
    surfaceContainerHigh = SecondaryTextColor
)

@Composable
fun ClimeWatchTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppGradient.primaryLinearGradient)
        ) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.2f),
                contentScale = ContentScale.Crop
            )
            content()
        }
    }
}