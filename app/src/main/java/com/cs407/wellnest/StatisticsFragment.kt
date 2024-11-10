package com.cs407.wellnest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatisticsScreen() {
    var selectedTabIndex by remember { mutableStateOf(0) } // Track selected tab index

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Health Summary",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Tabs for Day, Week, Month
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Day", "Week", "Month").forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display content based on selected tab
        when (selectedTabIndex) {
            0 -> TimeRangeContent("Day")
            1 -> TimeRangeContent("Week")
            2 -> TimeRangeContent("Month")
        }
    }
}

@Composable
fun TimeRangeContent(timeRange: String) {
    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf(20000) } // Default goal
    var progress by remember { mutableStateOf(5000) } // Example progress value (can be dynamic)
    var calories by remember { mutableStateOf(185) }
    var sleep by remember { mutableStateOf(8) }
    var runDistance by remember { mutableStateOf(5) }
    var gymHours by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Circular Progress with Clickable Modifier
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
                .clickable { showDialog = true; dialogType = "Goal" }, // Show dialog on click
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = progress / goal.toFloat(),
                color = Color.Green,
                strokeWidth = 8.dp,
                modifier = Modifier.size(100.dp)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(goal.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("${(progress / goal.toFloat() * 100).toInt()}%", fontSize = 14.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Step, Distance, Calories, Sleep Sections with Clickable Modifiers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox("Step", "$progress steps", Color(0xFFADD8E6))
            InfoBox("Distance", "4.5 km", Color(0xFFADD8E6))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox("Calories", "$calories kcal", Color(0xFFFFE4B5), onClick = {
                showDialog = true
                dialogType = "Calories"
            })
            InfoBox("Sleep", "$sleep hours", Color(0xFFADD8E6), onClick = {
                showDialog = true
                dialogType = "Sleep"
            })
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Workouts Section with Clickable Items
        Text("Workouts", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        WorkoutItem("Run", "$runDistance km", onClick = {
            showDialog = true
            dialogType = "Run"
        })
        WorkoutItem("Gym", "$gymHours hours", onClick = {
            showDialog = true
            dialogType = "Gym"
        })

        Spacer(modifier = Modifier.height(16.dp))

        // Food Section
        Text("Food", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFE4B5), RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text("Food log goes here", color = Color.DarkGray)
        }
    }

    // Dialog for Setting Values
    if (showDialog) {
        SetValueDialog(
            currentValue = when (dialogType) {
                "Calories" -> calories.toString()
                "Sleep" -> sleep.toString()
                "Run" -> runDistance.toString()
                "Gym" -> gymHours.toString()
                else -> goal.toString()
            },
            label = dialogType,
            onDismiss = { showDialog = false },
            onConfirm = { newValue ->
                when (dialogType) {
                    "Calories" -> calories = newValue.toInt()
                    "Sleep" -> sleep = newValue.toInt()
                    "Run" -> runDistance = newValue.toInt()
                    "Gym" -> gymHours = newValue.toInt()
                    "Goal" -> goal = newValue.toInt()
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun SetValueDialog(currentValue: String, label: String, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var text by remember { mutableStateOf(currentValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set $label") },
        text = {
            Column {
                Text("Enter the value for $label:")
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(label) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(text)
            }) {
                Text("Set $label")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun InfoBox(label: String, value: String, backgroundColor: Color, onClick: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
    ) {
        Column {
            Text(label, fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 18.sp, color = Color.Black)
        }
    }
}

@Composable
fun WorkoutItem(name: String, details: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB2DFDB), RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(details, fontSize = 14.sp, color = Color.Gray)
        }
    }
}
