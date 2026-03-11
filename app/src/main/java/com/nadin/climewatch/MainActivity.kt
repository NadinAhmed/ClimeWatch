package com.nadin.climewatch

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorStyle
import com.example.bottombar.model.VisibleItem
import com.nadin.climewatch.presentation.features.SplashScreen
import com.nadin.climewatch.presentation.features.alert.AlertScreen
import com.nadin.climewatch.presentation.features.favourite.FavScreen
import com.nadin.climewatch.presentation.features.home.HomeScreen
import com.nadin.climewatch.presentation.features.settings.SettingsScreen
import com.nadin.climewatch.presentation.ui.theme.ClimeWatchTheme
import com.nadin.climewatch.presentation.ui.theme.PrimaryDarkColor
import com.nadin.climewatch.presentation.utils.BottomNavItem
import com.nadin.climewatch.presentation.utils.NavigationRoutes

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClimeWatchTheme {
                MainScreen()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {

    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem(
            stringResource(R.string.home),
            NavigationRoutes.Home.route,
            Icons.Default.Home
        ),
        BottomNavItem(
            stringResource(R.string.favourite),
            NavigationRoutes.Favourite.route,
            Icons.Default.Favorite
        ),
        BottomNavItem(
            stringResource(R.string.alerts),
            NavigationRoutes.Alerts.route,
            Icons.Default.Timer
        ),
        BottomNavItem(
            stringResource(R.string.settings),
            NavigationRoutes.Settings.route,
            Icons.Default.Settings
        )
    )

    var selectedIndex by remember { mutableIntStateOf(0) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val shouldShowBottomBar = currentRoute != null &&
        currentRoute != NavigationRoutes.Splash.route &&
        currentRoute != NavigationRoutes.MapPicker.route

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            if (shouldShowBottomBar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Color.White.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = PrimaryDarkColor,
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    AnimatedBottomBar(
                        selectedItem = selectedIndex,
                        itemSize = items.size,
                        containerColor = Color.Transparent,
                        indicatorStyle = IndicatorStyle.DOT,
                        indicatorColor = PrimaryDarkColor,
                        containerShape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp)),
                    ) {
                        items.forEachIndexed { index, screen ->
                            BottomBarItem(
                                selected = currentRoute == screen.route,
                                onClick = {
                                    selectedIndex = index

                                    navController.navigate(screen.route) {
                                        popUpTo(NavigationRoutes.Home.route) {
                                            saveState = false
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                imageVector = screen.icon,
                                label = screen.name,
                                visibleItem = VisibleItem.BOTH,
                                iconColor = PrimaryDarkColor,
                                textColor = PrimaryDarkColor,
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.Splash.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {
            composable(NavigationRoutes.Splash.route) {
                SplashScreen(onTimeout = {
                    navController.navigate(
                        NavigationRoutes.Home.route
                    ) {
                        popUpTo(NavigationRoutes.Splash.route) {
                            inclusive = true
                        }
                    }
                })
            }
            composable(NavigationRoutes.Home.route) { HomeScreen() }
            composable(NavigationRoutes.Favourite.route) { FavScreen() }
            composable(NavigationRoutes.Alerts.route) { AlertScreen() }
            composable(NavigationRoutes.Settings.route) { SettingsScreen() }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ClimeWatchTheme {
        MainScreen()
    }
}
