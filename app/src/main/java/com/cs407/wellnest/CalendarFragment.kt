package com.cs407.wellnest

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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.cs407.wellnest.ui.theme.Red40
import com.cs407.wellnest.ui.theme.Salmon
import com.cs407.wellnest.data.CountdownItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

@Composable
fun CalendarScreen(navController: NavController) {
    val context = LocalContext.current


    // Get today's date
    val today = LocalDate.now()
    val formattedDate = today.format(DateTimeFormatter.ofPattern("EEEE MMM d, yyyy"))

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
                calendarView.setCalendarDays(list)
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
                CountdownCard(daysLeft = ChronoUnit.DAYS.between(today, item.targetDate),
                    description = item.description,
                    cardColor = Color.White,
                    navController = navController,
                    onDelete = { countdownItems.remove(item) }
                )
            }
        }
    }

}

val countdownItems = mutableListOf(
    CountdownItem(LocalDate.of(2024, 12, 1), "CS 407 Midterm 📅"),
    CountdownItem(LocalDate.of(2024, 12, 12), "CS 407 Final 📅"),
    CountdownItem(LocalDate.of(2025, 2, 9), "First Anniversary 💖"),
    CountdownItem(LocalDate.of(2025, 5, 12), "Summer Break 🎉")
)

val list = countdownItems.map { item ->
    val calendar = Calendar.getInstance().apply {
        set(item.targetDate.year, item.targetDate.monthValue - 1, item.targetDate.dayOfMonth)
    }
    CalendarDay(calendar).apply {
        backgroundResource = R.drawable.ic_target_date
    }
}

@Composable
fun CountdownCard(daysLeft: Long,
                  description: String,
                  cardColor: Color,
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
        colors = CardDefaults.cardColors(containerColor = cardColor),
        onClick = { navController.navigate("nav_add_item/$description/${LocalDate.now().plusDays(daysLeft)}") }
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
