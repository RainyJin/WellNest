package com.cs407.wellnest

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cs407.wellnest.R
import kotlinx.coroutines.delay

@Composable
fun MeditationScreen(navController: NavHostController) {
    val context = LocalContext.current // Get context outside remember
    var isPlaying by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(0) } // Remaining time in seconds
    var userTimeInput by remember { mutableStateOf("") } // User input for meditation time


    // Remember and initialize the MediaPlayer
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.rain_sound).apply {
            isLooping = true // Enable looping
        }
    }

    // Start a countdown when timeLeft is set
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
                timeLeft = minutes * 60 // Convert to seconds
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
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
}

