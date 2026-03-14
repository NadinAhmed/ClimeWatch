package com.nadin.climewatch.presentation.features.settings

import androidx.compose.ui.graphics.vector.ImageVector
import com.nadin.climewatch.data.model.Units

data class UnitOption(
    val unit: Units,
    val icon: ImageVector,
    val label: String,
    val description: String
)
