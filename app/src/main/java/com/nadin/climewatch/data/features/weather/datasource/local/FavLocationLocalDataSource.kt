package com.nadin.climewatch.data.features.weather.datasource.local

import android.content.Context
import com.nadin.climewatch.data.db.AppDatabase
import com.nadin.climewatch.data.features.weather.model.FavoriteLocation

class FavLocationLocalDataSource(context: Context) {
    val favLocationDao: FavLocationDao = AppDatabase.getInstance(context).favLocationsDao()

    suspend fun insertLocation(location: FavoriteLocation) {
        favLocationDao.insertLocation(location)
    }

    suspend fun deleteLocation(location: FavoriteLocation) {
        favLocationDao.deleteLocation(location)
    }

    fun getAllLocations() = favLocationDao.getAllLocations()
}