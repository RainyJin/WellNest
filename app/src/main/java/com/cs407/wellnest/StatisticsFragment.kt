package com.cs407.wellnest

import androidx.compose.foundation.background
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
    // This function displays the same content but could be customized based on timeRange
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Circular Progress
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = 0.25f,
                color = Color.Green,
                strokeWidth = 8.dp,
                modifier = Modifier.size(100.dp)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("20000", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("25%", fontSize = 14.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Step, Distance, Calories, Sleep Sections
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox("Step", "5000 steps", Color(0xFFADD8E6))
            InfoBox("Distance", "4.5 km", Color(0xFFADD8E6))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox("Calories", "185 kcal", Color(0xFFFFE4B5))
            InfoBox("Sleep", "8 hours", Color(0xFFADD8E6))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Workouts Section
        Text("Workouts", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        WorkoutItem("Run", "5 km")
        WorkoutItem("Gym", "1 hour")

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
}

@Composable
fun InfoBox(label: String, value: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(label, fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 18.sp, color = Color.Black)
        }
    }
}

@Composable
fun WorkoutItem(name: String, details: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB2DFDB), RoundedCornerShape(8.dp))
            .padding(16.dp)
            .padding(vertical = 4.dp)
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
