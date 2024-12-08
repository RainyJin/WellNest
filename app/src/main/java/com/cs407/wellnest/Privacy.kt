package com.cs407.wellnest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Notice") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        PrivacyContent(
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )
    }
}

@Composable
fun PrivacyContent(modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Title
        Text(
            text = "Your Privacy Matters",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Intro Section
        Text(
            text = "At WellNest, we value your privacy and are committed to protecting your personal information.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Data Collection Section
        Text(
            text = "What We Collect",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "- Data you provide, such as profile information and preferences.\n" +
                    "- Activity data, like completed tasks and reminders.\n" +
                    "- Device data, such as app usage metrics.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Data Usage Section
        Text(
            text = "How We Use Your Data",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "- To personalize your experience, such as reminder settings.\n" +
                    "- To improve app performance and features.\n" +
                    "- To notify you of important updates or reminders.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Permissions Section
        Text(
            text = "Permissions We Require",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "- Notifications: To send you reminders.\n" +
                    "- Storage: To save your profile information.\n" +
                    "- Alarm Access: To schedule reminders.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // User Responsibility Section
        Text(
            text = "Your Responsibilities",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "- Keep your device secure to protect your data.\n" +
                    "- Ensure permissions are enabled for reminders and features.\n" +
                    "- Regularly review your profile information and settings.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Support Section
        Text(
            text = "Need Assistance?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "If you have questions about our privacy practices, please contact us via the Feedback section in the app.",
            fontSize = 16.sp,
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