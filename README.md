# Clime Watch

![Clime Watch Logo](app/src/main/res/drawable/logo.png)

Clime Watch is an Android weather application that allows users to track severe weather alerts and forecasts for their current location or other locations they choose.

## Features

*   **Current Weather:** Get the current weather conditions for any location.
*   **Weather Forecast:** Get a 7-day weather forecast.
*   **Weather Alerts:** Get notifications for severe weather alerts.
*   **Favorite Locations:** Save your favorite locations for quick access to their weather forecasts.
*   **Location Picker:** Choose a location from a map to get its weather forecast.
*   **Customizable Units:** Choose between metric, imperial, and standard units.
*   **Multi-language Support:** The app is available in English and Arabic.

## Technologies Used

*   **UI:** Jetpack Compose
*   **Architecture:** MVVM (Model-View-ViewModel)
*   **Asynchronous Programming:** Kotlin Coroutines and Flow
*   **Networking:** Retrofit and Gson
*   **Database:** Room
*   **Local Storage:** DataStore for settings
*   **Navigation:** Jetpack Navigation Compose
*   **Location:** Google Play Services
*   **Maps:** Google Maps Compose
*   **Background Processing:** WorkManager
*   **Services:** Foreground Service for notifications and alarms

## Setup

To run the app, you need to provide API keys for the OpenWeatherMap and Google Maps APIs.

1.  Create a file named `local.properties` in the root directory of the project.
2.  Add the following lines to the file, replacing `YOUR_API_KEY` with your actual API keys:

    ```
    WEATHER_API_KEY="YOUR_OPENWEATHERMAP_API_KEY"
    GOOGLE_MAPS_API_KEY="YOUR_GOOGLE_MAPS_API_KEY"
    ```

## Build and Run

### Build Commands

*   `./gradlew assembleDebug`: Compile the debug version of the app.
*   `./gradlew installDebug`: Install the debug version of the app on a connected device or emulator.
*   `./gradlew clean`: Clean the build directory.

### Running Tests

*   `./gradlew testDebugUnitTest`: Run unit tests.
*   `./gradlew connectedDebugAndroidTest`: Run instrumented tests on a connected device or emulator.