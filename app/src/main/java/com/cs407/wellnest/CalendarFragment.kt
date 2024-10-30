package com.cs407.wellnest

import android.view.LayoutInflater
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.applandeo.materialcalendarview.CalendarView
import com.cs407.wellnest.ui.theme.Red40

@Composable
fun CalendarScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->
                val view =
                    LayoutInflater.from(context).inflate(R.layout.fragment_calendar, null, false)

                val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
                calendarView.setHeaderColor(R.color.salmon)
                // calendarView.setSelectionBackground(R.color.purple_200)

                view
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp) // Define a fixed height for the calendar
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Selected Date and Event List Header
        Text(
            text = "Thursday Feb 6, 2024",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 110.dp)
        )

        // Countdown List
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            items(countdownItems) { item ->
                CountdownCard(daysLeft = item.daysLeft,
                    description = item.description,
                    cardColor = Color.White)
            }
        }
    }
}

// Sample data structure for countdown items
data class CountdownItem(val daysLeft: Int, val description: String)

val countdownItems = listOf(
    CountdownItem(7, "CS 407 Midterm 📅"),
    CountdownItem(65, "First Anniversary 💖"),
    CountdownItem(101, "CS 407 Final 📅"),
    CountdownItem(109, "Summer Break 🎉")
)

@Composable
fun CountdownCard(daysLeft: Int, description: String, cardColor: Color) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
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

                // Description
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        }
    }
}
