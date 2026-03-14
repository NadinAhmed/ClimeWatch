package com.nadin.climewatch.presentation.service.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nadin.climewatch.R
import com.nadin.climewatch.presentation.service.AlarmService

class NotificationHelper(private val context: Context) {
    companion object {
        const val WEATHER_CHANNEL_ID = "weather_channel"
        const val ALARM_CHANNEL_ID = "alarm_channel"
        const val WEATHER_NOTIFICATION_ID = 1001
        const val ALARM_NOTIFICATION_ID = 1002
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val weatherChannel = NotificationChannel(
                WEATHER_CHANNEL_ID,
                "Weather Alerts",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Weather updates during alert period"
            }

            val alarmChannel = NotificationChannel(
                ALARM_CHANNEL_ID,
                "Alarm Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alarm sound weather alerts"
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()

                )
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(weatherChannel)
            manager.createNotificationChannel(alarmChannel)
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showWeatherNotification(
        temperature: Double,
        description: String,
        city: String
    ) {
        val notification = NotificationCompat.Builder(context, WEATHER_CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Weather Update - $city")
            .setContentText("$temperature° | $description")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(WEATHER_NOTIFICATION_ID, notification)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showAlarmNotification(
        temperature: String,
        description: String,
        city: String
    ): Notification {

        val dismissIntent = Intent(context, AlarmService::class.java).apply {
            action = AlarmService.ACTION_DISMISS
        }
        val dismissPendingIntent = PendingIntent.getService(
            context, 0, dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Weather Alarm - $city")
            .setContentText("$temperature° | $description")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOngoing(true)
            .addAction(R.drawable.pressure_icon, "Dismiss", dismissPendingIntent)
            .build()

    }
}