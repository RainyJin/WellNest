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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cs407.wellnest.R

@Composable
fun MeditationScreen(navController: NavHostController) {
    val context = LocalContext.current // Get context outside remember
    var isPlaying by remember { mutableStateOf(false) }

    // Remember and initialize the MediaPlayer
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.rain_sound).apply {
            isLooping = true // Enable looping
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
        IconButton(
            onClick = {
                if (isPlaying) {
                    mediaPlayer.pause()
                } else {
                    mediaPlayer.start()
                }
                isPlaying = !isPlaying
            },
            modifier = Modifier.size(72.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_meditation),
                contentDescription = "Meditation",
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = if (isPlaying) "Pause Rain Sound" else "Play Rain Sound")
    }

    // Dispose of the MediaPlayer when the Composable leaves the composition
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
}
