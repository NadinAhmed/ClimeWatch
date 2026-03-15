package com.nadin.climewatch.data.features.weather.datasource.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.nadin.climewatch.data.db.AppDatabase
import com.nadin.climewatch.data.features.weather.datasource.local.favlocations.FavLocationDao
import com.nadin.climewatch.data.features.weather.datasource.local.favlocations.FavLocationLocalDataSource
import com.nadin.climewatch.data.features.weather.entites.FavoriteLocation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class FavLocationLocalDataSourceTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: FavLocationDao
    private lateinit var localDataSource: FavLocationLocalDataSource

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.favLocationsDao()
        localDataSource = FavLocationLocalDataSource(ApplicationProvider.getApplicationContext())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertLocation_callsDaoInsert() = runTest {
        //Given
        val location =
            FavoriteLocation(id = 1, city = "Cairo", country = "EG", lat = 30.0, lon = 31.0)

        //when
        localDataSource.insertLocation(location)

        //Then
        val result = localDataSource.getAllLocations().first()
        assertThat(result.size, `is`(1))
        assertThat(result[0], `is`(location))
    }

    @Test
    fun deleteLocation_callsDaoDelete() = runTest {
        //Given
        val location =
            FavoriteLocation(id = 1, city = "Cairo", country = "EG", lat = 30.0, lon = 31.0)
        localDataSource.insertLocation(location)

        // When
        localDataSource.deleteLocation(location)

        // Then — DB should be empty
        val result = localDataSource.getAllLocations().first()
        assertThat(result.isEmpty(), `is`(true))

    }

    @Test
    fun getAllLocations_returnsDaoFlow() = runTest {
        // Given — insert real data into the DB
        val location =
            FavoriteLocation(id = 1, city = "Cairo", country = "EG", lat = 30.0, lon = 31.0)
        localDataSource.insertLocation(location)

        // When
        val result = localDataSource.getAllLocations().first()

        // Then
        assertThat(result, `is`(listOf(location)))
    }
}