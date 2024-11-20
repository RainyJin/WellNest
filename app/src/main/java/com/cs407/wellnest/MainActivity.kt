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
            composable("nav_todo") { TodoScreen(navController) } // Reference to the To-Do screen
            composable("nav_calendar") { CalendarScreen(navController) } // Reference to the Calendar screen
            composable("nav_stat") { StatisticsScreen() } // Reference to the Statistics screen
            composable("nav_profile") { ProfileScreen(navController) } // Reference to the Profile screen
            composable("nav_about_us") { AboutUsScreen(navController) }
            composable("nav_add_item") { AddItemFragment(navController) }
            composable("survey") { SurveyScreen(navController) }
            composable("meditation") { MeditationScreen(navController)}
            composable("nav_add_item/{eventName}/{eventDate}") { AddItemFragment(navController) }

            // Editing a todo
            composable(
                "edit_todo/{course}/{selectedTabIndex}/{backgroundColor}",
                arguments = listOf(
                    navArgument("course") { type = NavType.StringType },
                    navArgument("selectedTabIndex") { type = NavType.IntType },
                    navArgument("backgroundColor") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val course = backStackEntry.arguments?.getString("course") ?: ""
                val selectedTabIndex = backStackEntry.arguments?.getInt("selectedTabIndex") ?: 0
                val backgroundColorInt = backStackEntry.arguments?.getString("backgroundColor")?.toInt() ?: 0xFF5BBAE9
                val backgroundColor = Color(backgroundColorInt.toLong())


                EditTodoScreen(
                    itemName = course,
                    navController,
                    backgroundColor = backgroundColor,
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
