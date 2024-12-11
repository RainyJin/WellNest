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
fun PrivacyScreen(navController: NavController, isDarkMode: MutableState<Boolean>) {
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
                    title = { Text("Privacy Notice", color = contentColor) },
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
            PrivacyContent(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                isDarkMode = isDarkMode
            )
        }
    }
}

@Composable
fun PrivacyContent(modifier: Modifier = Modifier, navController: NavController, isDarkMode: MutableState<Boolean>) {
    val textColor = if (isDarkMode.value) Color.White else Color.Black

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDarkMode.value) Color.Black else Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Title
        Text(
            text = "Your Privacy Matters",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Intro Section
        Text(
            text = "At WellNest, we value your privacy and are committed to protecting your personal information.",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Data Collection Section
        Text(
            text = "What We Collect",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "- Data you provide, such as profile information and preferences.\n" +
                    "- Activity data, like completed tasks and reminders.\n" +
                    "- Device data, such as app usage metrics.",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Data Usage Section
        Text(
            text = "How We Use Your Data",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "- To personalize your experience, such as reminder settings.\n" +
                    "- To improve app performance and features.\n" +
                    "- To notify you of important updates or reminders.",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Permissions Section
        Text(
            text = "Permissions We Require",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "- Notifications: To send you reminders.\n" +
                    "- Storage: To save your profile information.\n" +
                    "- Alarm Access: To schedule reminders.",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // User Responsibility Section
        Text(
            text = "Your Responsibilities",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "- Keep your device secure to protect your data.\n" +
                    "- Ensure permissions are enabled for reminders and features.\n" +
                    "- Regularly review your profile information and settings.",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Support Section
        Text(
            text = "Need Assistance?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "If you have questions about our privacy practices, please contact us via the Feedback section in the app.",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Button to Feedback Screen
        Button(
            onClick = { navController.navigate("survey") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Contact Us")
        }
    }
}
