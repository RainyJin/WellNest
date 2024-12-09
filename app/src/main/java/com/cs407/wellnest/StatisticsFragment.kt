package com.cs407.wellnest


import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs407.wellnest.utils.GoogleFitHelper


@Composable
fun StatisticsScreen(isDarkMode: MutableState<Boolean>) {
    val context = LocalContext.current
    val googleFitHelper = remember { GoogleFitHelper(context) }


    // Health states for the "Day" tab
    var daySteps by remember { mutableStateOf(0) }
    var dayCalories by remember { mutableStateOf(0.0) }
    var dayDistance by remember { mutableStateOf(0.0) }
    var dayGoal by remember { mutableStateOf(10000) }


    var selectedTabIndex by remember { mutableStateOf(0) } // Track selected tab index


    // Check and fetch Google Fit data
    LaunchedEffect(Unit) {
        if (googleFitHelper.hasGoogleFitPermissions()) {
            fetchGoogleFitData(googleFitHelper) { steps, distance, calories ->
                daySteps = steps
                dayDistance = distance
                dayCalories = calories
            }
        } else {
            val activity = context as? ComponentActivity
            activity?.let {
                googleFitHelper.requestGoogleFitPermissions(it, requestCode = 1001)
            }
        }
    }


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


        // Display content for the Day tab (Google Fit data is fetched for the day only)
        if (selectedTabIndex == 0) {
            TimeRangeContent(
                steps = daySteps,
                calories = dayCalories.toInt(),
                sleep = 8, // Example static value
                runDistance = dayDistance.toInt(),
                gymHours = 1, // Example static value
                foodLog = "",
                goal = dayGoal,
                onStepsChange = { daySteps = it },
                onCaloriesChange = { dayCalories = it.toDouble() },
                onSleepChange = { /* No-op */ },
                onRunDistanceChange = { dayDistance = it.toDouble() },
                onGymHoursChange = { /* No-op */ },
                onFoodLogChange = { /* No-op */ },
                onGoalChange = { dayGoal = it }
            )
        } else {
            // Static content for Week and Month tabs
            TimeRangeContent(
                steps = 0, // Example static value
                calories = 0,
                sleep = 0,
                runDistance = 0,
                gymHours = 0,
                foodLog = "",
                goal = 10000, // Example static goal
                onStepsChange = { /* No-op */ },
                onCaloriesChange = { /* No-op */ },
                onSleepChange = { /* No-op */ },
                onRunDistanceChange = { /* No-op */ },
                onGymHoursChange = { /* No-op */ },
                onFoodLogChange = { /* No-op */ },
                onGoalChange = { /* No-op */ }
            )
        }
    }
}


private fun fetchGoogleFitData(
    googleFitHelper: GoogleFitHelper,
    onStepsFetched: (steps: Int, distance: Double, calories: Double) -> Unit
) {
    googleFitHelper.fetchDailyData(
        onSuccess = { steps, distance, calories ->
            onStepsFetched(steps, distance, calories)
        },
        onFailure = { exception ->
            Log.e("StatisticsScreen", "Failed to fetch Google Fit data: ${exception.message}")
        }
    )
}


@Composable
fun TimeRangeContent(
    steps: Int,
    calories: Int,
    sleep: Int,
    runDistance: Int,
    gymHours: Int,
    foodLog: String,
    goal: Int,
    onStepsChange: (Int) -> Unit,
    onCaloriesChange: (Int) -> Unit,
    onSleepChange: (Int) -> Unit,
    onRunDistanceChange: (Int) -> Unit,
    onGymHoursChange: (Int) -> Unit,
    onFoodLogChange: (String) -> Unit,
    onGoalChange: (Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Circular Progress with Clickable Modifier
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    showDialog = true
                    dialogType = "Goal"
                },
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = steps / goal.toFloat(),
                color = Color.Green,
                strokeWidth = 8.dp,
                modifier = Modifier.size(100.dp)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(goal.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("${(steps / goal.toFloat() * 100).toInt()}%", fontSize = 14.sp, color = Color.Gray)
            }
        }


        Spacer(modifier = Modifier.height(24.dp))


        // Step, Distance, Calories, Sleep Sections
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox("Steps", "$steps steps", Color(0xFFADD8E6)) {
                showDialog = true
                dialogType = "Steps"
            }
            InfoBox("Distance", "$runDistance m", Color(0xFFADD8E6))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox("Calories", "$calories kcal", Color(0xFFFFE4B5)) {
                showDialog = true
                dialogType = "Calories"
            }
            InfoBox("Sleep", "$sleep hours", Color(0xFFADD8E6)) {
                showDialog = true
                dialogType = "Sleep"
            }
        }
    }
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

