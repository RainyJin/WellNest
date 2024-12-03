package com.cs407.wellnest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.util.Calendar
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun ProfileScreen(navController: NavController) {
    // Dark mode state
    val isDarkMode = remember { mutableStateOf(false) }
    val backgroundColor = if (isDarkMode.value) Color.Black else Color.White
    val contentColor = if (isDarkMode.value) Color.White else Color.Black

    // Remember scroll state
    val scrollState = rememberScrollState()

    // Make the content scrollable by applying a verticalScroll modifier
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
            .verticalScroll(scrollState), // Enable vertical scrolling
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Section
        ProfileSection(contentColor)

        Spacer(modifier = Modifier.height(24.dp))

        // Settings Section
        SettingsSection(
            isDarkMode,
            onAboutUsClick = { navController.navigate("nav_about_us") },
            onFeedbackClick = { navController.navigate("survey") },
            onHelpClick = { navController.navigate("help") },
            onPrivacyClick = { navController.navigate("privacy") }
        )
    }
}


@Composable
fun ProfileSection(contentColor: Color) {
    // State to hold the selected avatar URI
    val avatarUri = remember { mutableStateOf<String?>(null) }

    // Launcher for selecting an image
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            avatarUri.value = uri?.toString()
        }
    )

    Box(
        modifier = Modifier
            .size(100.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        // Use rememberAsyncImagePainter inside the composable context
        val painter = if (avatarUri.value != null) {
            rememberAsyncImagePainter(avatarUri.value)
        } else {
            null
        }

        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = "Profile Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Default Avatar",
                modifier = Modifier.size(120.dp),
                tint = contentColor
            )
        }

        // Plus button to open file picker
        IconButton(
            onClick = { launcher.launch("image/*") },
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
fun SettingsSection(
    isDarkMode: MutableState<Boolean>,
    onAboutUsClick: () -> Unit,
    onFeedbackClick: () -> Unit,
    onHelpClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {
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
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Meditation Reminder
        ReminderPreference(
            title = "Meditation Reminder",
            isDarkMode = isDarkMode.value
        )

        // Bedtime Reminder
        ReminderPreference(
            title = "Bedtime Reminder",
            isDarkMode = isDarkMode.value
        )

        // Wake Up Reminder
        ReminderPreference(
            title = "Wake Up Reminder",
            isDarkMode = isDarkMode.value
        )

        // Dark Mode Toggle
        DarkModeToggle(isDarkMode)

        Spacer(modifier = Modifier.height(16.dp))

        // Help & Policies
        Text(
            text = "Help & Policies",
            style = MaterialTheme.typography.titleSmall,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Help Option
        HelpPolicyItem(
            title = "Help",
            icon = painterResource(id = R.drawable.icon_help),
            textColor = textColor,
            onClick = onHelpClick
        )

        // Privacy Notice
        HelpPolicyItem(
            title = "Privacy Notice",
            icon = rememberVectorPainter(image = Icons.Default.Lock),
            textColor = textColor,
            onClick = onPrivacyClick
        )

        // Feedback
        HelpPolicyItem(
            title = "Feedback",
            icon = painterResource(id = R.drawable.icon_comment),
            textColor = textColor,
            onClick = onFeedbackClick
        )

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
fun ReminderPreference(
    title: String,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val textColor = if (isDarkMode) Color.White else Color.Black
    val timeState = remember { mutableStateOf("No time set") }
    val isReminderOn = remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val notificationHelper = remember { NotificationHelper(context) }

    // Time picker dialog
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val timeString = String.format("%02d:%02d", hourOfDay, minute)
            timeState.value = timeString

            // Schedule notification
            val notificationCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)

                // If the time has already passed today, schedule for tomorrow
                if (timeInMillis < System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            notificationHelper.scheduleNotification(
                title,
                notificationCalendar.timeInMillis
            )
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, color = textColor)

            Switch(
                checked = isReminderOn.value,
                onCheckedChange = { isChecked ->
                    isReminderOn.value = isChecked
                    if (isChecked) {
                        timePickerDialog.show()
                    } else {
                        timeState.value = "No time set"
                        // Cancel any existing notifications here if needed
                    }
                }
            )
        }

        if (isReminderOn.value) {
            Text(
                text = "Reminder set for: ${timeState.value}",
                color = textColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
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
fun HelpAndPoliciesSection(isDarkMode: Boolean, onFeedbackClick: () -> Unit, onHelpClick: () -> Unit, onPrivacyClick: () -> Unit) {
    val textColor = if (isDarkMode) Color.White else Color.Black
    val options = listOf("Help", "Privacy Notice", "Feedback", "Delete account")
    val icons = listOf(
        painterResource(id = R.drawable.icon_help),
        rememberVectorPainter(image = Icons.Default.Lock),
        painterResource(id = R.drawable.icon_comment),

    )


    Column {
        options.zip(icons).forEach { (option, icon) ->
            HelpPolicyItem(option, icon, textColor) {
                when (option) {
                    "Help" -> onHelpClick() // Navigate to HelpScreen
                    "Feedback" -> onFeedbackClick()
                    "Privacy Notice" -> onPrivacyClick()
                    // Add other cases if needed
                }
            }
        }
    }
}

// Ensure HelpPolicyItem is outside HelpAndPoliciesSection
@Composable
fun HelpPolicyItem(title: String, icon: Painter, textColor: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
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
