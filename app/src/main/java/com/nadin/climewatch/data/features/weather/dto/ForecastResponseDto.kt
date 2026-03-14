package com.nadin.climewatch.data.features.weather.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.nadin.climewatch.data.model.DailyForecast
import com.nadin.climewatch.data.model.Forecast
import com.nadin.climewatch.data.model.HourlyForecast
import com.nadin.climewatch.presentation.utils.Date

data class ForecastResponseDto(
    val list: List<ForecastDto>
)

@RequiresApi(Build.VERSION_CODES.O)
fun ForecastResponseDto.toForecast(): Forecast {

    val hourlyForecast = list
        .filter { Date.isToday(it.dt_txt) }
        .map {
            HourlyForecast(
                time = Date.extractHour(it.dt_txt),
                temperature = it.main.temp,
                icon = it.weather.firstOrNull()?.icon?:""
            )
        }

    val weeklyForecast = list
        .filter { it.dt_txt.contains("12:00:00") }
        .map {
            DailyForecast(
                day = Date.extractDayName(it.dt_txt),
                temperature = it.main.temp,
                description = it.weather.firstOrNull()?.description ?: "",
                icon = it.weather.firstOrNull()?.icon ?: ""
            )
        }

    return Forecast(
        hourly = hourlyForecast,
        weekly = weeklyForecast
    )
}