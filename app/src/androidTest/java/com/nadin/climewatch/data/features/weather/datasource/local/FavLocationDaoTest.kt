package com.nadin.climewatch.data.features.weather.datasource.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.nadin.climewatch.data.db.AppDatabase
import com.nadin.climewatch.data.features.weather.datasource.local.favlocations.FavLocationDao
import com.nadin.climewatch.data.features.weather.entites.FavoriteLocation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.not

@SmallTest
@RunWith(AndroidJUnit4::class)
class FavLocationDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: FavLocationDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()

        dao = database.favLocationsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertLocation_success() = runTest {
        val location =
            FavoriteLocation(id = 1, city = "Cairo", country = "EG", lat = 30.0, lon = 31.0)

        dao.insertLocation(location)

        val result = dao.getAllLocations().first()
        assertThat(result.size, `is`(1))
        assertThat(result.first(), `is`(location))
    }

    @Test
    fun deleteLocation_existingLocation_removedSuccessfully() = runTest {
        val location =
            FavoriteLocation(id = 1, city = "Cairo", country = "EG", lat = 30.0, lon = 31.0)
        dao.insertLocation(location)

        dao.deleteLocation(location)

        val result = dao.getAllLocations().first()
        assertThat(result, not(hasItem(location)))
    }

    @Test
    fun getAllLocations_returnsAllInsertedLocations() = runTest {
        val location1 = FavoriteLocation(id = 1, city = "Cairo", country = "EG", lat = 30.0, lon = 31.0)
        val location2 = FavoriteLocation(id = 2, city = "Alex", country = "EG", lat = 31.2, lon = 29.9)
        dao.insertLocation(location1)
        dao.insertLocation(location2)

        val result = dao.getAllLocations().first()
        assertThat(result.size, `is`(2))
        assertThat(result, hasItem(location1))
        assertThat(result, hasItem(location2))
    }
}