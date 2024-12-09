package com.cs407.wellnest

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cs407.wellnest.data.CountdownEntity
import com.cs407.wellnest.ui.theme.DeepPink
import com.cs407.wellnest.ui.theme.LightPink
import com.cs407.wellnest.ui.theme.MidPink
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.UUID

@Composable
fun AddItemFragment(navController: NavController,
                    isDarkMode: MutableState<Boolean>,
                    viewModel: CountdownViewModel = viewModel()) {
    val context = LocalContext.current

    val backStackEntry = navController.currentBackStackEntry
    val eventIdArg = backStackEntry?.arguments?.getString("eventId") ?: UUID.randomUUID().toString()
    val eventDescArg = backStackEntry?.arguments?.getString("eventDesc") ?: ""
    val eventDateArg = backStackEntry?.arguments?.getString("eventDate") ?:
        LocalDate.now().format(DateTimeFormatter.ofPattern("M/d/yyyy"))
    val eventRepeatArg = backStackEntry?.arguments?.getString("eventRepeat") ?: "Does not repeat"
    val eventEndDateArg = backStackEntry?.arguments?.getString("eventEndDate") ?: "Select End Date"

    // input field, date picker, and repeat option default texts
    var eventDesc by remember { mutableStateOf(eventDescArg) }
    var eventDate by remember { mutableStateOf(eventDateArg) }
    var eventRepeat by remember { mutableStateOf(eventRepeatArg) }
    var eventEndDate by remember { mutableStateOf(eventEndDateArg) }

    // hardcoded suggestion list
    val suggestions = remember {
        mutableStateListOf("Birthday", "Folklore 100 Final", "School Starts")
    }

    // dropdown menu
    var expandedDropdown by remember { mutableStateOf(false) }
    val repeatOptions = listOf("Does not repeat", "Daily", "Weekly", "Monthly", "Yearly")

    // date picker dialog
    val calendar = Calendar.getInstance()

    // Dynamic Colors for Dark Mode
    val backgroundColor = if (isDarkMode.value) Color.Black else LightPink
    val cardColor = if (isDarkMode.value) Color.DarkGray else Color.White
    val textColor = if (isDarkMode.value) Color.White else Color.Black
    val buttonColor = if (isDarkMode.value) MidPink else LightPink

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // event input card
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(vertical = 40.dp, horizontal = 25.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            ) {
                // close button
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Default.Close,
                        contentDescription = "Close",
                        tint = textColor
                    )
                }

                // Input Field
                TextField(
                    value = eventDesc,
                    onValueChange = { eventDesc = it },
                    placeholder = { Text("Enter a new event...", color = textColor) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        focusedIndicatorColor = textColor,
                        unfocusedIndicatorColor = textColor,
                        cursorColor = textColor
                    )
                )

                Spacer(modifier = Modifier.height(40.dp))

                // calendar and repeat options
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconText(
                        icon = R.drawable.ic_calendar,
                        text = eventDate,
                        textColor = textColor,
                        onClick = { DatePickerDialog(
                            navController.context,
                            { _, year, month, day -> eventDate = "${month + 1}/$day/$year" },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show() }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconText(
                            icon = R.drawable.ic_repeat,
                            text = eventRepeat,
                            textColor = textColor, // Added textColor
                            onClick = { expandedDropdown = !expandedDropdown }
                        )
                        if (eventRepeat != "Does not repeat") {
                            IconText(
                                icon = R.drawable.ic_calendar,
                                text = eventEndDate,
                                textColor = textColor, // Added textColor
                                onClick = {
                                    DatePickerDialog(
                                        context,
                                        { _, year, month, day -> eventEndDate = "${month + 1}/$day/$year" },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    ).show()
                                }
                            )
                        }
                    }
                }

                // box to overlay the dropdown menu
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (expandedDropdown) {
                        DropdownMenu(
                            expanded = true,
                            onDismissRequest = { expandedDropdown = false },
                            modifier = Modifier
                                .width(300.dp)
                                .align(Alignment.TopStart)
                        ) {
                            repeatOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = textColor) },
                                    onClick = {
                                        eventRepeat = option
                                        expandedDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // save button
                Button(
                    onClick = {
                        // Field checking
                        if (eventDesc.isBlank() || eventDate.isBlank() ||
                            (eventRepeat != "Does not repeat" && eventEndDate.isNullOrBlank())) {
                            Toast.makeText(context, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        // Create an countdown item and add
                        val countdown = CountdownEntity(
                            id = eventIdArg,
                            targetDate = LocalDate.parse(
                                eventDate,
                                DateTimeFormatter.ofPattern("M/d/yyyy")
                            ).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                            description = eventDesc,
                            repeatOption = eventRepeat,
                            endDate = if (eventRepeat != "Does not repeat") eventEndDate else null
                        )

                        viewModel.apply {
                            viewModelScope.launch {
                                if (eventRepeat == "Does not repeat") {
                                    val existingCountdown = viewModel.getCountdownByIdAndDate(
                                        countdown.id, countdown.targetDate
                                    )
                                    if (existingCountdown == null || existingCountdown != countdown) {
                                        viewModel.upsertCountdown(countdown)
                                    }
                                } else {
                                    val dates = generateRepeatingDates(countdown)
                                    val repeatingCountdowns = dates.map { date ->
                                        countdown.copy(
                                            targetDate = date.format(DateTimeFormatter.ofPattern("M/d/yyyy"))
                                        )
                                    }

                                    repeatingCountdowns.forEach { repeatingCountdown ->
                                        val existingCountdown = viewModel.getCountdownByIdAndDate(
                                            repeatingCountdown.id, repeatingCountdown.targetDate
                                        )
                                        if (existingCountdown == null || existingCountdown != repeatingCountdown) {
                                            viewModel.upsertCountdown(repeatingCountdown)
                                        }
                                    }
                                }
                            }
                        }

                        if (eventRepeat == "Does not repeat") {
                            viewModel.apply {
                                viewModelScope.launch {
                                    upsertCountdown(countdown)
                                }
                            }
                        } else {
                            val dates = generateRepeatingDates(countdown)
                            val repeatingCountdowns = dates.map { date ->
                                countdown.copy(
                                    targetDate = date.format(DateTimeFormatter.ofPattern("M/d/yyyy"))
                                )
                            }
                            Log.d("Number of items:", "${repeatingCountdowns.size}")
                            viewModel.apply {
                                viewModelScope.launch {
                                    repeatingCountdowns.forEach { countdown ->
                                        upsertCountdown(countdown)
                                    }
                                }
                            }
                        }
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save", color = textColor)
                }
            }
        }

        // suggestion title
        Text(
            text = "SUGGESTIONS",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 30.dp),
            color = buttonColor
        )

        // suggestion list
        suggestions.forEach { suggestion ->
            SuggestionItem(
                text = suggestion,
                onRemove = { suggestions.remove(suggestion) },
                isDarkMode = isDarkMode // Pass isDarkMode
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// clickable icon & text for calendar and repeat options
@Composable
fun IconText(icon: Int, text: String, textColor: Color, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton( onClick = onClick ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = textColor
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, color = textColor, modifier = Modifier.clickable { onClick() })
    }
}

@Composable
fun SuggestionItem(text: String, onRemove: () -> Unit , isDarkMode: MutableState<Boolean>) {
    val backgroundColor = if (isDarkMode.value) MidPink else LightPink
    val textColor = if (isDarkMode.value) Color.White else Color.Black

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 25.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, color = textColor)
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Default.Close,
                    contentDescription = "Remove",
                    tint = textColor
                )
            }
        }
    }
}

fun generateRepeatingDates(countdown: CountdownEntity): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    val formatter = DateTimeFormatter.ofPattern("M/d/yyyy")
    var curr = LocalDate.parse(countdown.targetDate, formatter)
    val last = LocalDate.parse(countdown.endDate, formatter)
    while (curr.isBefore(last) || curr.isEqual(last)) {
        dates.add(curr)
        curr = when (countdown.repeatOption) {
            "Daily" -> curr.plusDays(1)
            "Weekly" -> curr.plusWeeks(1)
            "Monthly" -> curr.plusMonths(1)
            "Yearly" -> curr.plusYears(1)
            else -> break
        }
    }
    return dates
}