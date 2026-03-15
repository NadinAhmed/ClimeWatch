package com.nadin.climewatch.presentation.features.maps

import com.google.android.gms.maps.model.LatLng
import com.nadin.climewatch.data.features.weather.IWeatherRepo
import com.nadin.climewatch.data.local.ISettingsDataStore
import com.nadin.climewatch.data.model.City
import com.nadin.climewatch.presentation.utils.states.ResultState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MapViewModelTest {

    private val repository: IWeatherRepo = mockk(relaxed = true)
    private val settingsDataStore: ISettingsDataStore = mockk(relaxed = true)

    private lateinit var viewModel: MapViewModel

    @Before
    fun setUp() {
        viewModel = MapViewModel(
            repository = repository,
            settingsDataStore = settingsDataStore,
            mapSource = MapSource.FromFavorites
        )
    }

    @Test
    fun onSearchQueryChanged_updatesSearchQueryState() = runTest {
        // When
        viewModel.onSearchQueryChanged("Cairo")

        // Then
        assertEquals("Cairo", viewModel.searchQuery.value)
    }


    @Test
    fun onSuggestionSelected_updateAllStatesValue() = runTest {
        // Given
        val city = City(name = "Cairo", country = "EG", lat = 30.0, lon = 31.0)

        // When
        viewModel.onSuggestionSelected(city)

        // Then
        assertEquals(LatLng(30.0, 31.0), viewModel.selectedLocation.value)
        assertTrue(viewModel.isSaveEnabled.value)
        assertEquals("Cairo, EG", viewModel.searchQuery.value)
        assertEquals(
            ResultState.Success(city),
            viewModel.cityState.value
        )
    }

    @Test
    fun onLocationSelected_calls_getCityBtGeoCodeWithCorrectParams() = runTest {
        val geoCode = LatLng(30.0, 31.0)
        coEvery { repository.getCityByGeoCode(geoCode) } returns
                Result.success(
                    City(
                        name = "Cairo",
                        country = "EG",
                        lat = 30.0,
                        lon = 31.0
                    )
                )

        // When
        viewModel.onLocationSelected(geoCode)

        // Then - verify the repo was called
        coVerify(exactly = 1) { repository.getCityByGeoCode(geoCode) }
    }
}

