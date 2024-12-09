package com.cs407.wellnest

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cs407.wellnest.ui.theme.WellNestTheme

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "Please allow notifications for reminders.", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppDatabase.getDatabase(this)
        enableEdgeToEdge()
        setContent {
            val isDarkMode = remember { mutableStateOf(false) } // Centralized state for dark mode
            WellNestTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "start_screen"
                ) {
                    composable("start_screen") {
                        StartScreen(navController)
                    }
                    composable("main_screen") {
                        MainScreen()
                    }
                }
            }
        }

        // Request notification permission
        requestNotificationPermission()

        // Initialize NotificationHelper
        NotificationHelper(this).createNotificationChannel()

        // Request location permission
        requestLocationPermission()
    }
}


@Composable
fun MainScreen() {
    val isDarkMode = remember { mutableStateOf(false) }
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "nav_todo",
            Modifier.padding(innerPadding)
        ) {
            composable("nav_todo") { TodoScreen(navController, isDarkMode) } // Reference to the To-Do screen
            composable("nav_calendar") { CalendarScreen(navController, isDarkMode) } // Reference to the Calendar screen
            composable("nav_stat") { StatisticsScreen(isDarkMode) } // Reference to the Statistics screen
            composable("nav_profile") { ProfileScreen(navController,isDarkMode) } // Reference to the Profile screen
            composable("nav_about_us") { AboutUsScreen(navController, isDarkMode) }
            composable("nav_add_item") { AddItemFragment(navController) }
            composable("survey") { SurveyScreen(navController, isDarkMode) }

            composable("meditation") { MeditationScreen(navController)}
            composable("pet_profile") { PetProfileScreen(navController) }
            composable("help") { HelpScreen(navController, isDarkMode) }
            composable("nav_add_item/{eventId}/{eventDesc}/{eventDate}/{eventRepeat}/{eventEndDate}") { AddItemFragment(navController) }
            composable("privacy") { PrivacyScreen(navController, isDarkMode) }

            // Editing a todo
            composable(
                "edit_todo/{todoId}/{selectedTabIndex}/{backgroundColor}",
                arguments = listOf(
                    navArgument("todoId") { type = NavType.StringType },
                    navArgument("selectedTabIndex") { type = NavType.IntType },
                    navArgument("backgroundColor") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val todoId = backStackEntry.arguments?.getString("todoId")
                val selectedTabIndex = backStackEntry.arguments?.getInt("selectedTabIndex") ?: 0

                val backgroundColorInt = backStackEntry.arguments?.getInt("backgroundColor") ?: 0xFF5BBAE9.toInt()
                val backgroundColor = Color(backgroundColorInt)

                EditTodoScreen(
                    itemId = todoId,
                    navController = navController,
                    backgroundColor = backgroundColor,
                    isDarkMode = isDarkMode
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navItems = listOf(
        BottomNavItem("nav_todo", "To-Do", R.drawable.ic_todo),
        BottomNavItem("nav_calendar", "Calendar", R.drawable.ic_calendar),
        BottomNavItem("nav_stat", "Stats", R.drawable.ic_stat),
        BottomNavItem("nav_profile", "Profile", R.drawable.ic_profile)
    )

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination?.route

    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(item.icon), contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentDestination == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Avoid multiple copies of the same destination on the back stack
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(val route: String, val title: String, val icon: Int)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WellNestTheme {
        MainScreen()
    }
}