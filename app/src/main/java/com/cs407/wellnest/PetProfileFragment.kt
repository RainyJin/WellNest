package com.cs407.wellnest

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.navigation.NavController

@Composable
fun PetProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
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
            // Speech bubble section
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
                    .padding(16.dp)
                    .align(Alignment.Bottom)
            ) {
                Text(text = "How are you today?", fontSize = 18.sp)
            }
        }

        // Pet placeholder
        Box(
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray)
                .padding(16.dp)
                .align(Alignment.Start)
        ) {
            Text(text = "Pet Placeholder")
        }

        Spacer(modifier = Modifier.height(16.dp))

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
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { /* Handle click */ }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go to Profile", tint = Color(0xFF4CAF50))
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Profile Details
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            ProfileDetailItem(label = "Breed", value = "Northern Cardinal",
                detail = "Striking and familiar backyard bird throughout most of eastern North " +
                        "America. Crest, large red bill, and long tail.")
            ProfileDetailItem(label = "Age", value = "27 Days")
            ProfileDetailItem(label = "Human", value = "Bucky")
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
