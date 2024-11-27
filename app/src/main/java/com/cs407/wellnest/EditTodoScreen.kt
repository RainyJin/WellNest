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
import android.app.DatePickerDialog
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EditTodoScreen(itemId: String?,
                   navController: NavController,
                   backgroundColor: Color,
                   viewModel: TodoViewModel = viewModel()
) {
    var goalText by remember { mutableStateOf(TextFieldValue("")) }
    var descriptionText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedRepeatOption by remember { mutableStateOf("Does not repeat") }
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var expandedDropdown by remember { mutableStateOf(false) }
    val repeatOptions = listOf("Does not repeat", "Daily", "Weekly", "Monthly")
    val category = if (backgroundColor == Color(0xFF5BBAE9)) 0 else 1

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        navController.context,
        { _, year, month, day ->
            selectedDate = "$month/$day/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val scope = rememberCoroutineScope()

    LaunchedEffect(itemId) {
        // If editing existing todo, load its data
        if (itemId != null && itemId != "new") {
            // Load existing todo data here

        }
    }

    val (saveButtonColor, suggestionColor) = when (backgroundColor) {
        Color(0xFF5BBAE9) -> Color(0xFF338FBD) to Color(0xFFD3E7F7) // Lighter blue
        Color(0xFF48AB4C) -> Color(0xFF48AB4C) to Color(0xFFD0F0D4) // Lighter green
        else -> Color.Black to Color.Gray // Default fallback
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
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
                    .padding(vertical = 6.dp, horizontal = 20.dp)
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
                        Text(
                            text = selectedDate,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .clickable { datePickerDialog.show() }
                                .padding(8.dp) // Optional padding for better touch interaction
                        )
                    }


                }

                Spacer(modifier = Modifier.height(8.dp))

                // Repeat options dropdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar), // Replace with your calendar icon resource
                            contentDescription = "Calendar",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
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

                // Save button beneath the "Does not repeat" section
                Spacer(modifier = Modifier.height(16.dp)) // Add space before the save button
                Button(
                    onClick = {
                        scope.launch {
                            val todo = TodoEntity(
                                id = itemId ?: UUID.randomUUID().toString(),
                                title = goalText.text,
                                description = descriptionText.text,
                                dueDate = selectedDate,
                                category = category,
                                repeatOption = selectedRepeatOption
                            )
                            viewModel.saveTodo(todo)
                            navController.navigate("nav_todo") {
                                popUpTo("nav_todo") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = saveButtonColor)
                ) {
                    Text("Save", fontSize = 16.sp)
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
                SuggestionItem(suggestion, suggestionColor)
            }
        }

    }
}

fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Change format as needed
    return dateFormat.format(calendar.time)
}

@Composable
fun SuggestionItem(suggestion: String, backgroundColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .background(backgroundColor, MaterialTheme.shapes.medium)
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

