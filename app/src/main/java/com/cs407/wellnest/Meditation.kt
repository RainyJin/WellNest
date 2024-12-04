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
fun MeditationScreen(navController: NavHostController) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(0) }
    var userTimeInput by remember { mutableStateOf("") }
    var showSoundDialog by remember { mutableStateOf(false) }

    // List of available sounds
    val soundOptions = remember {
        listOf(
            SoundOption("Rain Sound", R.raw.rain_sound),
            // Add more sounds here
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Relax and Meditate", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (timeLeft > 0) {
                "Time left: ${timeLeft / 60}:${String.format("%02d", timeLeft % 60)}"
            } else {
                "Set your meditation time"
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Sound selection button
        Button(
            onClick = { showSoundDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Selected Sound: ${selectedSound.name}")
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userTimeInput,
            onValueChange = { userTimeInput = it.filter { char -> char.isDigit() } },
            label = { Text("Set time (minutes)") },
            modifier = Modifier.fillMaxWidth()
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
            enabled = userTimeInput.isNotEmpty()
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
            enabled = isPlaying
        ) {
            Text("Stop Meditation")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Back to Todo Screen")
        }
    }

    // Sound selection dialog
    if (showSoundDialog) {
        AlertDialog(
            onDismissRequest = { showSoundDialog = false },
            title = { Text("Choose Sound") },
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
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = sound.name,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSoundDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
}