package com.nadin.climewatch.presentation.features.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nadin.climewatch.R
import com.nadin.climewatch.data.model.AppLanguage

@Composable
fun LanguageSettingsCard(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    Column {
        SettingsOptionRow(
            icon     = Icons.Default.Language,
            title    = stringResource(R.string.english),
            subtitle = "en",
            selected = selectedLanguage == AppLanguage.ENGLISH,
            onClick  = { onLanguageSelected(AppLanguage.ENGLISH) }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )

        SettingsOptionRow(
            icon     = Icons.Default.Language,
            title    = stringResource(R.string.arabic),
            subtitle = "ar",
            selected = selectedLanguage == AppLanguage.ARABIC,
            onClick  = { onLanguageSelected(AppLanguage.ARABIC) }
        )
    }
}