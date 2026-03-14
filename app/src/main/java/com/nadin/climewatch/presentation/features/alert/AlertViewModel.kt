package com.nadin.climewatch.presentation.features.alert

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.data.features.weather.entites.Alert
import com.nadin.climewatch.presentation.service.worker.AlertWorker
import com.nadin.climewatch.presentation.utils.states.ResultState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
class AlertViewModel(
    private val repository: WeatherRepository,
    private val workManager: WorkManager,
) : ViewModel() {


    private val _alertsState = MutableStateFlow<ResultState<List<Alert>>>(ResultState.Loading)
    val alertsState = _alertsState.asStateFlow()

    init {
        getAllAlerts()
    }

    private fun getAllAlerts() {
        viewModelScope.launch {
            repository.getAllAlerts().collect { state ->
                _alertsState.value = state
            }
        }
    }

    fun insertAlert(alert: Alert, city: String) {
        viewModelScope.launch {
            repository.insertAlert(alert)
            scheduleAlert(alert, city)
        }
    }

    fun deleteAlert(alert: Alert) {
        viewModelScope.launch {
            repository.deleteAlert(alert)
            cancelAlert(alert.id)
        }
    }

    private fun scheduleAlert(
        alert: Alert,
        city: String
    ) {
        val delay = alert.startTime - System.currentTimeMillis()

        if (delay < 0) return

        val inputData = workDataOf(
            AlertWorker.EXTRA_ALERT_TYPE to alert.alertType.name,
            AlertWorker.EXTRA_CITY to city,
        )

        val workRequest = PeriodicWorkRequestBuilder<AlertWorker>(
            repeatInterval = 15L,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(alert.id.toString())
            .build()

        workManager.enqueueUniquePeriodicWork(
            alert.id.toString(),
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun cancelAlert(alertId: Int) {
        workManager.cancelUniqueWork(alertId.toString())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
class AlertViewModelFactory(
    private val context: Context,
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlertViewModel(
                repository,
                workManager = WorkManager.getInstance(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}