package com.nadin.climewatch.data.features

import com.google.android.gms.maps.model.LatLng
import com.nadin.climewatch.data.features.weather.WeatherRepository
import com.nadin.climewatch.data.features.weather.datasource.local.alert.IAlertLocalDataSource
import com.nadin.climewatch.data.features.weather.datasource.local.favlocations.IFavLocationLocalDataSource
import com.nadin.climewatch.data.features.weather.datasource.remote.IWeatherRemoteDatasource
import com.nadin.climewatch.data.features.weather.dto.CityDto
import com.nadin.climewatch.data.features.weather.dto.CloudsDto
import com.nadin.climewatch.data.features.weather.dto.MainDto
import com.nadin.climewatch.data.features.weather.dto.SysDto
import com.nadin.climewatch.data.features.weather.dto.WeatherDto
import com.nadin.climewatch.data.features.weather.dto.WeatherResponseDto
import com.nadin.climewatch.data.features.weather.dto.WindDto
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WeatherRepoTest {
    private lateinit var fakeFavLocationLocalDataSource: IFavLocationLocalDataSource
    private lateinit var fakeAlertLocalDataSource: IAlertLocalDataSource
    private lateinit var fakeRemoteDatasource: IWeatherRemoteDatasource
    private lateinit var repository: WeatherRepository

    private val fakeWeatherDto = WeatherResponseDto(
        weather = listOf(
            WeatherDto(
                id = 800,
                main = "Clear",
                description = "clear sky",
                icon = "01d"
            )
        ),
        mainInfo = MainDto(
            temp = 25.0,
            feels_like = 24.0,
            temp_min = 22.0,
            temp_max = 27.0,
            humidity = 60,
            pressure = 1013
        ),
        wind = WindDto(speed = 5.0, deg = 180),
        clouds = CloudsDto(all = 10),
        dateTime = 1_700_000_000L,
        sys = SysDto(country = "EG", sunrise = 1_699_990_000L, sunset = 1_700_030_000L),
        timezone = 7200,
        id = 99L,
        city = "Cairo"
    )

    @Before
    fun setUp() {
        fakeFavLocationLocalDataSource = mockk(relaxed = true)
        fakeAlertLocalDataSource = mockk(relaxed = true)
        fakeRemoteDatasource = mockk()
        repository =
            WeatherRepository(
                remoteDataSource = fakeRemoteDatasource,
                favLocalDataSource = fakeFavLocationLocalDataSource,
                alertLocalDataSource = fakeAlertLocalDataSource
            )
    }

    @Test
    fun getWeatherByGeoCode_dto_returnSuccess() =
        runTest {
            // Given
            coEvery {
                fakeRemoteDatasource.getWeatherByGeoCode(
                    any(),
                    any()
                )
            } returns fakeWeatherDto

            // When
            val result = repository.getWeatherByGeoCode(lat = 30.0, lon = 31.0)

            // Then
            assertTrue(result.isSuccess)
            val weather = result.getOrNull()!!
            assertEquals("Cairo", weather.city)
            assertEquals("EG", weather.country)
            assertEquals(25.0, weather.temperature, 0.001)
            assertEquals(60, weather.humidity)
            assertEquals(5.0, weather.windSpeed, 0.001)
            assertEquals("clear sky", weather.description)
            assertEquals("01d", weather.icon)
        }

    @Test
    fun getWeatherByCity_dto_returnSuccess() =
        runTest {
            // Given
            coEvery {
                fakeRemoteDatasource.getWeatherByCity(
                    any()
                )
            } returns fakeWeatherDto

            // When
            val result = repository.getWeatherByCity(city = "Cairo")

            // Then
            assertTrue(result.isSuccess)
            val weather = result.getOrNull()!!
            assertEquals("Cairo", weather.city)
            assertEquals("EG", weather.country)
            assertEquals(25.0, weather.temperature, 0.001)
            assertEquals(60, weather.humidity)
            assertEquals(5.0, weather.windSpeed, 0.001)
            assertEquals("clear sky", weather.description)
            assertEquals("01d", weather.icon)
        }

    @Test
    fun getCityByGeoCode_dto_returnSuccess() = runTest {
        val geoCode = LatLng(30.0, 31.0)
        val fakeCityDto = CityDto(
            name = "Cairo",
            lat = 30.0,
            lon = 31.0,
            country = "EG"
        )
        coEvery { fakeRemoteDatasource.getCityByGeoCode(geoCode) } returns fakeCityDto

        // When
        val result = repository.getCityByGeoCode(geoCode)

        // Then
        assertTrue(result.isSuccess)
        val city = result.getOrNull()!!
        assertEquals("Cairo", city.name)
        assertEquals("EG",    city.country)
        assertEquals(30.0,    city.lat, 0.001)
        assertEquals(31.0,    city.lon, 0.001)
    }
}

