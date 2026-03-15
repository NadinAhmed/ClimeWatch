package com.nadin.climewatch.presentation.utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.os.ConfigurationCompat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
object Date {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun toLocalDateTime(dateTime: String): LocalDateTime {
        return LocalDateTime.parse(dateTime, formatter)
    }

    fun extractHour(dateTime: String): String {
        return toLocalDateTime(dateTime)
            .format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun extractDayName(dateTime: String): String {
        return toLocalDateTime(dateTime)
            .dayOfWeek
            .getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    fun isToday(dateTime: String): Boolean {
        return toLocalDateTime(dateTime)
            .toLocalDate() == LocalDate.now()
    }

    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun convertToTimestamp(hour: Int, minute: Int): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun getTodayDateString(context: Context): String {
        val locale = ConfigurationCompat.getLocales(context.resources.configuration)[0] ?: Locale.getDefault()
        val formatter = SimpleDateFormat("EEEE, dd MMM yyyy", locale)
        return formatter.format(Date())
    }
}