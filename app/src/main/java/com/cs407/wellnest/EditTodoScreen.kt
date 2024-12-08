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
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.toArgb
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
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    val datePickerDialog = DatePickerDialog(
        navController.context,
        { _, year, month, day ->
            selectedDate = dateFormatter.format(
                LocalDate.of(year, month + 1, day) // Properly constructs the date
            )
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    var expandedColorDropdown by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(backgroundColor.toArgb()) }

    // Define a list of color options
    val colorOptions = listOf(
        ColorOption(Color(0xFF5BBAE9), "Blue"),
        ColorOption(Color(0xFF48AB4C), "Green"),
        ColorOption(Color(0xFFF7B32D), "Yellow"),
        ColorOption(Color(0xFFE76F51), "Coral"),
        ColorOption(Color(0xFF6A4C93), "Purple"),
        ColorOption(Color(0xFF1A535C), "Teal")
    )

    val todoItem by remember(itemId) {
        if (itemId != null && itemId != "new") {
            viewModel.getTodoByIdFlow(itemId)
        } else {
            MutableStateFlow(null)
        }
    }.collectAsState(initial = null)

    val scope = rememberCoroutineScope()

    LaunchedEffect(todoItem) {
        println("LaunchedEffect triggered with itemId: $itemId")
        if (itemId != null && itemId != "new") {
            val existingTodo = viewModel.getTodoById(itemId)
            println("Existing todo retrieved: $existingTodo")
            existingTodo?.let { todo ->
                goalText = TextFieldValue(todo.title)
                descriptionText = TextFieldValue(todo.description)
                selectedDate = todo.dueDate
                selectedRepeatOption = todo.repeatOption
                selectedColor = todo.color
            }
        }
    }

    val (saveButtonColor, suggestionColor) = when (backgroundColor) {
        Color(0xFF5BBAE9) -> Color(0xFF338FBD) to Color(0xFFD3E7F7) // Lighter blue
        Color(0xFF48AB4C) -> Color(0xFF48AB4C) to Color(0xFFD0F0D4) // Lighter green
        else -> Color.Black to Color.Gray // Default fallback
    }

    var showTitleError by remember { mutableStateOf(false) }

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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Box {
                        // Color preview box that triggers dropdown
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(selectedColor), shape = CircleShape)
                                .clickable { expandedColorDropdown = true }
                        )

                        DropdownMenu(
                            expanded = expandedColorDropdown,
                            onDismissRequest = { expandedColorDropdown = false },
                            modifier = Modifier.width(120.dp)
                        ) {
                            colorOptions.forEach { colorOption ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .background(colorOption.color, shape = CircleShape)
                                            )
                                            Text(colorOption.name)
                                        }
                                    },
                                    onClick = {
                                        selectedColor = colorOption.color.toArgb()
                                        expandedColorDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = goalText,
                        onValueChange = {
                            goalText = it
                            showTitleError = false // Reset error when user starts typing
                        },
                        label = { Text("Title") },
                        modifier = Modifier.weight(1f),
                        isError = showTitleError, // This will trigger the error state
                        supportingText = {
                            if (showTitleError) {
                                Text(
                                    text = "Title is required",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        // Optional: Customize the border color and outline when in error state
                        colors = OutlinedTextFieldDefaults.colors(
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error,
                            errorCursorColor = MaterialTheme.colorScheme.error
                        )
                    )
                }

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
                        if (goalText.text.isBlank()) {
                            showTitleError = true
                            return@Button
                        }
                        showTitleError = false

                        scope.launch {
                            val todo = TodoEntity(
                                id = if (itemId != null && itemId != "new") itemId else UUID.randomUUID().toString(),
                                title = goalText.text,
                                description = descriptionText.text,
                                dueDate = selectedDate,
                                category = category,
                                repeatOption = selectedRepeatOption,
                                color = selectedColor
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
            "Final Project Presentation",
            "Complete Zybooks Chapter 7",
            "Complete Zybooks Chapter 8",
            "Final Project Demo"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            items(suggestions) { suggestion ->
                SuggestionItem(
                    suggestion = suggestion,
                    backgroundColor = suggestionColor,
                    onClickSuggestion = {
                        // Clear previous text and add new suggestion
                        descriptionText = TextFieldValue(suggestion)
                    }
                )
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
fun SuggestionItem(
    suggestion: String,
    backgroundColor: Color,
    onClickSuggestion: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .background(backgroundColor, MaterialTheme.shapes.medium)
            .clickable { onClickSuggestion() } // Add clickable modifier
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(suggestion, fontSize = 16.sp, color = Color.Black)
        IconButton(onClick = { /* Optional: remove suggestion functionality */ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Remove",
                tint = Color.Black
            )
        }
    }
}

data class ColorOption(
    val color: Color,
    val name: String
)

