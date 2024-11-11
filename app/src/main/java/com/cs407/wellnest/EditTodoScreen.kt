package com.cs407.wellnest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EditTodoScreen(itemName: String, navController: NavController) {
    var goalText by remember { mutableStateOf(TextFieldValue(itemName)) }
    var descriptionText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedRepeatOption by remember { mutableStateOf("Does not repeat") }
    var expandedDropdown by remember { mutableStateOf(false) }

    val repeatOptions = listOf("Does not repeat", "Daily", "Weekly", "Monthly")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC)) // Light blue background
            .padding(16.dp)
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
            label = { Text("Goal") },
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Repeat options dropdown
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
        Spacer(modifier = Modifier.height(12.dp))

        Text("Suggestions", fontSize = 14.sp, color = Color.Gray)

        val suggestions = listOf(
            "Send 1 job application",
            "Complete Zybooks Chapter 7",
            "Complete Zybooks Chapter 8",
            "Search for 1 grad school"
        )

        suggestions.forEach { suggestion ->
            SuggestionItem(suggestion)
        }


        // Action buttons
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
                    .padding(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancel")
            }
        }
    }
}

@Composable
fun SuggestionItem(suggestion: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFFB3E5FC), MaterialTheme.shapes.medium)
            .padding(16.dp),
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

