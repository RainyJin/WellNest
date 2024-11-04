package com.cs407.wellnest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TodoScreen() {
    var selectedTabIndex by remember { mutableStateOf(0) } // State to track selected tab

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopSection()
        Spacer(modifier = Modifier.height(32.dp))
        PlaceholderImage(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(52.dp))
        CategoryTabs(selectedTabIndex = selectedTabIndex) { index ->
            selectedTabIndex = index // Update selected tab when clicked
        }
        Spacer(modifier = Modifier.height(10.dp))
        TodoListSection(selectedTabIndex = selectedTabIndex)
    }
}

@Composable
fun TopSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { /* Navigate to another page */ },
            modifier = Modifier.size(48.dp) // Adjust the button size here
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_todo), // Placeholder icon
                contentDescription = "First Button",
                modifier = Modifier.size(32.dp) // Adjust the icon size here
            )
        }
        IconButton(
            onClick = { /* Navigate to another page */ },
            modifier = Modifier.size(48.dp) // Adjust the button size here
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar), // Placeholder icon
                contentDescription = "Second Button",
                modifier = Modifier.size(32.dp) // Adjust the icon size here
            )
        }
    }
}

@Composable
fun PlaceholderImage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(120.dp)
            .background(Color.Gray, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Image", fontSize = 18.sp, color = Color.White)
    }
}

@Composable
fun CategoryTabs(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val categories = listOf("Academics", "Health")

    // SingleChoiceSegmentedButtonRow for selecting categories
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth() // Make the row fill the available width
            .padding(horizontal = 4.dp) // Add padding to the sides for better spacing
            .wrapContentWidth(Alignment.CenterHorizontally)
    ){
        categories.forEachIndexed { index, option ->
            SegmentedButton(
                modifier = Modifier
                    .weight(1f),
                shape = SegmentedButtonDefaults. itemShape(index = index, count = categories. size),
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index)
                }
            ) {
                Text(text = option, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun TodoListSection(selectedTabIndex: Int) {
    val academicGoals = when (selectedTabIndex) {
        0 -> listOf(
            TodoItem("CS 407", "Complete Zybooks Chapter 6", false),
            TodoItem("Folklore 100", "Read 'Our Lady's Maid...'", true),
            TodoItem("Math 101", "Prepare for Midterm Exam", false),
            TodoItem("Physics 201", "Submit Lab Report", false),
            TodoItem("History 102", "Study for Final", true),
            TodoItem("English 202", "Draft Essay", false)
        )
        else -> listOf(
            TodoItem("40 minutes of Treadmill", "", false),
            TodoItem("10 reps of Battemont Frappes", "", true)
        )
    }

    LazyColumn {
        item {
            Text("Today:", fontSize = 20.sp, modifier = Modifier.padding(vertical = 8.dp))
        }
        items(academicGoals) { todoItem ->
            TodoListItem(
                course = todoItem.course,
                description = todoItem.description,
                isCompleted = todoItem.isCompleted
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Tomorrow:", fontSize = 20.sp, modifier = Modifier.padding(vertical = 8.dp))
        }

        items(listOf(
            TodoItem("Add a goal", "", false)
        )) { todoItem ->
            TodoListItem(
                course = todoItem.course,
                description = todoItem.description,
                isCompleted = todoItem.isCompleted
            )
        }
    }
}

data class TodoItem(
    val course: String,
    val description: String,
    val isCompleted: Boolean
)

@Composable
fun TodoListItem(course: String, description: String, isCompleted: Boolean) {
    // State to track the completion status of the todo item
    var isChecked by remember { mutableStateOf(isCompleted) }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // To-do list icon
            Box(
                modifier = Modifier
                    .size(24.dp) // Size of the circle
                    .background(Color.Blue, shape = CircleShape) // Circle color
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f) // Allow text to take available space
            ) {
                Text(
                    course,
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                )
                if (description.isNotEmpty()) {
                    Text(
                        description,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }

            IconButton(onClick = {
                isChecked = !isChecked // Toggle completion status
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check_circle), // Placeholder icon
                    contentDescription = if (isChecked) "Task Completed" else "Complete Task",
                    tint = if (isChecked) Color.Green else Color.Blue
                )
            }
        }
    }
}


