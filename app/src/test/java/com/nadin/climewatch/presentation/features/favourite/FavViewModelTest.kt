package com.nadin.climewatch.presentation.features.favourite

import com.nadin.climewatch.data.features.weather.IWeatherRepo
import com.nadin.climewatch.data.features.weather.entites.FavoriteLocation
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class FavViewModelTest{
    lateinit var repo : IWeatherRepo
    lateinit var favViewModel: FavViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val location = FavoriteLocation(
        id = 1,
        lat = 30.0,
        lon = 31.2,
        city = "Cairo",
        country = "EG"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repo = mockk(relaxed = true)
        favViewModel = FavViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onFabClicked_emitsGoToMapEvent() = runTest {
        val result = async { favViewModel.favEvent.first() }
        advanceUntilIdle()
        favViewModel.onFabClicked()
        advanceUntilIdle()
        assertEquals(FavEvent.GoToMap, result.await())
    }

    @Test
    fun onFavCardClicked_emitsGoToHomeEvent() = runTest {
        val result = async { favViewModel.favEvent.first() }
        advanceUntilIdle()
        favViewModel.onFavCardClicked(location)
        advanceUntilIdle()
        val event = result.await() as FavEvent.GoToHome
        assertEquals(30.0, event.lat, 0.0)
        assertEquals(31.2, event.lon, 0.0)
    }

    @Test
    fun deleteLocation_callsRepoDelete() = runTest {
        favViewModel.deleteLocation(location)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repo.deleteLocationFromFav(location) }
    }
}