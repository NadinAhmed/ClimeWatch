package com.nadin.climewatch.data.features.weather.datasource.local.alert

import com.nadin.climewatch.data.features.weather.entites.Alert
import kotlinx.coroutines.flow.Flow

interface IAlertLocalDataSource {
    suspend fun insertAlert(alert: Alert): Int
    suspend fun deleteAlert(alert: Alert)
    fun getAllAlerts(): Flow<List<Alert>>
}