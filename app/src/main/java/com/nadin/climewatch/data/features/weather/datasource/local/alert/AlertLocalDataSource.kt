package com.nadin.climewatch.data.features.weather.datasource.local.alert

import android.content.Context
import com.nadin.climewatch.data.db.AppDatabase
import com.nadin.climewatch.data.features.weather.entites.Alert

class AlertLocalDataSource(context: Context): IAlertLocalDataSource {
    private val alertDao: AlertDao = AppDatabase.getInstance(context).alertDao()

    override suspend fun insertAlert(alert: Alert): Int {
        return alertDao.insertAlert(alert).toInt()
    }

    override suspend fun deleteAlert(alert: Alert) {
        alertDao.deleteAlert(alert)
    }

    override fun getAllAlerts() = alertDao.getAllAlerts()
}