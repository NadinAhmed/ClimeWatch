package com.nadin.climewatch.presentation.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

object AppGradient {
    val primaryLinearGradient = Brush.verticalGradient(
        colors = listOf(
            PrimaryDarkColor,
            PrimaryColor,
            PrimaryLightColor
        )
    )

    val labelLinearGradient = Brush.linearGradient(
        colors = listOf(
            LabelLightColor,
            LabelDarkColor
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    val buttonLinearGradient = Brush.linearGradient(
        colors = listOf(
            ButtonLightColor,
            ButtonDarkColor
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
}

