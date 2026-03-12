package com.nadin.climewatch.presentation.features.favourite

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.data.features.weather.model.FavoriteLocation
import com.nadin.climewatch.presentation.utils.states.ResultState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
class FavViewModel(private val repository: WeatherRepository) : ViewModel() {

    val favorites: StateFlow<ResultState<List<FavoriteLocation>>> = repository.getAllFavLocations()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ResultState.Loading
        )

    private val _favEvents = MutableSharedFlow<FavEvent>()
    val favEvent = _favEvents.asSharedFlow()

    fun onFabClicked() {
        viewModelScope.launch {
            _favEvents.emit(FavEvent.GoToMap)
        }
    }

    fun onFavCardClicked(location: FavoriteLocation) {
        viewModelScope.launch {
            _favEvents.emit(FavEvent.GoToHome(location.lat, location.lon))
        }
    }

    fun deleteLocation(location: FavoriteLocation) {
        viewModelScope.launch {
            repository.deleteLocationFromFav(location)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
class FavViewModelFactory(
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}