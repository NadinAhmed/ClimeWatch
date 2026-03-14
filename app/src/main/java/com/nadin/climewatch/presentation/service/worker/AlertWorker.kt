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
import com.nadin.climewatch.data.features.weather.model.Weather
import com.nadin.climewatch.presentation.service.AlarmService
import com.nadin.climewatch.presentation.service.notification.NotificationHelper

class AlertWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val EXTRA_ALERT_TYPE = "alert_type"
        const val EXTRA_CITY = "alert_city"
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val alertType = inputData.getString(EXTRA_ALERT_TYPE) ?: return Result.failure()
        val city = inputData.getString(EXTRA_CITY) ?: return Result.failure()
        val endTime = System.currentTimeMillis() + 15 * 60 * 1000 // 15 minutes from now

        if (System.currentTimeMillis() > endTime) {
            WorkManager.getInstance(context).cancelUniqueWork(id.toString())
            return Result.success()
        }

        return try {
            val weatherData = WeatherRepository(context).getWeatherByCity(city)
            if (weatherData.isSuccess) {
                val weather = weatherData.getOrNull()
                if (weather != null) {
                    when (alertType) {
                        AlertType.NOTIFICATION.name -> showNotification(weather)
                        AlertType.ALARM.name -> startAlarmService(weather)
                    }
                    Result.success()
                } else {
                    Result.failure()
                }
            } else {
                Result.failure()
            }

        } catch (e: Exception) {
            Result.Retry()
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