package com.nadin.climewatch.presentation.service

import android.Manifest
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.IBinder
import androidx.annotation.RequiresPermission
import com.nadin.climewatch.presentation.service.notification.NotificationHelper
import java.util.Locale

class AlarmService : Service() {

    companion object {
        const val ACTION_DISMISS = "ACTION_DISMISS"
        const val EXTRA_TEMPERATURE = "temperature"
        const val EXTRA_DESCRIPTION = "description"
        const val EXTRA_CITY = "city"
    }

    private lateinit var notificationHelper: NotificationHelper
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            ACTION_DISMISS -> stopAlarm()
            else -> {
                val temperatureValue = intent?.getDoubleExtra(EXTRA_TEMPERATURE, Double.NaN)
                val temperature = temperatureValue
                    ?.takeUnless { it.isNaN() }
                    ?.let { formatTemperature(it) }
                    ?: ""
                val description = intent?.getStringExtra(EXTRA_DESCRIPTION) ?: ""
                val city = intent?.getStringExtra(EXTRA_CITY) ?: ""

                startAlarm(temperature, description, city)
            }
        }

        return START_NOT_STICKY
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun startAlarm(temperature: String, description: String, city: String) {
        val notification = notificationHelper.showAlarmNotification(temperature, description, city)

        startForeground(NotificationHelper.ALARM_NOTIFICATION_ID, notification)
        playAlarmSound()
    }

    private fun playAlarmSound() {
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mediaPlayer = MediaPlayer().apply {
            setDataSource(applicationContext, alarmUri)
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            isLooping = true
            prepare()
            start()
        }
    }

    private fun stopAlarm() {
        mediaPlayer?.apply {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm()
    }

    override fun onBind(intent: Intent): IBinder? = null

    private fun formatTemperature(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            String.format(Locale.getDefault(), "%.1f", value)
        }
    }
}
