package com.nadin.climewatch.presentation.utils

import android.content.Context
import androidx.activity.ComponentActivity
import com.nadin.climewatch.data.local.SettingsDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

abstract class BaseActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val languageCode = runBlocking {
            SettingsDataStore(newBase).language.first()
        }
        val localeContext = LocalHelper.applyLocal(newBase, languageCode)
        super.attachBaseContext(localeContext)
    }
}