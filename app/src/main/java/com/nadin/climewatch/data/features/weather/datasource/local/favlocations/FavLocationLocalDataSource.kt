package com.nadin.climewatch.data.features.weather.datasource.local.favlocations

import android.content.Context
import com.nadin.climewatch.data.db.AppDatabase
import com.nadin.climewatch.data.features.weather.entites.FavoriteLocation

class FavLocationLocalDataSource(context: Context) : IFavLocationLocalDataSource {
    val favLocationDao: FavLocationDao = AppDatabase.getInstance(context).favLocationsDao()

    override suspend fun insertLocation(location: FavoriteLocation) {
        favLocationDao.insertLocation(location)
    }

    override suspend fun deleteLocation(location: FavoriteLocation) {
        favLocationDao.deleteLocation(location)
    }

    override fun getAllLocations() = favLocationDao.getAllLocations()
}