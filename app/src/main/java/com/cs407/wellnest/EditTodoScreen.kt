package com.cs407.wellnest

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun EditTodoScreen(itemName: String, navController: NavController) {
    var goalText by remember { mutableStateOf(TextFieldValue(itemName)) }
    var descriptionText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedRepeatOption by remember { mutableStateOf("Does not repeat") }
    var expandedDropdown by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var isDatePickerVisible by remember { mutableStateOf(false) }

    val repeatOptions = listOf("Does not repeat", "Daily", "Weekly", "Monthly")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5BBAE9)) // Light blue background
            .padding(16.dp)
    ) {
        // Card containing the goal, description, calendar, and repeat options
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            ) {
                // Top bar with title and close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Edit Goal", fontSize = 24.sp, color = Color.Black)
                    IconButton(onClick = { navController.navigate("nav_todo") }) { // Navigate to Todo List screen
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Close",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Editable goal text field
                OutlinedTextField(
                    value = goalText,
                    onValueChange = { goalText = it },
                    label = { Text("Course") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Editable description text field
                OutlinedTextField(
                    value = descriptionText,
                    onValueChange = { descriptionText = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Calendar feature
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar), // Replace with your calendar icon resource
                            contentDescription = "Calendar",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Calendar", fontSize = 16.sp, color = Color.Black)
                    }

                    Text(
                        text = selectedDate,
                        fontSize = 16.sp,
                        color = Color.Blue,
                        modifier = Modifier
                            .clickable {
                                // Show the DatePicker directly when clicked
                                isDatePickerVisible = true
                            }
                            .padding(8.dp)
                    )
                }

                // Custom DatePicker Dialog
                if (isDatePickerVisible) {
                    DatePickerDialog(
                        onDismissRequest = { isDatePickerVisible = false },
                        onDateSelected = { date ->
                            selectedDate = date
                            isDatePickerVisible = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Repeat options dropdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Repeat", fontSize = 16.sp, color = Color.Black)

                    // Clickable text that toggles the dropdown
                    Text(
                        text = selectedRepeatOption,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .clickable { expandedDropdown = !expandedDropdown }
                            .padding(8.dp) // Optional padding for better touch interaction
                    )
                }
                // Box to overlay the dropdown menu
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (expandedDropdown) {
                        DropdownMenu(
                            expanded = expandedDropdown,
                            onDismissRequest = { expandedDropdown = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopStart) // Align to the start of the Box
                        ) {
                            repeatOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        selectedRepeatOption = option
                                        expandedDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Suggestions Section (outside of Card)
        Text("Suggestions", fontSize = 20.sp, color = Color.Black)

        val suggestions = listOf(
            "Send 1 job application",
            "Complete Zybooks Chapter 7",
            "Complete Zybooks Chapter 8",
            "Search for 1 grad school"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            items(suggestions) { suggestion ->
                SuggestionItem(suggestion)
            }
        }

        // Action buttons (outside of Card)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { /* Save action */ },
                modifier = Modifier.weight(1f).padding(4.dp)
            ) {
                Text("Save")
            }
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .weight(1f)
                    .padding(6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancel")
            }
        }
    }
}


@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    // Get the current context inside the composable function
    val context = LocalContext.current

    // State for the selected date
    val selectedDate = remember { mutableStateOf(getCurrentDate()) }

    // Custom date picker dialog
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Date") },
        text = {
            // You can use DatePicker here for selecting the date
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = android.app.DatePickerDialog(
                    context, // Now using the context correctly
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                        selectedDate.value = formattedDate
                    },
                    year,
                    month,
                    day
                )

                // Directly show the date picker when the dialog is shown
                LaunchedEffect(Unit) {
                    datePickerDialog.show()
                }

                Text("Selected Date: ${selectedDate.value}", fontSize = 16.sp)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDateSelected(selectedDate.value) // Passing the selected date to the parent
                    onDismissRequest() // Dismissing the dialog
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = { onDismissRequest() }) {
                Text("Cancel")
            }
        }
    )
}



fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Change format as needed
    return dateFormat.format(calendar.time)
}

@Composable
fun SuggestionItem(suggestion: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .background(Color(0xFFB3E5FC), MaterialTheme.shapes.medium)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(suggestion, fontSize = 16.sp, color = Color.Black)
        IconButton(onClick = { /* Remove suggestion */ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Remove",
                tint = Color.Black
            )
        }
    }
}

