package com.nadin.climewatch.presentation.ui.theme

import androidx.compose.ui.graphics.Brush

object AppGradient {
    val primaryLinearGradient = Brush.verticalGradient(
        colors = listOf(
            PrimaryDarkColor,
            PrimaryColor,
            PrimaryLightColor
        )
    )
}

