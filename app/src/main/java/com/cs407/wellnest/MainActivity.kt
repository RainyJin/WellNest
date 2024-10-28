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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
            composable("nav_todo") { TodoScreen() } // Reference to the To-Do screen
            composable("nav_calendar") { CalendarScreen() } // Reference to the Calendar screen
            composable("nav_stat") { StatisticsScreen() } // Reference to the Statistics screen
            composable("nav_profile") { ProfileScreen() } // Reference to the Profile screen
        }

    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val navItems = listOf(
            BottomNavItem("nav_todo", "To-Do", R.drawable.ic_todo),
            BottomNavItem("nav_calendar", "Calendar", R.drawable.ic_calendar),
            BottomNavItem("nav_stat", "Stats", R.drawable.ic_stat),
            BottomNavItem("nav_profile", "Profile", R.drawable.ic_profile)
        )

        navItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(item.icon), contentDescription = item.title) },
                label = { Text(item.title) },
                selected = false, // You can manage selected state based on navController
                onClick = {
                    navController.navigate(item.route) {
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
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
        MainScreen() // Preview the main screen with bottom navigation
    }
}
