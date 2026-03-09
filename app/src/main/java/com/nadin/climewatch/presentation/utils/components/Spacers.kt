package com.nadin.climewatch.presentation.utils.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

class Spacers() {
    companion object {
        @Composable
        fun VerticalSpacer(height: Dp) = Spacer(modifier = Modifier.Companion.height(height))

        @Composable
        fun HorizontalSpacer(width: Dp) = Spacer(modifier = Modifier.Companion.width(width))
    }
}