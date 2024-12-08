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
fun PetProfileScreen(navController: NavController) {
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
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
                tint = Color.Black
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
                        .background(Color(0xFFE8F5E9))
                        .padding(16.dp)
                ) {
                    Text(text = message, fontSize = 18.sp, color = Color(0xFF4CAF50))
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
                        painter = painterResource(id = R.drawable.ic_self), // Replace with your image resource
                        contentDescription = "Self",
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(onClick = { message = getMessage("study") }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_study), // Replace with your image resource
                        contentDescription = "Study",
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(onClick = { message = getMessage("relationship") }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_relationship), // Replace with your image resource
                        contentDescription = "Relationship",
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(onClick = { message = getMessage("emotion") }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_emotion), // Replace with your image resource
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
                    .background(Color(0xFFE8F5E9)) // Light green background
                    .padding(vertical = 12.dp, horizontal = 16.dp) // Adjust padding as needed
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Select things to talk about",
                    color = Color(0xFF4CAF50), // Green text color
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
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Profile Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                birdNameDescPair?.let {
                    ProfileDetailItem(label = "Breed", value = it.first,
                        detail = it.second)
                }
                ProfileDetailItem(label = "Age", value = "27 Days")
                ProfileDetailItem(label = "Weight", value = "20g")
                ProfileDetailItem(label = "Height", value = "12cm")
            }
        }
    }
}

@Composable
fun ProfileDetailItem(label: String, value: String, detail: String = "") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.width(100.dp)
        )
        Column {
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
            if (detail != "") {
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = detail, style = MaterialTheme.typography.bodyMedium)
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