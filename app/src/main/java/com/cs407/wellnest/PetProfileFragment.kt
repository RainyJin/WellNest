package com.cs407.wellnest

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.cs407.wellnest.data.birdData
import com.cs407.wellnest.data.messages
import kotlin.random.Random

@SuppressLint("ServiceCast")
@Composable
fun PetProfileScreen(navController: NavController,  isDarkMode: MutableState<Boolean>) {
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var birdNameDescPair by remember { mutableStateOf<Pair<String, String>?>(null) }
    var message by remember { mutableStateOf("Hi! How are you today?") }

    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, // priority
        10000L // update interval
    ).apply {
        setMinUpdateIntervalMillis(5000L) // minimum interval
        setWaitForAccurateLocation(true)
    }.build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationData = locationResult.lastLocation
            if (locationData != null) {
                location = Pair(locationData.latitude, locationData.longitude)
            } else {
                location = Pair(38.0, 106.0) // default location
            }
        }
    }

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    birdNameDescPair = location?.let { getBirdNameAndDescription(it.first, it.second) }

    // stop location updates
    DisposableEffect(Unit) {
        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    // Dark Mode Colors
    val backgroundColor = if (isDarkMode.value) Color.Black else Color.White
    val textColor = if (isDarkMode.value) Color.White else Color.Black
    val cardColor = if (isDarkMode.value) Color.DarkGray else Color.White
    val messageBackgroundColor = if (isDarkMode.value) Color(0xFF2E7D32) else Color(0xFFE8F5E9)
    val messageTextColor = if (isDarkMode.value) Color.White else Color(0xFF4CAF50)


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor) // Background changes dynamically
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = textColor // Icon color changes dynamically
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 32.dp)
                .verticalScroll(rememberScrollState()) // Enable scrolling
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Speech bubble
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(230.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(messageBackgroundColor) // Background of the message
                        .padding(16.dp)
                ) {
                    Text(text = message, fontSize = 18.sp, color = messageTextColor)
                }
            }

            Image(
                painter = painterResource(id = R.drawable.bird_img),
                contentDescription = "Bird Image",
                modifier = Modifier
                    .height(250.dp)
                    .width(250.dp)
                    .align(Alignment.Start)
            )

            // Icon buttons section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { message = getMessage("self") }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_self),
                        contentDescription = "Self",
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(onClick = { message = getMessage("study") }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_study),
                        contentDescription = "Study",
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(onClick = { message = getMessage("relationship") }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_relationship),
                        contentDescription = "Relationship",
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(onClick = { message = getMessage("emotion") }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_emotion),
                        contentDescription = "Emotion",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Text box below icons
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(messageBackgroundColor)
                    .padding(vertical = 12.dp, horizontal = 16.dp) 
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Select things to talk about",
                    color = messageTextColor,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Profile Section Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Profile",
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Profile Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardColor, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                birdNameDescPair?.let {
                    ProfileDetailItem(label = "Breed", value = it.first,
                        detail = it.second, textColor = textColor)
                }
                ProfileDetailItem(label = "Age", value = "27 Days", textColor = textColor)
                ProfileDetailItem(label = "Weight", value = "20g", textColor = textColor)
                ProfileDetailItem(label = "Height", value = "12cm", textColor = textColor)
            }
        }
    }
}

@Composable
fun ProfileDetailItem(label: String, value: String, detail: String = "", textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = textColor),
            modifier = Modifier.width(100.dp)
        )
        Column {
            Text(text = value, style = MaterialTheme.typography.bodyLarge.copy(color = textColor))
            if (detail != "") {
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = detail, style = MaterialTheme.typography.bodyMedium.copy(color = textColor))
            }
        }

    }
}

fun getBirdNameAndDescription(lat: Double, lon: Double): Pair<String, String> {
    for ((name, detail) in birdData) {
        if ((lat in minOf(detail.lat1, detail.lat2)..maxOf(detail.lat1, detail.lat2)) &&
            (lon in minOf(detail.lon1, detail.lon2)..maxOf(detail.lon1, detail.lon2))) {
            return Pair(name, detail.description)
        }
    }

    return Pair("Northern Cardinal",
                "Striking and familiar backyard bird throughout most of eastern North America. " +
                "Crest, large red bill, and long tail.") // default bird
}

fun getMessage(category: String): String {
    val filteredMessages = messages.filter { message ->  message.category == category }
    val i = Random.nextInt(10)
    return filteredMessages[i].message
}