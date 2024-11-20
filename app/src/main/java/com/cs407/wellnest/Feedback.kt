package com.cs407.wellnest

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SurveyScreen(navController: NavController) {
    val question1Answer = remember { mutableStateOf("") }
    val question2Answer = remember { mutableStateOf("") }
    val question3Answer = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Feedback Survey", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Question 1
        Text(text = "1. How do you feel about the app?")
        OutlinedTextField(
            value = question1Answer.value,
            onValueChange = { question1Answer.value = it },
            label = { Text("Your answer") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Question 2
        Text(text = "2. What features do you like the most?")
        OutlinedTextField(
            value = question2Answer.value,
            onValueChange = { question2Answer.value = it },
            label = { Text("Your answer") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Question 3
        Text(text = "3. Any suggestions for improvement?")
        OutlinedTextField(
            value = question3Answer.value,
            onValueChange = { question3Answer.value = it },
            label = { Text("Your answer") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Handle form submission, e.g., save to database or send to server
                navController.popBackStack() // Navigate back after submission
            }
        ) {
            Text("Submit Feedback")
        }
    }
}
