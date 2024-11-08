package com.cs407.wellnest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController) {
    // Dark mode state
    val isDarkMode = remember { mutableStateOf(false) }
    val backgroundColor = if (isDarkMode.value) Color.Black else Color.White
    val contentColor = if (isDarkMode.value) Color.White else Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Section
        ProfileSection(contentColor)

        Spacer(modifier = Modifier.height(24.dp))

        // Settings Section
        SettingsSection(
            isDarkMode,
            onAboutUsClick = { navController.navigate("nav_about_us") }
        )
    }
}

@Composable
fun ProfileSection(contentColor: Color) {
    Box(
        modifier = Modifier
            .size(100.dp), // Size of the avatar area
        contentAlignment = Alignment.BottomEnd
    ) {
        // Avatar Icon
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile Avatar",
            modifier = Modifier.size(100.dp),
            tint = contentColor
        )

        // Plus Button
        IconButton(
            onClick = { /* Handle avatar change action here */ },
            modifier = Modifier
                .size(25.dp)
                .offset(x = (-4).dp, y = (-4).dp)
                .background(Color.Black, CircleShape)
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
        fontSize = 24.sp,
        color = contentColor
    )
}

@Composable
fun SettingsSection(isDarkMode: MutableState<Boolean>, onAboutUsClick: () -> Unit) {
    val textColor = if (isDarkMode.value) Color.White else Color.Black
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Notification Preferences
        Text(
            text = "Notification Preferences",
            style = MaterialTheme.typography.titleSmall,
            color = textColor,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable { onAboutUsClick() }

        )

        NotificationPreferenceItem("Meditation reminder", isDarkMode.value)
        NotificationPreferenceItem("Bedtime reminder", isDarkMode.value)
        NotificationPreferenceItem("Wake Up reminder", isDarkMode.value)

        DarkModeToggle(isDarkMode)

        Spacer(modifier = Modifier.height(16.dp))

        // Help & Policies Section
        Text(
            text = "Help & Policies",
            style = MaterialTheme.typography.titleSmall,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        HelpAndPoliciesSection(isDarkMode.value)

        Spacer(modifier = Modifier.height(16.dp))

        // About Us Section
        Text(
            text = "About Us",
            style = MaterialTheme.typography.titleSmall,
            color = textColor,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable { onAboutUsClick() }
        )
    }
}

@Composable
fun NotificationPreferenceItem(title: String, isDarkMode: Boolean) {
    val isChecked = remember { mutableStateOf(true) }
    val textColor = if (isDarkMode) Color.White else Color.Black
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, color = textColor)
        Switch(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it }
        )
    }
}

@Composable
fun DarkModeToggle(isDarkMode: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Dark Mode", color = if (isDarkMode.value) Color.White else Color.Black)
        Switch(
            checked = isDarkMode.value,
            onCheckedChange = { isDarkMode.value = it }
        )
    }
}

@Composable
fun HelpAndPoliciesSection(isDarkMode: Boolean) {
    val textColor = if (isDarkMode) Color.White else Color.Black
    val options = listOf("Help", "Privacy Notice", "Feedback", "Delete account")
    val icons = listOf(
        painterResource(id = R.drawable.icon_help),
        rememberVectorPainter(image = Icons.Default.Lock),
        painterResource(id = R.drawable.icon_comment),
        rememberVectorPainter(image = Icons.Default.ArrowForward)
    )

    Column {
        options.zip(icons).forEach { (option, icon) ->
            HelpPolicyItem(option, icon, textColor)
        }
    }
}

@Composable
fun HelpPolicyItem(title: String, icon: Painter, textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, color = textColor)
        Icon(
            painter = icon,
            contentDescription = "$title Icon",
            modifier = Modifier.size(24.dp),
            tint = textColor
        )
    }
}