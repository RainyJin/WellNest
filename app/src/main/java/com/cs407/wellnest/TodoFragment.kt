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
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun TodoScreen(navController: NavController, viewModel: TodoViewModel = viewModel()) {
    var selectedTabIndex by remember { mutableStateOf(0) } // State to track selected tab
    val todos by viewModel.getTodosByCategory(selectedTabIndex).collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()

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
            TopSection()
            Spacer(modifier = Modifier.height(32.dp))
            PlaceholderImage(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(52.dp))
            CategoryTabs(selectedTabIndex = selectedTabIndex) { index ->
                selectedTabIndex = index // Update selected tab when clicked
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                contentPadding = PaddingValues(bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding())
            ) {
                item {
                    Text("Today:", fontSize = 20.sp, modifier = Modifier.padding(vertical = 8.dp))
                }

                items(todos.filter { !it.isCompleted }) { todo ->
                    TodoListItem(
                        todo = todo,
                        onCheckChanged = { isCompleted ->
                            scope.launch {
                                viewModel.updateTodoCompletion(todo.id, isCompleted)
                            }
                        },
                        onClick = {
                            val backgroundColor = if (selectedTabIndex == 0) {
                                Color(0xFF5BBAE9)
                            } else {
                                Color(0xFF48AB4C)
                            }
                            navController.navigate("edit_todo/${todo.id}/${selectedTabIndex}/${backgroundColor.toArgb()}")
                        }
                    )
                }
            }
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
fun TodoListItem(todo: TodoEntity,
                 onCheckChanged: (Boolean) -> Unit,
                 onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
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
                    .background(if (todo.category == 0) Color(0xFF5BBAE9) else Color(0xFF48AB4C),
                        shape = CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    todo.title,
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                if (todo.description.isNotEmpty()) {
                    Text(
                        todo.description,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }

            IconButton(onClick = { onCheckChanged(!todo.isCompleted) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check_circle),
                    contentDescription = if (todo.isCompleted) "Task Completed" else "Complete Task",
                    tint = if (todo.isCompleted) Color.Green else Color.Blue
                )
            }
        }
    }
}



