package com.nadin.climewatch.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
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
}