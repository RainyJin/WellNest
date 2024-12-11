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
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.cs407.wellnest.SharedPrefsHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter



@Composable
fun StatisticsScreen(isDarkMode: MutableState<Boolean>) {
    val context = LocalContext.current
    val googleFitHelper = remember { GoogleFitHelper(context) }

    // Define dark mode and light mode colors
    val backgroundColor = if (isDarkMode.value) Color.Black else Color.White
    val textColor = if (isDarkMode.value) Color.White else Color.Black
    val progressColor = if (isDarkMode.value) Color.Green else Color.Blue
    val cardBackgroundColor = if (isDarkMode.value) Color.DarkGray else Color.LightGray

    // Independent goals for day, week, month
    var dayGoal by remember { mutableStateOf(SharedPrefsHelper.getInt(context, "dayGoal", 10000)) }
    var weekGoal by remember { mutableStateOf(SharedPrefsHelper.getInt(context, "weekGoal", 70000)) }
    var monthGoal by remember { mutableStateOf(SharedPrefsHelper.getInt(context, "monthGoal", 300000)) }

    // Steps data
    var daySteps by remember { mutableStateOf(0) }
    var weekSteps by remember { mutableStateOf(0) }
    var monthSteps by remember { mutableStateOf(0) }

    // Other fields for Day, Week, and Month
    var dayCalories by remember { mutableStateOf(0.0) }
    var weekCalories by remember { mutableStateOf(0.0) }
    var monthCalories by remember { mutableStateOf(0.0) }
    var dayDistance by remember { mutableStateOf(0.0) }
    var weekDistance by remember { mutableStateOf(0.0) }
    var monthDistance by remember { mutableStateOf(0.0) }
    var sleepLog by remember { mutableStateOf(loadLog(context, "sleepLog")) }
    var foodLog by remember { mutableStateOf(loadLog(context, "foodLog")) }

    // Gym hours and running/jogging
    var dailyGymHours by remember { mutableStateOf(0f) }
    var dailyRunningHours by remember { mutableStateOf(0f) }
    var weeklyGymHours by remember { mutableStateOf(loadAggregate(context, "weeklyGymHours")) }
    var monthlyGymHours by remember { mutableStateOf(loadAggregate(context, "monthlyGymHours")) }
    var weeklyRunningHours by remember { mutableStateOf(loadAggregate(context, "weeklyRunningHours")) }
    var monthlyRunningHours by remember { mutableStateOf(loadAggregate(context, "monthlyRunningHours")) }

    var selectedTabIndex by remember { mutableStateOf(0) }
    var showGoalDialog by remember { mutableStateOf(false) }
    var showSleepDialog by remember { mutableStateOf(false) }

    // Reset daily values at the end of the day
    LaunchedEffect(Unit) {
        resetDailyValues(context)
    }

    // Fetch Google Fit data
    LaunchedEffect(Unit) {
        if (googleFitHelper.hasGoogleFitPermissions()) {
            googleFitHelper.fetchDailyData(
                onSuccess = { steps, distance, calories ->
                    daySteps = steps
                    dayDistance = distance
                    dayCalories = calories
                },
                onFailure = { exception -> Log.e("StatisticsScreen", "Failed to fetch daily data: ${exception.message}") }
            )

            googleFitHelper.fetchWeeklyData(
                onSuccess = { steps, distance, calories ->
                    weekSteps = steps
                    weekDistance = distance
                    weekCalories = calories
                },
                onFailure = { exception -> Log.e("StatisticsScreen", "Failed to fetch weekly data: ${exception.message}") }
            )

            googleFitHelper.fetchMonthlyData(
                onSuccess = { steps, distance, calories ->
                    monthSteps = steps
                    monthDistance = distance
                    monthCalories = calories
                },
                onFailure = { exception -> Log.e("StatisticsScreen", "Failed to fetch monthly data: ${exception.message}") }
            )
        }
    }


    // UI Layout
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
        TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.fillMaxWidth()) {
            listOf("Day", "Week", "Month").forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display content based on the selected tab
        when (selectedTabIndex) {
            0 -> TimeRangeContent(
                context = context,
                steps = daySteps,
                calories = dayCalories.toInt(),
                sleep = sleepLog.size,
                runDistance = dayDistance.toInt(),
                gymHours = dailyGymHours,
                runningJogging = dailyRunningHours,
                foodConsumed = if (foodLog.isEmpty()) "No recent food" else foodLog.last(),
                goal = dayGoal,
                onGoalClick = { showGoalDialog = true },
                onGymHoursChange = { newGymHours ->
                    dailyGymHours = newGymHours
                    saveDailyValue(context, "dailyGymHours", dailyGymHours)
                },
                onRunningJoggingChange = { newRunningJogging ->
                    dailyRunningHours = newRunningJogging
                    saveDailyValue(context, "dailyRunningHours", dailyRunningHours)
                    saveAggregate(context, "weeklyRunningHours", weeklyRunningHours + dailyRunningHours)
                    saveAggregate(context, "monthlyRunningHours", monthlyRunningHours + dailyRunningHours)
                },
                isDarkMode = isDarkMode
            )

            1 -> TimeRangeContent(
                context = context,
                steps = weekSteps,
                calories = weekCalories.toInt(),
                sleep = sleepLog.size,
                runDistance = weekDistance.toInt(),
                gymHours = weeklyGymHours,
                runningJogging = weeklyRunningHours,
                foodConsumed = "Weekly total",
                goal = weekGoal,
                onGoalClick = { showGoalDialog = true },
                onGymHoursChange = {},
                onRunningJoggingChange = {},
                isDarkMode = isDarkMode
            )
            2 -> TimeRangeContent(
                context = context,
                steps = monthSteps,
                calories = monthCalories.toInt(),
                sleep = sleepLog.size,
                runDistance = monthDistance.toInt(),
                gymHours = monthlyGymHours,
                runningJogging = monthlyRunningHours,
                foodConsumed = "Monthly total",
                goal = monthGoal,
                onGoalClick = { showGoalDialog = true },
                onGymHoursChange = {},
                onRunningJoggingChange = {},
                isDarkMode = isDarkMode
            )
        }
    }

    // Goal dialog for setting the step goal
    if (showGoalDialog) {
        GoalDialog(
            currentGoal = when (selectedTabIndex) {
                0 -> dayGoal
                1 -> weekGoal
                else -> monthGoal
            },
            onGoalChange = { newGoal ->
                when (selectedTabIndex) {
                    0 -> {
                        dayGoal = newGoal
                        SharedPrefsHelper.saveInt(context, "dayGoal", dayGoal)
                    }
                    1 -> {
                        weekGoal = newGoal
                        SharedPrefsHelper.saveInt(context, "weekGoal", weekGoal)
                    }
                    2 -> {
                        monthGoal = newGoal
                        SharedPrefsHelper.saveInt(context, "monthGoal", monthGoal)
                    }
                }
            },
            onDismiss = { showGoalDialog = false },
            isDarkMode = isDarkMode
        )
    }
}


