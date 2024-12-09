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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(navController: NavController, isDarkMode: MutableState<Boolean>) {
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
                    title = {
                        Text(
                            text = "About Us",
                            color = contentColor // Ensure the title color adapts to dark mode
                        )
                    },
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
            AboutUsContent(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                isDarkMode = isDarkMode
            )
        }
    }
}


@Composable
fun AboutUsContent(modifier: Modifier = Modifier, navController: NavController, isDarkMode: MutableState<Boolean>) {
    val textColor = if (isDarkMode.value) Color.White else Color.Black
    val backgroundColor = if (isDarkMode.value) Color.Black else Color.White

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor) // Ensure background is set
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Title
        Text(
            text = "About Us",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Description
        Text(
            text = "WellNest is the final project for CS407 at UW-Madison. This app was created by a team of dedicated senior students:",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Team Members Section
        Text(
            text = "Our Team",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "- Rainy Jin\n- Charlotte Wan\n- Jordan Zhou\n- Zikun Zhou",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Purpose Section
        Text(
            text = "Purpose of the App",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "WellNest is designed to help users manage their daily routines effectively, including setting reminders, tracking habits, and maintaining a balanced lifestyle.",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Back Button
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
