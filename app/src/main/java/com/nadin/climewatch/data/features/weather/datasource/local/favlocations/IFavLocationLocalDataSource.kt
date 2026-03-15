package com.nadin.climewatch.data.features.weather.datasource.local.favlocations

import com.nadin.climewatch.data.features.weather.entites.FavoriteLocation
import kotlinx.coroutines.flow.Flow

interface IFavLocationLocalDataSource {
    suspend fun insertLocation(location: FavoriteLocation)

    suspend fun deleteLocation(location: FavoriteLocation)

    fun getAllLocations(): Flow<List<FavoriteLocation>>
}