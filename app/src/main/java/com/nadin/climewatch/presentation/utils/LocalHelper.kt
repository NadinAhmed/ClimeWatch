package com.nadin.climewatch.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocalHelper {

    fun applyLocal(context: Context, languageCode: String): Context {
        val local = Locale(languageCode)
        Locale.setDefault(local)

        val config = Configuration(context.resources.configuration)
        config.setLocale(local)

        return context.createConfigurationContext(config)
    }

    fun applyLocalToActivity(activity: Activity, languageCode: String) {
        val local = Locale(languageCode)
        Locale.setDefault(local)

        val config = Configuration(activity.resources.configuration)
        config.setLocale(local)

        activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
    }

}