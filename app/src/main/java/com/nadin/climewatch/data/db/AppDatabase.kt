package com.nadin.climewatch.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nadin.climewatch.data.features.weather.datasource.local.FavLocationDao
import com.nadin.climewatch.data.features.weather.model.FavoriteLocation

@Database(entities = [FavoriteLocation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favLocationsDao(): FavLocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "favorites_locations"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
