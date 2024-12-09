package com.cs407.wellnest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyScreen(navController: NavController) {
    // State to track if feedback is submitted
    var isFeedbackSubmitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isFeedbackSubmitted) "Thank You" else "Feedback Survey") },
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
        if (isFeedbackSubmitted) {
            // Show thank-you message
            ThankYouContent(modifier = Modifier.padding(innerPadding))
        } else {
            // Show feedback form
            SurveyContent(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                onSubmit = { isFeedbackSubmitted = true } // Mark feedback as submitted
            )
        }
    }
}

@Composable
fun SurveyContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    onSubmit: () -> Unit
) {
    val question1Answer = remember { mutableStateOf("") }
    val question2Answer = remember { mutableStateOf("") }
    val question3Answer = remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Title
        Text(
            text = "We Value Your Feedback",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Please take a moment to share your thoughts about WellNest. Your feedback helps us improve.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Question 1
        Text(
            text = "1. How do you feel about the app?",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = question1Answer.value,
            onValueChange = { question1Answer.value = it },
            label = { Text("Your answer") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Question 2
        Text(
            text = "2. What features do you like the most?",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = question2Answer.value,
            onValueChange = { question2Answer.value = it },
            label = { Text("Your answer") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Question 3
        Text(
            text = "3. Any suggestions for improvement?",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = question3Answer.value,
            onValueChange = { question3Answer.value = it },
            label = { Text("Your answer") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = {
                // Call the onSubmit callback when the form is submitted
                onSubmit()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Feedback")
        }
    }
}

@Composable
fun ThankYouContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Thank you for your feedback!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "We appreciate your input and will use it to improve WellNest.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}