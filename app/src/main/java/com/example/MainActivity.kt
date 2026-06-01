package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: MainViewModel = viewModel()
                MainScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                    .testTag("global_bottom_navigation")
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                // Home (Social Feed)
                NavigationBarItem(
                    selected = currentRoute == "feed",
                    onClick = {
                        navController.navigate("feed") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == "feed") Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Social Feed"
                        )
                    },
                    label = { Text("Feed", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_feed_item")
                )

                // Explore (discover social posts)
                NavigationBarItem(
                    selected = currentRoute == "explore",
                    onClick = {
                        navController.navigate("explore") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == "explore") Icons.Filled.Explore else Icons.Outlined.Explore,
                            contentDescription = "Explore"
                        )
                    },
                    label = { Text("Explore", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_explore_item")
                )

                // Dedicated Portfolio Showcase Space
                NavigationBarItem(
                    selected = currentRoute == "portfolio",
                    onClick = {
                        navController.navigate("portfolio") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == "portfolio") Icons.Filled.BusinessCenter else Icons.Outlined.BusinessCenter,
                            contentDescription = "Portfolio Space"
                        )
                    },
                    label = { Text("Portfolios", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_portfolio_item")
                )

                // Create Post
                NavigationBarItem(
                    selected = currentRoute == "create",
                    onClick = {
                        navController.navigate("create") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == "create") Icons.Filled.AddCircle else Icons.Outlined.AddCircle,
                            contentDescription = "Publish"
                        )
                    },
                    label = { Text("Publish", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_publish_item")
                )

                // Profiles
                val selfUserId = "niko_dev"
                val isProfileActive = currentRoute?.startsWith("profile") == true
                NavigationBarItem(
                    selected = isProfileActive,
                    onClick = {
                        navController.navigate("profile/$selfUserId") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isProfileActive) Icons.Filled.Person else Icons.Outlined.Person,
                            contentDescription = "Profile Space"
                        )
                    },
                    label = { Text("Profile", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_profile_item")
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "feed",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Screen 1: Feed (Instagram style home)
            composable("feed") {
                FeedScreen(
                    viewModel = viewModel,
                    onNavigateToProfile = { userId ->
                        navController.navigate("profile/$userId") {
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Screen 2: Explore Grid
            composable("explore") {
                ExploreScreen(
                    viewModel = viewModel,
                    onNavigateToProfile = { userId ->
                        navController.navigate("profile/$userId") {
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Screen 3: Dedicated professional portfolio Space
            composable("portfolio") {
                PortfolioDashboardScreen(
                    viewModel = viewModel,
                    onNavigateToProfile = { userId ->
                        navController.navigate("profile/$userId") {
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Screen 4: Combined Upload Hub
            composable("create") {
                CreateScreen(
                    viewModel = viewModel,
                    onNavigateToFeed = {
                        navController.navigate("feed") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigateToPortfolio = {
                        navController.navigate("portfolio") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // Screen 5: Dynamic Profile view
            composable(
                route = "profile/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: "niko_dev"
                ProfileScreen(
                    userIdOnLaunch = userId,
                    viewModel = viewModel
                )
            }
        }
    }
}
