import com.android.tools.r8.internal.im
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization") version "2.3.10"
}

val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.nadin.climewatch"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.nadin.climewatch"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    defaultConfig {
        buildConfigField(
            "String",
            "WEATHER_API_KEY",
            "\"${localProperties["WEATHER_API_KEY"]}\""
        )
        buildConfigField(
            "String",
            "GOOGLE_MAPS_API_KEY",
            "\"${localProperties["GOOGLE_MAPS_API_KEY"]}\""
        )
        manifestPlaceholders["GOOGLE_MAPS_API_KEY"] = localProperties["GOOGLE_MAPS_API_KEY"] ?: ""
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //Coroutines
    implementation(libs.kotlinx.coroutines.core)

    //ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose.android)

    //Icons
    implementation(libs.androidx.compose.material.icons.extended)

    //Work Manager
    implementation(libs.androidx.work.runtime.ktx)

    //Location
    implementation(libs.play.services.location)

    //Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    //Animated Bottom Nav Bar
    implementation(libs.bottombar)

    //Google Maps
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)

    //svg support
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("io.coil-kt:coil-svg:2.6.0")

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Dependencies for local unit tests
    testImplementation ("junit:junit:4.13.2")

    // AndroidX Test - Instrumented testing
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")

    // Kotlin
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.23")
    implementation ("androidx.fragment:fragment-ktx:1.7.1")

    testImplementation ("org.hamcrest:hamcrest:3.0")
    testImplementation ("org.hamcrest:hamcrest-library:3.0")

    testImplementation ("androidx.test:core-ktx:1.7.0")
    testImplementation ("org.robolectric:robolectric:4.16.1")
    testImplementation ("androidx.test.ext:junit-ktx:1.3.0")

    testImplementation ("androidx.arch.core:core-testing:2.2.0")

    testImplementation ("io.mockk:mockk-android:1.13.17")
    testImplementation ("io.mockk:mockk-agent:1.13.17")

    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
}