package com.cs407.wellnest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AboutUsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "About Us",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Add team member details
        Text(text = "This project WellNest is the final project from CS407, and we are the senior students at UW-Madiosn. We are Rainy, Charlotte, Jordan, Zikun", fontSize = 20.sp)


        Spacer(modifier = Modifier.height(24.dp))

        // Back button to return to the profile screen
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back", color = Color.White)
        }
    }
}
