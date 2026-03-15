package com.nadin.climewatch.data.features.weather.datasource.local.alert

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nadin.climewatch.data.features.weather.entites.Alert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: Alert): Long

    @Delete
    suspend fun deleteAlert(alert: Alert)

    @Query("SELECT * FROM alerts")
    fun getAllAlerts(): Flow<List<Alert>>
}