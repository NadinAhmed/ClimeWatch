package com.nadin.climewatch.data.features.weather.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nadin.climewatch.data.features.weather.entites.FavoriteLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface FavLocationDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertLocation(location: FavoriteLocation)

    @Delete
    suspend fun deleteLocation(location: FavoriteLocation)

    @Query("SELECT * FROM favorites_locations")
    fun getAllLocations(): Flow<List<FavoriteLocation>>
}