package com.cs407.wellnest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(navController: NavController, isDarkMode: MutableState<Boolean>) {
    val backgroundColor = if (isDarkMode.value) Color.Black else Color.White
    val contentColor = if (isDarkMode.value) Color.White else Color.Black

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Help & Support", color = contentColor) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = contentColor
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = backgroundColor,
                        titleContentColor = contentColor
                    )
                )
            }
        ) { innerPadding ->
            HelpContent(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                isDarkMode = isDarkMode
            )
        }
    }
}

@Composable
fun HelpContent(modifier: Modifier = Modifier, navController: NavController, isDarkMode: MutableState<Boolean>) {
    val textColor = if (isDarkMode.value) Color.White else Color.Black
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDarkMode.value) Color.Black else Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // App Overview Section
        Text(
            text = "Welcome to WellNest",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "WellNest helps you manage your daily routines such as taking medication, meditating, and keeping track of your schedule.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Features Section
        Text(
            text = "Features",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "- Set reminders for medication, bedtime, and wakeup.\n" +
                    "- Track your daily habits and routines.\n" +
                    "- View helpful statistics to stay on track.\n" +
                    "- Meditate with guided sessions.\n" +
                    "- Customize your profile.",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Instructions Section
        Text(
            text = "How to Use",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "1. Navigate through the bottom tabs to explore the app's features.\n" +
                    "2. Set reminders by going to the To-Do section.\n" +
                    "3. View your calendar for an overview of your schedule.\n" +
                    "4. Customize your profile in the Profile tab.\n" +
                    "5. Check stats to track your progress.",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Support Section
        Text(
            text = "Need Help?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "If you have any questions or need assistance, you can reach us via the Feedback section in the Profile tab.",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Button to Feedback Screen
        Button(
            onClick = { navController.navigate("survey") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Feedback")
        }
    }
}


