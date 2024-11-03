package com.cs407.wellnest
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.rpc.Help
import androidx.compose.ui.res.painterResource

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Section
        ProfileSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Settings Section
        SettingsSection()
    }
}

@Composable
fun ProfileSection() {
    Box(
        modifier = Modifier
            .size(100.dp), // Size of the avatar area
        contentAlignment = Alignment.BottomEnd
    ) {
        // Avatar Icon
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile Avatar",
            modifier = Modifier
                .size(100.dp)  // Adjust avatar size as needed
        )

        // Plus Button
        IconButton(
            onClick = { /* Handle avatar change action here */ },
            modifier = Modifier
                .size(25.dp)  // Adjust size of the plus icon
                .offset(x = (-4).dp, y = (-4).dp)
                .background(Color.Black, CircleShape)
                .align(Alignment.BottomEnd)
                .padding(2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Change Avatar",
                tint = Color.White
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // User name text
    Text(
        text = "Bucky",
        fontSize = 24.sp
    )
}




@Composable
fun SettingsSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Setting",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Notification Preferences
        Text(
            text = "Notification Preferences",
            style = MaterialTheme.typography.titleSmall
        )

        NotificationPreferenceItem("Meditation reminder")
        NotificationPreferenceItem("Bedtime reminder")
        NotificationPreferenceItem("Wake Up reminder")

        // Dark Mode Toggle
        DarkModeToggle()

        Spacer(modifier = Modifier.height(16.dp))

        // Help & Policies Section
        Text(
            text = "Help & Policies",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        HelpAndPoliciesSection()

        Spacer(modifier = Modifier.height(16.dp))

        // About Us Section
        Text(
            text = "About Us",
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun NotificationPreferenceItem(title: String) {
    val isChecked = remember { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)
        Switch(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it }
        )
    }
}

@Composable
fun DarkModeToggle() {
    val isDarkMode = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Dark Mode")
        Switch(
            checked = isDarkMode.value,
            onCheckedChange = { isDarkMode.value = it }
        )
    }
}
@Composable
fun HelpAndPoliciesSection() {
    val options = listOf("Help", "Privacy Notice", "Feedback", "Delete account")
    val icons = listOf(
        painterResource(id = R.drawable.icon_help),
        rememberVectorPainter(image = Icons.Default.Lock),
        painterResource(id = R.drawable.icon_comment),
        rememberVectorPainter(image = Icons.Default.ArrowForward)
    )

    Column {
        options.zip(icons).forEach { (option, icon) ->
            HelpPolicyItem(option, icon)
        }
    }
}

@Composable
fun HelpPolicyItem(title: String, icon: Painter) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)
        Icon(
            painter = icon,
            contentDescription = "$title Icon",
            modifier = Modifier.size(24.dp)
        )
    }
}
