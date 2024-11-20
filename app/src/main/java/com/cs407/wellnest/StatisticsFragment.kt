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

    // Separate states for each tab
    var daySteps by remember { mutableStateOf(0) }
    var dayCalories by remember { mutableStateOf(185) }
    var daySleep by remember { mutableStateOf(8) }
    var dayRunDistance by remember { mutableStateOf(5) }
    var dayGymHours by remember { mutableStateOf(1) }
    var dayFoodLog by remember { mutableStateOf("") }
    var dayGoal by remember { mutableStateOf(10000) }

    var weekSteps by remember { mutableStateOf(0) }
    var weekCalories by remember { mutableStateOf(0) }
    var weekSleep by remember { mutableStateOf(0) }
    var weekRunDistance by remember { mutableStateOf(0) }
    var weekGymHours by remember { mutableStateOf(0) }
    var weekFoodLog by remember { mutableStateOf("") }
    var weekGoal by remember { mutableStateOf(70000) }

    var monthSteps by remember { mutableStateOf(0) }
    var monthCalories by remember { mutableStateOf(0) }
    var monthSleep by remember { mutableStateOf(0) }
    var monthRunDistance by remember { mutableStateOf(0) }
    var monthGymHours by remember { mutableStateOf(0) }
    var monthFoodLog by remember { mutableStateOf("") }
    var monthGoal by remember { mutableStateOf(300000) }

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
            0 -> TimeRangeContent(
                steps = daySteps,
                calories = dayCalories,
                sleep = daySleep,
                runDistance = dayRunDistance,
                gymHours = dayGymHours,
                foodLog = dayFoodLog,
                goal = dayGoal,
                onStepsChange = { daySteps = it },
                onCaloriesChange = { dayCalories = it },
                onSleepChange = { daySleep = it },
                onRunDistanceChange = { dayRunDistance = it },
                onGymHoursChange = { dayGymHours = it },
                onFoodLogChange = { dayFoodLog = it },
                onGoalChange = { dayGoal = it }
            )
            1 -> TimeRangeContent(
                steps = weekSteps,
                calories = weekCalories,
                sleep = weekSleep,
                runDistance = weekRunDistance,
                gymHours = weekGymHours,
                foodLog = weekFoodLog,
                goal = weekGoal,
                onStepsChange = { weekSteps = it },
                onCaloriesChange = { weekCalories = it },
                onSleepChange = { weekSleep = it },
                onRunDistanceChange = { weekRunDistance = it },
                onGymHoursChange = { weekGymHours = it },
                onFoodLogChange = { weekFoodLog = it },
                onGoalChange = { weekGoal = it }
            )
            2 -> TimeRangeContent(
                steps = monthSteps,
                calories = monthCalories,
                sleep = monthSleep,
                runDistance = monthRunDistance,
                gymHours = monthGymHours,
                foodLog = monthFoodLog,
                goal = monthGoal,
                onStepsChange = { monthSteps = it },
                onCaloriesChange = { monthCalories = it },
                onSleepChange = { monthSleep = it },
                onRunDistanceChange = { monthRunDistance = it },
                onGymHoursChange = { monthGymHours = it },
                onFoodLogChange = { monthFoodLog = it },
                onGoalChange = { monthGoal = it }
            )
        }
    }
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
            InfoBox("Distance", "4.5 km", Color(0xFFADD8E6))
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

        Spacer(modifier = Modifier.height(16.dp))

        // Workouts Section
        Text("Workouts", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        WorkoutItem("Run", "$runDistance km") {
            showDialog = true
            dialogType = "Run"
        }
        WorkoutItem("Gym", "$gymHours hours") {
            showDialog = true
            dialogType = "Gym"
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Food Section
        Text("Food", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        InfoBox("Food Log", foodLog, Color(0xFFFFE4B5)) {
            showDialog = true
            dialogType = "Food"
        }
    }

    // Dialog for Setting Values
    if (showDialog) {
        SetValueDialog(
            currentValue = when (dialogType) {
                "Steps" -> steps.toString()
                "Calories" -> calories.toString()
                "Sleep" -> sleep.toString()
                "Run" -> runDistance.toString()
                "Gym" -> gymHours.toString()
                "Food" -> foodLog
                else -> goal.toString()
            },
            label = dialogType,
            onDismiss = { showDialog = false },
            onConfirm = { newValue ->
                when (dialogType) {
                    "Steps" -> onStepsChange(newValue.toInt())
                    "Calories" -> onCaloriesChange(newValue.toInt())
                    "Sleep" -> onSleepChange(newValue.toInt())
                    "Run" -> onRunDistanceChange(newValue.toInt())
                    "Gym" -> onGymHoursChange(newValue.toInt())
                    "Food" -> onFoodLogChange(newValue)
                    "Goal" -> onGoalChange(newValue.toInt())
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
            Button(onClick = { onConfirm(text) }) {
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
