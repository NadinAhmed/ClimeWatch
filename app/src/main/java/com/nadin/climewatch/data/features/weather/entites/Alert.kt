package com.nadin.climewatch.data.features.weather.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class Alert(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val startTime : Long,
    val endTime : Long,
    val alertType : AlertType,
)

enum class AlertType {
    NOTIFICATION,
    ALARM
}


