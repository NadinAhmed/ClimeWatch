package com.nadin.climewatch.data.features.weather.datasource.local

import android.content.Context
import com.nadin.climewatch.data.db.AppDatabase
import com.nadin.climewatch.data.features.weather.entites.Alert

class AlertLocalDataSource(context: Context) {
    private val alertDao: AlertDao = AppDatabase.getInstance(context).alertDao()

    suspend fun insertAlert(alert: Alert) {
        alertDao.insertAlert(alert)
    }

    suspend fun deleteAlert(alert: Alert) {
        alertDao.deleteAlert(alert)
    }

    fun getAllAlerts() = alertDao.getAllAlerts()
}