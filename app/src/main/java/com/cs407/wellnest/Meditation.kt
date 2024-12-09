package com.cs407.wellnest

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cs407.wellnest.R
import kotlinx.coroutines.delay

data class SoundOption(
    val name: String,
    val resourceId: Int
)

@Composable
fun MeditationScreen(navController: NavHostController, isDarkMode: MutableState<Boolean>) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(0) }
    var userTimeInput by remember { mutableStateOf("") }
    var showSoundDialog by remember { mutableStateOf(false) }

    // List of available sounds
    val soundOptions = remember {
        listOf(
            SoundOption("Rain Sound", R.raw.rain_sound),
            SoundOption("Ocean Waves", R.raw.ocean_waves),
            SoundOption("Forest Birds", R.raw.forest),
            SoundOption("Meditation Bell", R.raw.wind_chimes)
        )
    }

    var selectedSound by remember { mutableStateOf(soundOptions[0]) }

    // Initialize MediaPlayer with selected sound
    val mediaPlayer = remember(selectedSound) {
        MediaPlayer.create(context, selectedSound.resourceId).apply {
            isLooping = true
        }
    }

    // Countdown effect
    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft -= 1
        } else if (isPlaying) {
            mediaPlayer.pause()
            isPlaying = false
        }
    }

    // Dark Mode Colors
    val backgroundColor = if (isDarkMode.value) Color.Black else Color.LightGray
    val textColor = if (isDarkMode.value) Color.White else Color.Black
    val cardBackgroundColor = if (isDarkMode.value) Color.DarkGray else Color.White
    val buttonColor = if (isDarkMode.value) Color.Gray else Color.LightGray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Relax and Meditate",
            style = MaterialTheme.typography.headlineSmall.copy(color = textColor)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (timeLeft > 0) {
                "Time left: ${timeLeft / 60}:${String.format("%02d", timeLeft % 60)}"
            } else {
                "Set your meditation time"
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Sound selection button
        Button(
            onClick = { showSoundDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = textColor
            )
        ) {
            Text("Selected Sound: ${selectedSound.name}")
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userTimeInput,
            onValueChange = { userTimeInput = it.filter { char -> char.isDigit() } },
            label = { Text("Set time (minutes)", color = textColor) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(color = textColor),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedIndicatorColor = textColor,
                unfocusedIndicatorColor = textColor,
                cursorColor = textColor
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val minutes = userTimeInput.toIntOrNull() ?: 0
                timeLeft = minutes * 60
                if (!isPlaying) {
                    mediaPlayer.start()
                    isPlaying = true
                }
            },
            enabled = userTimeInput.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDarkMode.value) Color(0xFF1E88E5) else Color(0xFFBBDEFB), // Active blue colors
                contentColor = Color.White,
                disabledContainerColor = if (isDarkMode.value) Color(0xFF424242) else Color(0xFFBDBDBD), // Gray for disabled
                disabledContentColor = if (isDarkMode.value) Color.LightGray else Color.DarkGray // Lighter gray for text
            )
        ) {
            Text("Start Meditation")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                mediaPlayer.pause()
                isPlaying = false
                timeLeft = 0
            },
            enabled = isPlaying,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDarkMode.value) Color(0xFF1E88E5) else Color(0xFFBBDEFB),
                contentColor = Color.White,
                disabledContainerColor = if (isDarkMode.value) Color(0xFF424242) else Color(0xFFBDBDBD), // Gray for disabled
                disabledContentColor = if (isDarkMode.value) Color.LightGray else Color.DarkGray // Lighter gray for text
            )
        ) {
            Text("Stop Meditation")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDarkMode.value) Color(0xFF1E88E5) else Color(0xFFBBDEFB), // Blue for dark mode, light blue for light mode
                contentColor = Color.White, // White text for both modes

            )
        ) {
            Text("Back to Todo Screen")
        }


        // Sound selection dialog
    if (showSoundDialog) {
        AlertDialog(
            onDismissRequest = { showSoundDialog = false },
            title = { Text("Choose Sound", color = textColor) },
            text = {
                Column {
                    soundOptions.forEach { sound ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isPlaying) {
                                        mediaPlayer.stop()
                                    }
                                    selectedSound = sound
                                    showSoundDialog = false
                                }
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(4.dp),
                            color = cardBackgroundColor
                        ) {
                            Text(
                                text = sound.name,
                                modifier = Modifier.padding(8.dp),
                                color = textColor
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSoundDialog = false }) {
                    Text("Cancel", color = textColor)
                }
            },
            containerColor = cardBackgroundColor
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
    }
}