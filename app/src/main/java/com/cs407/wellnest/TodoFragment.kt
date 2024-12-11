package com.cs407.wellnest

import android.content.Context
import android.opengl.GLSurfaceView
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.filament.Camera
import com.google.android.filament.Engine
import com.google.android.filament.EntityManager
import com.google.android.filament.Renderer
import com.google.android.filament.Scene
import com.google.android.filament.View
import com.google.android.filament.Viewport
import com.google.android.filament.gltfio.AssetLoader
import com.google.android.filament.gltfio.MaterialProvider
import com.google.android.material.snackbar.BaseTransientBottomBar
import io.github.sceneview.Scene
import io.github.sceneview.animation.ModelAnimator
import io.github.sceneview.animation.Transition.animateRotation
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironment
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun TodoScreen(navController: NavController, isDarkMode: MutableState<Boolean>, viewModel: TodoViewModel = viewModel()) {
    var selectedTabIndex by remember { mutableStateOf(0) } // State to track selected tab
    val todos by viewModel.getTodosByCategory(selectedTabIndex).collectAsState(initial = emptyList())

    val nextTenDays = generateNextTenDays()
    val groupedTodos = expandRecurringTodos(todos, nextTenDays)

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)

    // Dark Mode
    val backgroundColor = if (isDarkMode.value) Color.Black else Color.White
    val contentColor = if (isDarkMode.value) Color.White else Color.Black
    val cardColor = if (isDarkMode.value) Color.DarkGray else Color.White
    val cardTextColor = if (isDarkMode.value) Color.White else Color.DarkGray


    // Set up camera and rotation for the 3D view
    val cameraNode = rememberCameraNode(engine).apply {
        position = Position(x = 0.0f, y = 0.6f, z = 2.4f)
    }
    val centerNode = rememberNode(engine).apply {
        position = Position(x = 0.0f, y = 0.8f, z = 0.0f) // Example position for the target
    }.addChildNode(cameraNode)


    // Read the GLB model file from assets
    val assetManager = context.assets
    val inputStream = assetManager.open("northern_cardinal_stillAnim.glb")
    val buffer = inputStream.readBytes()
    inputStream.close()
    val byteBuffer = ByteBuffer.wrap(buffer)

    // Create ModelNode from model instance
    val modelInstance = modelLoader.createModelInstance(byteBuffer)
    val modelNode = ModelNode(modelInstance = modelInstance, scaleToUnits = 1.6f).apply {
        // Start the first two animations on a loop
        repeat(2) { index ->
            playAnimation(
                animationIndex = index,
            )
        }

        // Add onTouch handling for the third animation
        onTouch = { hitResult, motionEvent ->
            // Check if third animation exists
            if (modelInstance.animator.animationCount > 2) {
                // Check if third animation is already playing
                val thirdAnimationPlaying = playingAnimations.any { it.key == 2 }

                if (thirdAnimationPlaying) {
                    // If playing, stop the third animation
                    stopAnimation(animationIndex = 2)
                } else {
                    // If not playing, start the third animation
                    playAnimation(
                        animationIndex = 2,
                        loop = false

                    )
                }
            }
            false // Return false to allow further touch event handling
        }
    }

    val environmentAssetLocation = "autumn_field_puresky.hdr"
    val environment = environmentLoader.createHDREnvironment(assetFileLocation = environmentAssetLocation)

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
                    contentDescription = "Add To-Do",
                    tint = contentColor
                )
            }
        },
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor)
                .padding(horizontal = 16.dp) // Additional padding within inner padding
        ) {
            TopSection(onMeditationClick = { navController.navigate("meditation") }, onPetClick={navController.navigate("pet_profile")},
                isDarkMode = isDarkMode)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Control the height of the 3D scene
                    .background(Color.White)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                if (environment != null) {
                    Scene(
                        modifier = Modifier.fillMaxSize(),
                        engine = engine,
                        modelLoader = modelLoader,
                        cameraNode = cameraNode,
                        childNodes = listOf(centerNode, modelNode),
                        onFrame = {
//                            centerNode.rotation = cameraRotation
                            cameraNode.lookAt(centerNode)
                        },
                        environment = environment
                    )
                }
            }
            Spacer(modifier = Modifier.height(42.dp))
            CategoryTabs(selectedTabIndex = selectedTabIndex) { index ->
                selectedTabIndex = index // Update selected tab when clicked
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                contentPadding = PaddingValues(bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding())
            ) {
                // Filter out dates that have no uncompleted todos
                val filteredDates = nextTenDays.filter { date ->
                    val todosForDate = groupedTodos[date] ?: emptyList()
                    todosForDate.any { !it.isCompleted }
                }

                filteredDates.forEach { date ->
                    val todosForDate = groupedTodos[date] ?: emptyList()
                    val uncompletedTodos = todosForDate.filter { !it.isCompleted }

                    if (uncompletedTodos.isNotEmpty()) {
                        // Header for the date
                        item {
                            val dateText = when {
                                date == LocalDate.now() -> "Today, ${date.format(DateTimeFormatter.ofPattern("MMM d"))}"
                                date == LocalDate.now().plusDays(1) -> "Tomorrow, ${date.format(DateTimeFormatter.ofPattern("MMM d"))}"
                                else -> date.format(DateTimeFormatter.ofPattern("EEE, MMM d"))
                            }
                            Text(
                                text = dateText,
                                fontSize = 20.sp,
                                color = contentColor,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        // Todos for this date
                        items(uncompletedTodos) { todo ->
                            TodoListItem(
                                todo = todo,
                                onCheckChanged = { isCompleted ->
                                    scope.launch {
                                        viewModel.completeTodo(todo)
                                    }
                                },
                                onClick = {
                                    val backgroundColor = if (selectedTabIndex == 0) {
                                        Color(0xFF5BBAE9)
                                    } else {
                                        Color(0xFF48AB4C)
                                    }
                                    navController.navigate("edit_todo/${todo.id}/$selectedTabIndex/${backgroundColor.toArgb()}")
                                },
                                cardColor = cardColor,
                                textColor = cardTextColor
                            )
                        }
                    }
                }

            }
        }
    }

}

