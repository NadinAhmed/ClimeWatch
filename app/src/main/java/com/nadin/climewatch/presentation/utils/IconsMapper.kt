package com.nadin.climewatch.presentation.utils

import com.nadin.climewatch.R

object IconsMapper {
        fun getWeatherIcon(icon: String): Int {
            return when (icon) {
                "01d" -> R.drawable.clear_n
                "01n" -> R.drawable.clear_n
                "02d" -> R.drawable.clear_n
                "02n" -> R.drawable.clear_n
                "03d" -> R.drawable.clear_n
                "03n" -> R.drawable.clear_n
                "04d" -> R.drawable.clear_n
                "04n" -> R.drawable.clear_n
                "09d" -> R.drawable.clear_n
                "09n" -> R.drawable.clear_n
                "10d" -> R.drawable.clear_n
                "10n" -> R.drawable.clear_n
                "11d" -> R.drawable.clear_n
                "11n" -> R.drawable.clear_n
                "13d" -> R.drawable.clear_n
                "13n" -> R.drawable.clear_n
                "50d" -> R.drawable.clear_n
                "50n" -> R.drawable.clear_n
                else -> R.drawable.clear_n
            }
        }
}