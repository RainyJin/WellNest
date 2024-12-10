package com.cs407.wellnest

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.lazy.items


@Composable
fun StatisticsScreen(isDarkMode: MutableState<Boolean>) {
    val context = LocalContext.current
    val googleFitHelper = remember { GoogleFitHelper(context) }

    // Define dark mode and light mode colors
    val backgroundColor = if (isDarkMode.value) Color.Black else Color.White
    val textColor = if (isDarkMode.value) Color.White else Color.Black
    val progressColor = if (isDarkMode.value) Color.Green else Color.Blue
    val tabSelectedColor = if (isDarkMode.value) Color.Gray else Color.LightGray
    val cardBackgroundColor = if (isDarkMode.value) Color.DarkGray else Color.LightGray


    // Health states for Day, Week, and Month tabs
    var daySteps by remember { mutableStateOf(0) }
    var weekSteps by remember { mutableStateOf(0) }
    var monthSteps by remember { mutableStateOf(0) }
    var stepGoal by remember { mutableStateOf(10000) } // Default step goal

    // Other fields for Day, Week, and Month
    var dayCalories by remember { mutableStateOf(0.0) }
    var weekCalories by remember { mutableStateOf(0.0) }
    var monthCalories by remember { mutableStateOf(0.0) }
    var dayDistance by remember { mutableStateOf(0.0) }
    var weekDistance by remember { mutableStateOf(0.0) }
    var monthDistance by remember { mutableStateOf(0.0) }
    var sleepHours by remember { mutableStateOf(8) }
    var gymHours by remember { mutableStateOf(0f) } // Default value set to 0
    var runningJogging by remember { mutableStateOf(0f) } // Default value set to 0
    var foodConsumed by remember { mutableStateOf("2,000 kcal") }

    var selectedTabIndex by remember { mutableStateOf(0) } // Track selected tab index
    var showGoalDialog by remember { mutableStateOf(false) } // Control GoalDialog visibility

    // Check and fetch Google Fit data
    LaunchedEffect(Unit) {
        if (googleFitHelper.hasGoogleFitPermissions()) {
            // Fetch Daily Data
            googleFitHelper.fetchDailyData(
                onSuccess = { steps, distance, calories ->
                    daySteps = steps
                    dayDistance = distance
                    dayCalories = calories
                },
                onFailure = { exception -> Log.e("StatisticsScreen", "Failed to fetch daily data: ${exception.message}") }
            )

            // Fetch Weekly Data
            googleFitHelper.fetchWeekData(
                onSuccess = { steps ->
                    weekSteps = steps
                    weekDistance = steps * 0.8
                    weekCalories = steps * 0.05
                },
                onFailure = { exception -> Log.e("StatisticsScreen", "Failed to fetch weekly data: ${exception.message}") }
            )

            // Fetch Monthly Data
            googleFitHelper.fetchMonthData(
                onSuccess = { steps ->
                    monthSteps = steps
                    monthDistance = steps * 0.8
                    monthCalories = steps * 0.05
                },
                onFailure = { exception -> Log.e("StatisticsScreen", "Failed to fetch monthly data: ${exception.message}") }
            )
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
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Text(
            text = "Health Summary",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Tabs for Day, Week, Month
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = backgroundColor,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Day", "Week", "Month").forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, color = textColor) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display content based on the selected tab
        when (selectedTabIndex) {
            0 -> TimeRangeContent(
                steps = daySteps,
                calories = dayCalories.toInt(),
                sleep = sleepHours,
                runDistance = dayDistance.toInt(),
                gymHours = gymHours,
                runningJogging = runningJogging,
                foodConsumed = foodConsumed,
                goal = stepGoal,
                onGoalClick = { showGoalDialog = true },
                onGymHoursChange = { newGymHours -> gymHours = newGymHours },
                onRunningJoggingChange = { newRunningJogging -> runningJogging = newRunningJogging },
                isDarkMode = isDarkMode
            )
            1 -> TimeRangeContent(
                steps = 49564,
                calories = 22450,
                sleep = 42,
                runDistance = 48095,
                gymHours = gymHours * 7,
                runningJogging = runningJogging * 7,
                foodConsumed = "14,000 kcal",
                goal = stepGoal * 7,
                onGoalClick = { showGoalDialog = true },
                onGymHoursChange = { newGymHours -> gymHours = newGymHours },
                onRunningJoggingChange = { newRunningJogging -> runningJogging = newRunningJogging },
                isDarkMode = isDarkMode

            )
            2 -> TimeRangeContent(
                steps = 49564,
                calories = 22450,
                sleep = 42,
                runDistance = 48095,
                gymHours = gymHours * 30,
                runningJogging = runningJogging * 30,
                foodConsumed = "60,000 kcal",
                goal = stepGoal * 30,
                onGoalClick = { showGoalDialog = true },
                onGymHoursChange = { newGymHours -> gymHours = newGymHours },
                onRunningJoggingChange = { newRunningJogging -> runningJogging = newRunningJogging },
                isDarkMode = isDarkMode
            )
        }
    }

    // Show GoalDialog when triggered
    if (showGoalDialog) {
        GoalDialog(
            currentGoal = stepGoal,
            onGoalChange = { newGoal -> stepGoal = newGoal },
            onDismiss = { showGoalDialog = false },
            isDarkMode = isDarkMode

        )
    }
}

@Composable
fun TimeRangeContent(
    steps: Int,
    calories: Int,
    sleep: Int,
    runDistance: Int,
    gymHours: Float,
    runningJogging: Float,
    foodConsumed: String,
    goal: Int,
    onGoalClick: () -> Unit,
    onGymHoursChange: (Float) -> Unit,
    onRunningJoggingChange: (Float) -> Unit,
    isDarkMode: MutableState<Boolean>
) {

    val backgroundColor = if (isDarkMode.value) Color.Black else Color.White
    val textColor = if (isDarkMode.value) Color.White else Color.Black
    val cardColor = if (isDarkMode.value) Color.DarkGray else Color.LightGray


    var showGymDialog by remember { mutableStateOf(false) }
    var showRunningJoggingDialog by remember { mutableStateOf(false) }
    var showFoodDialog by remember { mutableStateOf(false) }
    var showSleepDialog by remember { mutableStateOf(false) }
    var foodLog by remember { mutableStateOf(listOf<String>()) }
    var sleepLog by remember { mutableStateOf(listOf<String>()) }
    var selectedValue by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Circular Progress
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardColor, RoundedCornerShape(8.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { onGoalClick() },
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = steps / goal.toFloat(),
                        color = if (isDarkMode.value) Color.Green else Color.Blue,
                        strokeWidth = 12.dp,
                        modifier = Modifier.size(200.dp)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(goal.toString(), fontSize = 24.sp, color = textColor, fontWeight = FontWeight.Bold)
                        Text("${(steps / goal.toFloat() * 100).toInt()}%", fontSize = 16.sp, color = textColor)
                    }
                }
            }
        }

        // Info Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoBox("Steps", "$steps steps", cardColor, textColor)
                InfoBox("Calories", "$calories kcal", cardColor, textColor)
            }
        }

        // Distance and Sleep
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoBox("Distance", "$runDistance m", backgroundColor,  textColor)
                Box(
                    modifier = Modifier
                        .background(Color(0xFFD8BFD8), RoundedCornerShape(8.dp))
                        .clickable { showSleepDialog = true }
                        .padding(16.dp)
                        .width(160.dp)
                ) {
                    Column {
                        Text("Sleep", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (sleepLog.isNotEmpty()) sleepLog.firstOrNull() ?: "$sleep hours" else "$sleep hours",
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        // Gym Hours
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFA07A), RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Gym Hours", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(
                            text = "$gymHours hours",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.clickable {
                                selectedValue = gymHours.toString()
                                showGymDialog = true
                            }
                        )
                    }
                    Row {
                        Button(onClick = { onGymHoursChange(gymHours + 0.5f) }) {
                            Text("+0.5")
                        }
                        Button(onClick = { onGymHoursChange(gymHours + 1f) }) {
                            Text("+1")
                        }
                    }
                }
            }
        }

        // Running/Jogging
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFADD8E6), RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Running/Jogging", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(
                            text = "$runningJogging hours",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.clickable {
                                selectedValue = runningJogging.toString()
                                showRunningJoggingDialog = true
                            }
                        )
                    }
                    Row {
                        Button(onClick = { onRunningJoggingChange(runningJogging + 0.5f) }) {
                            Text("+0.5")
                        }
                        Button(onClick = { onRunningJoggingChange(runningJogging + 1f) }) {
                            Text("+1")
                        }
                    }
                }
            }
        }

        // Food Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE0FFFF), RoundedCornerShape(8.dp))
                    .clickable { showFoodDialog = true }
                    .padding(16.dp)
            ) {
                Column {
                    Text("Food", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (foodLog.isNotEmpty()) foodLog.firstOrNull() ?: "No recent food" else "No recent food",
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }

    // Dialog for Gym Hours
    if (showGymDialog) {
        InputDialog(
            title = "Edit Gym Hours",
            currentValue = selectedValue,
            onValueChange = { newGymHours ->
                onGymHoursChange(newGymHours.toFloatOrNull() ?: gymHours)
                showGymDialog = false
            },
            onDismiss = { showGymDialog = false }
        )
    }

    // Dialog for Running/Jogging
    if (showRunningJoggingDialog) {
        InputDialog(
            title = "Edit Running/Jogging Hours",
            currentValue = selectedValue,
            onValueChange = { newRunningJogging ->
                onRunningJoggingChange(newRunningJogging.toFloatOrNull() ?: runningJogging)
                showRunningJoggingDialog = false
            },
            onDismiss = { showRunningJoggingDialog = false }
        )
    }

    // Sleep Dialog
    if (showSleepDialog) {
        SleepLogDialog(
            sleepLog = sleepLog,
            onAddSleep = { newSleep ->
                sleepLog = listOf(newSleep) + sleepLog
            },
            onDismiss = { showSleepDialog = false }
        )
    }

    // Food Dialog
    if (showFoodDialog) {
        FoodLogDialog(
            foodLog = foodLog,
            onAddFood = { newFood ->
                foodLog = listOf(newFood) + foodLog
            },
            onDismiss = { showFoodDialog = false }
        )
    }
}




@Composable
fun FoodLogDialog(
    foodLog: List<String>,
    onAddFood: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newFood by remember { mutableStateOf("") } // State for new food entry

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Food Log") },
        text = {
            Column {
                // Food Log List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp), // Limit the height of the food log list
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(foodLog) { food ->
                        Text(food, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Input for new food
                TextField(
                    value = newFood,
                    onValueChange = { newFood = it },
                    placeholder = { Text("Enter new food") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newFood.isNotBlank()) {
                        onAddFood(newFood)
                        newFood = "" // Reset input
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}


@Composable
fun InputDialog(
    title: String,
    currentValue: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var inputValue by remember { mutableStateOf(currentValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                TextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    placeholder = { Text("Enter value") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onValueChange(inputValue) }) {
                Text("Save")
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
fun GoalDialog(
    currentGoal: Int,
    onGoalChange: (Int) -> Unit,
    onDismiss: () -> Unit,
    isDarkMode: MutableState<Boolean>
) {
    val backgroundColor = if (isDarkMode.value) Color.DarkGray else Color.White
    val textColor = if (isDarkMode.value) Color.White else Color.Black


// Use `remember` for managing the input goal state
    var inputGoal by remember { mutableStateOf(currentGoal.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Your Goal" , color = textColor) },
        text = {
            Column {
                Text("Enter your step goal:",color = textColor )
                TextField(
                    value = inputGoal,
                    onValueChange = { inputGoal = it },
                    placeholder = { Text("Step Goal") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = textColor,
                        errorContainerColor = Color.Transparent,
                        errorIndicatorColor = Color.Red
                    )
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val newGoal = inputGoal.toIntOrNull() ?: currentGoal
                onGoalChange(newGoal)
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel", color = textColor)
            }
        },
        containerColor = backgroundColor
    )
}

@Composable
fun InfoBox(
    label: String,
    value: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier.width(160.dp)
) {
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(label, fontSize = 14.sp, color = textColor, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 18.sp, color = textColor)
        }
    }
}

@Composable
fun SleepLogDialog(
    sleepLog: List<String>,
    onAddSleep: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newSleep by remember { mutableStateOf("") } // State for new sleep entry

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sleep Log") },
        text = {
            Column {
                // Sleep Log List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp), // Limit the height of the sleep log list
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sleepLog) { sleep ->
                        Text(sleep, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Input for new sleep
                TextField(
                    value = newSleep,
                    onValueChange = { newSleep = it },
                    placeholder = { Text("Enter sleep hours") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newSleep.isNotBlank()) {
                        onAddSleep(newSleep)
                        newSleep = "" // Reset input
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}