@Composable
fun TimeRangeContent(
    context: Context,
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
    var foodLog by remember { mutableStateOf(loadLog(context, "foodLog")) }
    var sleepLog by remember { mutableStateOf(loadLog(context, "sleepLog")) }
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
                        Text(goal.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("${(steps / goal.toFloat() * 100).toInt()}%", fontSize = 16.sp, color = Color.Gray)
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
                InfoBox("Steps", "$steps steps", Color(0xFFADD8E6))
                InfoBox("Calories", "$calories kcal", Color(0xFFFFE4B5))
            }
        }


        // Distance and Sleep
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoBox("Distance", "$runDistance m", Color(0xFF98FB98))
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
                addToLog(context, "sleepLog", newSleep) // Save new entry
                sleepLog = loadLog(context, "sleepLog") // Reload the updated log
            },
            onDismiss = { showSleepDialog = false }
        )
    }



    // Food Dialog
    if (showFoodDialog) {
        FoodLogDialog(
            foodLog = foodLog,
            onAddFood = { newFood ->
                addToLog(context, "foodLog", newFood) // Save new entry
                foodLog = loadLog(context, "foodLog") // Reload the updated log
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
    var newFood by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add Food Log") },
        text = {
            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(foodLog) { food ->
                        Text(food, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = newFood,
                    onValueChange = { newFood = it },
                    placeholder = { Text("Enter food item") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newFood.isNotBlank()) {
                        onAddFood(newFood) // Add new entry
                        onDismiss() // Close the dialog
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
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
    var inputGoal by remember { mutableStateOf(currentGoal.toString()) }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Your Goal") },
        text = {
            Column {
                Text("Enter your step goal:")
                TextField(
                    value = inputGoal,
                    onValueChange = { inputGoal = it },
                    placeholder = { Text("Step Goal") }
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
                Text("Cancel")
            }
        }
    )
}


@Composable
fun InfoBox(
    label: String,
    value: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier.width(160.dp)
) {
    Box(
        modifier = modifier
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
fun SleepLogDialog(
    sleepLog: List<String>,
    onAddSleep: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newSleep by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add Sleep Log") },
        text = {
            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sleepLog) { sleep ->
                        Text(sleep, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                        onAddSleep(newSleep) // Add new entry
                        onDismiss() // Close the dialog
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Close")
            }
        }
    )
}





fun loadLog(context: Context, key: String): List<String> {
    val rawLog = SharedPrefsHelper.getString(context, key, "")
    return if (rawLog.isEmpty()) listOf() else rawLog.split(",")
}

fun saveLog(context: Context, key: String, log: List<String>) {
    SharedPrefsHelper.saveString(context, key, log.joinToString(","))
}



fun loadAggregate(context: Context, key: String): Float {
    return SharedPrefsHelper.getFloat(context, key, 0f)
}

fun saveAggregate(context: Context, key: String, value: Float) {
    SharedPrefsHelper.saveFloat(context, key, value)
}

fun saveDailyValue(context: Context, key: String, value: Float) {
    SharedPrefsHelper.saveFloat(context, key, value)
}

fun resetDailyValues(context: Context) {
    val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    val lastResetDate = SharedPrefsHelper.getString(context, "lastResetDate", "")

    if (currentDate != lastResetDate) {
        SharedPrefsHelper.saveString(context, "lastResetDate", currentDate)

        // Add daily values to weekly/monthly aggregates
        val dailyGymHours = SharedPrefsHelper.getFloat(context, "dailyGymHours", 0f)
        val dailyRunningHours = SharedPrefsHelper.getFloat(context, "dailyRunningHours", 0f)

        val weeklyGymHours = SharedPrefsHelper.getFloat(context, "weeklyGymHours", 0f) + dailyGymHours
        val weeklyRunningHours = SharedPrefsHelper.getFloat(context, "weeklyRunningHours", 0f) + dailyRunningHours
        val monthlyGymHours = SharedPrefsHelper.getFloat(context, "monthlyGymHours", 0f) + dailyGymHours
        val monthlyRunningHours = SharedPrefsHelper.getFloat(context, "monthlyRunningHours", 0f) + dailyRunningHours

        SharedPrefsHelper.saveFloat(context, "weeklyGymHours", weeklyGymHours)
        SharedPrefsHelper.saveFloat(context, "weeklyRunningHours", weeklyRunningHours)
        SharedPrefsHelper.saveFloat(context, "monthlyGymHours", monthlyGymHours)
        SharedPrefsHelper.saveFloat(context, "monthlyRunningHours", monthlyRunningHours)

        // Reset daily values
        SharedPrefsHelper.saveFloat(context, "dailyGymHours", 0f)
        SharedPrefsHelper.saveFloat(context, "dailyRunningHours", 0f)
    }
}


fun addToLog(context: Context, key: String, newEntry: String) {
    val rawLog = SharedPrefsHelper.getString(context, key, "")
    val log = if (rawLog.isEmpty()) mutableListOf() else rawLog.split(",").toMutableList()
    log.add(0, newEntry) // Add new entry to the beginning of the list
    saveLog(context, key, log)
}


fun filterOldEntries(log: List<String>, daysToKeep: Int): List<String> {
    val cutoff = System.currentTimeMillis() - daysToKeep * 24 * 60 * 60 * 1000 // 30 days in milliseconds
    return log.filter {
        val parts = it.split("@")
        val timestamp = parts.getOrNull(1)?.toLongOrNull() ?: 0L
        timestamp >= cutoff
    }
}


