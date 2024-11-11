package com.cs407.wellnest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cs407.wellnest.ui.theme.WellNestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WellNestTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
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
            composable("nav_todo") { TodoScreen(navController) } // Pass navController to TodoScreen
            composable("nav_calendar") { CalendarScreen() }
            composable("nav_stat") { StatisticsScreen() }
            composable("nav_profile") { ProfileScreen(navController) }
            composable("nav_about_us") { AboutUsScreen(navController) }

            // Route for editing a specific to-do item
            composable("edit_todo/{itemId}") { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId")
                EditTodoScreen(itemName = itemId ?: "", navController = navController)
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
