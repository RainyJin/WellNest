package com.cs407.wellnest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
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
