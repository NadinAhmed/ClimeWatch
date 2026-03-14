package com.nadin.climewatch.data.features.weather.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_locations")
data class FavoriteLocation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val city: String,
    val country: String,
    val lat: Double,
    val lon: Double
)