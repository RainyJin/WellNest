package com.cs407.wellnest

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
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
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.cs407.wellnest.data.birdData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@SuppressLint("ServiceCast")
@Composable
fun PetProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var birdNameDescPair by remember { mutableStateOf<Pair<String, String>?>(null) }

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
                Log.d("Location", "Updated location: (${locationData.latitude}, ${locationData.longitude})")
            } else {
                Log.d("Error", "LocationCallback returned null")
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

    if (location == null) {Log.d("Error", "location is null")}
    location?.let{
        Log.d("lat", "${it.first}")
        Log.d("long", "${it.second}")
    }

    birdNameDescPair = location?.let { getBirdNameAndDescription(it.first, it.second) }
    if (birdNameDescPair == null) {Log.d("Error", "birdNameDescPair is null")}
    birdNameDescPair?.let {
        Log.d("Bird Name", it.first)
        Log.d("Bird Des", it.second)
    }

    // stop location updates
    DisposableEffect(Unit) {
        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            // Speech bubble
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE8F5E9))
                    .padding(16.dp)
            ) {
                Text(text = "How are you today?", fontSize = 18.sp, color = Color(0xFF4CAF50))
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
            IconButton(onClick = { /* Handle click */ }) {
                Icon(Icons.Default.MailOutline, contentDescription = "Book", tint = Color(0xFF4CAF50))
            }
            IconButton(onClick = { /* Handle click */ }) {
                Icon(Icons.Default.Face, contentDescription = "Face", tint = Color(0xFF4CAF50))
            }
            IconButton(onClick = { /* Handle click */ }) {
                Icon(Icons.Default.Home, contentDescription = "Home", tint = Color(0xFF4CAF50))
            }
            IconButton(onClick = { /* Handle click */ }) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = "Group", tint = Color(0xFF4CAF50))
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