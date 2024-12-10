package com.cs407.wellnest

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.cs407.wellnest.ui.theme.Red40
import com.cs407.wellnest.ui.theme.Salmon
import com.cs407.wellnest.data.CountdownEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@Composable
fun CalendarScreen(navController: NavController, isDarkMode: MutableState<Boolean>, viewModel: CalendarViewModel = viewModel()) {
    val countdownState = viewModel.getCountdownItemsFlow().collectAsState(initial = emptyList())
    val countdownItems = remember { mutableStateListOf<CountdownEntity>() }

    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    LaunchedEffect(Unit) {
        viewModel.deleteExpiredCountdown()
        countdownItems.clear()
        countdownItems.addAll(viewModel.getCountdownItems())
        Log.d("CalendarScreen", "Countdown State Value: ${countdownState.value}")
    }

    // Colors based on dark mode
    val backgroundColor = if (isDarkMode.value) Color.Black else Color.White
    val textColor = if (isDarkMode.value) Color.White else Color.Black
    val cardBackgroundColor = if (isDarkMode.value) Color.DarkGray else Color.White
    val buttonTint = if (isDarkMode.value) Color.LightGray else Salmon
    val headerColorRes = if (isDarkMode.value) android.R.color.darker_gray else R.color.salmon
    val calendarTextColorRes = if (isDarkMode.value) android.R.color.white else android.R.color.black


    // Get today's date
    val today = LocalDate.now()
    val formattedDate = today.format(DateTimeFormatter.ofPattern("EEEE MMM d, yyyy"))

    // Convert countdown dates to Calendar objects
    val targetDates = remember(countdownState.value) {
        countdownState.value.map { countdown ->
            val parsedDate = LocalDate.parse(countdown.targetDate, formatter)
            Calendar.getInstance().apply {
                set(parsedDate.year, parsedDate.monthValue - 1, parsedDate.dayOfMonth)
            }
        }
    }
    Log.d("CalendarScreen", "Target Dates: $targetDates")

    val calendarDays = targetDates.map {
        CalendarDay(it).apply {
            backgroundResource = R.drawable.ic_target_date
        }
    }
    Log.d("CalendarDays", calendarDays.toString())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        AndroidView(
            // calendar
            factory = { context ->
                val view =
                    LayoutInflater.from(context).inflate(R.layout.fragment_calendar, null, false)

                val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
                calendarView.setHeaderColor(headerColorRes)
                calendarView.setCalendarDays(calendarDays)



                view
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            // today's date
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.titleMedium.copy(color = textColor),
                modifier = Modifier.align(Alignment.Center)
            )

            // add button
            IconButton(
                onClick = { navController.navigate("nav_add_item") },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Item",
                    tint = buttonTint
                )
            }
        }

        // countdown item list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            items(countdownItems) { item ->
                CountdownCard(
                    id = item.id,
                    date = item.targetDate,
                    daysLeft = ChronoUnit.DAYS.between(today, LocalDate.parse(item.targetDate, formatter)),
                    description = item.description,
                    repeat = item.repeatOption,
                    endDate = item.endDate,
                    navController = navController,
                    onDelete = { deleteAll ->
                        if (deleteAll) {
                            viewModel.viewModelScope.launch {
                                // Delete all occurrences of the repeating event
                                val allOccurrences = countdownItems.filter { it.id == item.id }
                                allOccurrences.forEach { countdown ->
                                    viewModel.deleteCountdown(countdown.id)
                                }
                                countdownItems.removeAll(allOccurrences)
                            }
                        } else {
                            // Delete only the current occurrence
                            viewModel.viewModelScope.launch {
                                viewModel.deleteCountdown(item.id)
                                countdownItems.remove(item)
                            }
                        }
                    },
                    isDarkMode = isDarkMode
                )
            }
        }
    }
}

@Composable
fun CountdownCard(id: String,
                  date: String,
                  daysLeft: Long,
                  description: String,
                  repeat: String,
                  endDate: String?,
                  navController: NavController,
                  isDarkMode: MutableState<Boolean>,
                  onDelete: (deleteAll: Boolean) -> Unit) {
    val showDialog = remember { mutableStateOf(false) }

    // Colors based on dark mode
    val cardBackgroundColor = if (isDarkMode.value) Color.DarkGray else Color.White
    val textColor = if (isDarkMode.value) Color.White else Color.Black
    val daysIndicatorColor = if (isDarkMode.value) Red40 else Salmon

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Confirm Deletion",color = textColor) },
            text = { Text(text = "Do you want to delete just this event or all occurrences? " +
                    "You can also click outside the box to cancel.",
                color = textColor) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(true)
                        showDialog.value = false
                    }
                ) {
                    Text("Delete All", color = textColor)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDelete(false)
                        showDialog.value = false
                    }
                ) {
                    Text("Delete Only This", color = textColor)
                }
            }
        )
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { showDialog.value = true }
                )
            },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        onClick = {
            navController.navigate("nav_add_item/${Uri.encode(id)}/${Uri.encode(description)}" +
                    "/${Uri.encode(date)}/${Uri.encode(repeat)}/${Uri.encode(endDate)}") }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Days left indicator
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(daysIndicatorColor, shape = RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$daysLeft",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Days To ",
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor)
                )

                // description
                Text(
                    text = description,
                    style =  MaterialTheme.typography.bodyLarge.copy(color = textColor)
                )
            }
        }
    }
}