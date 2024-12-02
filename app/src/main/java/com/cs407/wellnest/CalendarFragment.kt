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
import androidx.compose.runtime.mutableStateListOf
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
//import com.cs407.wellnest.data.CountdownItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@Composable
fun CalendarScreen(navController: NavController, viewModel: CountdownViewModel = viewModel()) {
    val countdownItems = remember { mutableStateListOf<CountdownEntity>() }
    LaunchedEffect(Unit) {
        countdownItems.addAll(viewModel.getCountdownItems())
    }


    // Get today's date
    val today = LocalDate.now()
    val formattedDate = today.format(DateTimeFormatter.ofPattern("EEEE MMM d, yyyy"))

    // Get a list of CalendarDays from the countdown items
    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    val calendarDays = countdownItems.map { item ->
        Log.d("target Date:", "${item.targetDate}")
        val parsedDate = LocalDate.parse(item.targetDate, formatter)
        val calendar = Calendar.getInstance().apply {
            set(parsedDate.year, parsedDate.monthValue - 1, parsedDate.dayOfMonth)
        }
        CalendarDay(calendar).apply {
            backgroundResource = R.drawable.ic_target_date
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        AndroidView(
            // calendar
            factory = { context ->
                val view =
                    LayoutInflater.from(context).inflate(R.layout.fragment_calendar, null, false)

                val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
                calendarView.setHeaderColor(R.color.salmon)
                calendarView.setCalendarDays(calendarDays)
                // calendarView.setSelectionBackground(R.color.purple_200)

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
                style = MaterialTheme.typography.titleMedium,
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
                    tint = Salmon
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
                    navController = navController,
                    onDelete = {
                        viewModel.viewModelScope.launch {
                            viewModel.deleteCountdown(item)
                            countdownItems.remove(item)
                        }
                    }
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
                  navController: NavController,
                  onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onDelete() }
                )
            },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            navController.navigate("nav_add_item/${Uri.encode(description)}/" +
                    "${Uri.encode(date)}/${Uri.encode(repeat)}") }
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
                    .background(Red40, shape = RoundedCornerShape(24.dp)),
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
                    style = MaterialTheme.typography.bodyMedium
                )

                // description
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        }
    }
}
