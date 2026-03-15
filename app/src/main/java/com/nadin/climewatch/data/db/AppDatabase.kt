package com.nadin.climewatch.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nadin.climewatch.data.features.weather.datasource.local.alert.AlertDao
import com.nadin.climewatch.data.features.weather.datasource.local.favlocations.FavLocationDao
import com.nadin.climewatch.data.features.weather.entites.Alert
import com.nadin.climewatch.data.features.weather.entites.FavoriteLocation

@Database(entities = [FavoriteLocation::class, Alert::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favLocationsDao(): FavLocationDao
    abstract fun alertDao(): AlertDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weather_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
