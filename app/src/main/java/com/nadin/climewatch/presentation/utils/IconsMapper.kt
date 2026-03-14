package com.nadin.climewatch.presentation.utils

import com.nadin.climewatch.R

object IconsMapper {
        fun getWeatherIcon(icon: String): Int {
            return when (icon) {
                "01d" -> R.drawable.neight
                "01n" -> R.drawable.sunny
                "02d" -> R.drawable.clouds
                "02n" -> R.drawable.clouds
                "03d" -> R.drawable.clouds
                "03n" -> R.drawable.clouds
                "04d" -> R.drawable.clouds
                "04n" -> R.drawable.clouds
                "09d" -> R.drawable.rain
                "09n" -> R.drawable.rain
                "10d" -> R.drawable.rain
                "10n" -> R.drawable.rain
                "11d" -> R.drawable.thunder
                "11n" -> R.drawable.thunder
                "13d" -> R.drawable.neight_snow
                "13n" -> R.drawable.morning_snow
                "50d" -> R.drawable.wind
                "50n" -> R.drawable.wind
                else -> R.drawable.logo
            }
        }
}