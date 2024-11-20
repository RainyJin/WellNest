package com.cs407.wellnest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TodoScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) } // State to track selected tab

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val backgroundColor = if (selectedTabIndex == 0) {
                    Color(0xFF5BBAE9) // Light Blue for Academics
                } else {
                    Color(0xFF48AB4C) // Green for Health
                }

                // Navigate with the selectedTabIndex and background color
                navController.navigate("edit_todo/new/$selectedTabIndex/${backgroundColor.toArgb()}")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add To-Do"
                )
            }
        },
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp) // Additional padding within inner padding
        ) {
            TopSection(onMeditationClick = { navController.navigate("meditation") })
            Spacer(modifier = Modifier.height(32.dp))
            PlaceholderImage(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(52.dp))
            CategoryTabs(selectedTabIndex = selectedTabIndex) { index ->
                selectedTabIndex = index // Update selected tab when clicked
            }
            Spacer(modifier = Modifier.height(12.dp))
            TodoListSection(
                selectedTabIndex = selectedTabIndex,
                navController = navController,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }

}

@Composable
fun TopSection(onMeditationClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { /* Another action */ },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_todo),
                contentDescription = "First Button",
                modifier = Modifier.size(32.dp)
            )
        }
        IconButton(
            onClick = onMeditationClick,  // Trigger passed action
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_meditation),
                contentDescription = "Second Button",
                modifier = Modifier.size(32.dp)
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
fun MeditationScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Meditation Screen", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back to Todo Screen")
        }
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
fun TodoListSection(selectedTabIndex: Int,
                    navController: NavController,
                    modifier: Modifier = Modifier) {
    val backgroundColor = when (selectedTabIndex) {
        0 -> Color(0xFF5BBAE9) // Light Blue for Academics
        else -> Color(0xFF48AB4C) // Green for Health
    }

    val academicGoals = when (selectedTabIndex) {
        0 -> listOf(
            TodoItem("CS 407", "Complete Zybooks Chapter 6", false),
            TodoItem("Folklore 100", "Read 'Our Lady's Maid...'", true),
            TodoItem("Math 101", "Prepare for Midterm Exam", false),
            TodoItem("Physics 201", "Submit Lab Report", false),
        )
        else -> listOf(
            TodoItem("40 minutes of Treadmill", "", false),
            TodoItem("10 reps of Battemont Frappes", "", true)
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxHeight(), // Ensure it stretches vertically
        contentPadding = PaddingValues(
            bottom = WindowInsets.systemBars
                .asPaddingValues()
                .calculateBottomPadding()
        )
    ) {
        item {
            Text("Today:", fontSize = 20.sp, modifier = Modifier.padding(vertical = 8.dp))
        }
        items(academicGoals) { todoItem ->
            TodoListItem(
                course = todoItem.course,
                description = todoItem.description,
                isCompleted = todoItem.isCompleted,
                onClick = {
                    // Navigate to the edit screen with the item ID (e.g., course name)
                    navController.navigate("edit_todo/${todoItem.course}/${selectedTabIndex}/${backgroundColor.toArgb()}")
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Tomorrow:", fontSize = 20.sp, modifier = Modifier.padding(vertical = 8.dp))
        }

        items(listOf(TodoItem("Add a goal", "", false))) { todoItem ->
            TodoListItem(
                course = todoItem.course,
                description = todoItem.description,
                isCompleted = todoItem.isCompleted,
                onClick = {
                    // Navigate to the edit screen with a placeholder ID
                    navController.navigate("edit_todo/${todoItem.course}/${selectedTabIndex}/${backgroundColor.toArgb()}")
                }
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
fun TodoListItem(course: String, description: String, isCompleted: Boolean, onClick: () -> Unit) {
    var isChecked by remember { mutableStateOf(isCompleted) }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable { onClick() }, // Trigger onClick when clicked
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.Blue, shape = CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
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
                isChecked = !isChecked
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check_circle),
                    contentDescription = if (isChecked) "Task Completed" else "Complete Task",
                    tint = if (isChecked) Color.Green else Color.Blue
                )
            }
        }
    }
}