@Composable
fun TopSection(onMeditationClick: () -> Unit, onPetClick: () -> Unit, isDarkMode: MutableState<Boolean>) {
    val iconColor = if (isDarkMode.value) Color.White else Color.Black
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onPetClick,
            modifier = Modifier.size(48.dp)
            ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_pet),
                contentDescription = "First Button",
                tint = iconColor,
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
                tint = iconColor,
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
fun CategoryTabs(selectedTabIndex: Int, onTabSelected: (Int) -> Unit ) {
    val categories = listOf("Academics", "Health")

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    ) {
        categories.forEachIndexed { index, option ->
            val colors = SegmentedButtonColors(
                activeContainerColor = when (option) {
                    "Academics" -> Color(0xFF5BBAE9)
                    "Health" -> Color(0xFF48AB4C)
                    else -> Color.Gray
                },
                activeContentColor = Color.White,
                activeBorderColor = Color.Transparent,
                inactiveContainerColor = Color.Transparent,
                inactiveContentColor = Color.Gray,
                inactiveBorderColor = Color.DarkGray,
                disabledActiveContainerColor = Color.LightGray,
                disabledActiveContentColor = Color.DarkGray,
                disabledActiveBorderColor = Color.Transparent,
                disabledInactiveContainerColor = Color.Transparent,
                disabledInactiveContentColor = Color.LightGray,
                disabledInactiveBorderColor = Color.Transparent
            )

            SegmentedButton(
                modifier = Modifier.weight(1f),
                shape = SegmentedButtonDefaults.itemShape(index = index, count = categories.size),
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                colors = colors
            ) {
                Text(
                    text = option,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}


@Composable
fun TodoListItem(todo: TodoEntity,
                 onCheckChanged: (Boolean) -> Unit,
                 onClick: () -> Unit,
                 cardColor: Color,
                 textColor: Color) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(Color(todo.color), shape = CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    todo.title,
                    fontSize = 22.sp,
                    color = textColor,
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                if (todo.description.isNotEmpty()) {
                    Text(
                        todo.description,
                        fontSize = 16.sp,
                        color = textColor.copy(alpha = 0.8f),
                        textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }

            IconButton(
                onClick = { onCheckChanged(!todo.isCompleted) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check_circle),
                    contentDescription = if (todo.isCompleted) "Task Completed" else "Complete Task",
                    tint = if (todo.isCompleted) Color(0xFF48AB4C) else Color(0xFF5BBAE9),
                    modifier = Modifier.size(42.dp)
                )
            }
        }
    }
}

fun generateNextTenDays(): List<LocalDate> {
    val today = LocalDate.now()
    return (0..9).map { today.plusDays(it.toLong()) }
}

fun expandRecurringTodos(todos: List<TodoEntity>, datesRange: List<LocalDate>): Map<LocalDate, List<TodoEntity>> {
    val expandedTodos = mutableMapOf<LocalDate, MutableList<TodoEntity>>()

    // First, add the original todos to their respective dates
    datesRange.forEach { date ->
        expandedTodos[date] = mutableListOf()
    }

    todos.forEach { todo ->
        val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        val originalDate = LocalDate.parse(todo.dueDate, dateFormatter)

        when (todo.repeatOption) {
            "Daily" -> {
                datesRange.forEach { date ->
                    if (date >= originalDate) {
                        expandedTodos[date]?.add(todo)
                    }
                }
            }
            "Weekly" -> {
                datesRange.forEach { date ->
                    if (date >= originalDate && ChronoUnit.DAYS.between(originalDate, date) % 7 == 0L) {
                        expandedTodos[date]?.add(todo)
                    }
                }
            }
            "Monthly" -> {
                datesRange.forEach { date ->
                    if (date >= originalDate && date.monthValue != originalDate.monthValue &&
                        date.dayOfMonth == originalDate.dayOfMonth) {
                        expandedTodos[date]?.add(todo)
                    }
                }
            }
            else -> {
                // Non-recurring todos
                if (originalDate in datesRange) {
                    expandedTodos[originalDate]?.add(todo)
                }
            }
        }
    }

    return expandedTodos
}





