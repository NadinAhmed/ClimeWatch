package com.nadin.climewatch.presentation.service.worker

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.data.features.weather.entites.AlertType
import com.nadin.climewatch.data.local.SettingsDataStore
import com.nadin.climewatch.data.model.Weather
import com.nadin.climewatch.presentation.service.AlarmService
import com.nadin.climewatch.presentation.service.notification.NotificationHelper

class AlertWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val EXTRA_ALERT_ID = "alert_id"
        const val EXTRA_ALERT_TYPE = "alert_type"
        const val EXTRA_CITY = "alert_city"
        const val EXTRA_END_TIME = "alert_end_time"
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val alertId = inputData.getInt(EXTRA_ALERT_ID, -1)
        if (alertId == -1) return Result.failure()

        val alertTypeString = inputData.getString(EXTRA_ALERT_TYPE) ?: return Result.failure()
        val alertType = AlertType.valueOf(alertTypeString)
        val city = inputData.getString(EXTRA_CITY) ?: return Result.failure()
        val endTime = inputData.getLong(EXTRA_END_TIME, -1L)
        if (endTime <= 0L) return Result.failure()

        if (System.currentTimeMillis() > endTime) {
            WorkManager.getInstance(context).cancelUniqueWork(alertId.toString())
            return Result.success()
        }

        return try {
            val settingsDataStore = SettingsDataStore(context)
            val weatherData = WeatherRepository.getInstance(context).getWeatherByCity(city)
            if (weatherData.isSuccess) {
                val weather = weatherData.getOrNull()
                if (weather != null) {
                    when (alertType) {
                        AlertType.NOTIFICATION -> showNotification(weather)
                        AlertType.ALARM -> startAlarmService(weather)
                    }
                    Result.success()
                } else {
                    Result.failure()
                }
            } else {
                Result.failure()
            }

        } catch (e: Exception) {
            Result.retry()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startAlarmService(weather: Weather) {
        val intent = Intent(context, AlarmService::class.java).apply {
            putExtra(AlarmService.EXTRA_TEMPERATURE, weather.temperature)
            putExtra(AlarmService.EXTRA_DESCRIPTION, weather.description)
            putExtra(AlarmService.EXTRA_CITY, weather.city)
        }
        context.startForegroundService(intent)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(weather: Weather) {
        val notificationHelper = NotificationHelper(context)
        notificationHelper.showWeatherNotification(
            temperature = weather.temperature,
            description = weather.description,
            city = weather.city
        )
    }
}