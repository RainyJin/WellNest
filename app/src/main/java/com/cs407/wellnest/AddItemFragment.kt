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
fun AddItemFragment(navController: NavController, viewModel: CountdownViewModel = viewModel()) {
    val context = LocalContext.current

    val backStackEntry = navController.currentBackStackEntry
    val eventIdArg = backStackEntry?.arguments?.getString("eventId")
    val eventDescArg = backStackEntry?.arguments?.getString("eventDesc") ?: ""
    val eventDateArg = backStackEntry?.arguments?.getString("eventDate") ?:
        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
    val eventRepeatArg = backStackEntry?.arguments?.getString("eventRepeat") ?: "Does not repeat"
    val eventEndDateArg = backStackEntry?.arguments?.getString("eventEndDate") ?: "Select End Date"

    // input field, date picker, and repeat option default texts
    var eventDesc by remember { mutableStateOf(eventDescArg) }
    var eventDate by remember { mutableStateOf(eventDateArg) }
    var eventRepeat by remember { mutableStateOf(eventRepeatArg) }
    var eventEndDate by remember { mutableStateOf(eventEndDateArg) }

    // hardcoded suggestion list
    val suggestions = remember {
        mutableStateListOf("Birthday", "New Year", "School Starts")
    }

    // dropdown menu
    var expandedDropdown by remember { mutableStateOf(false) }
    val repeatOptions = listOf("Does not repeat", "Daily", "Weekly", "Monthly", "Yearly")

    // date picker dialog
    val calendar = Calendar.getInstance()
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightPink),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // event input card
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(vertical = 40.dp, horizontal = 25.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
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
                        tint = Color.Black
                    )
                }

                // input field
                TextField(
                    value = eventDesc,
                    onValueChange = { eventDesc = it },
                    placeholder = { Text("Enter a new event...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors()
                )

                Spacer(modifier = Modifier.height(40.dp))

                // calendar and repeat options
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconText(
                        icon = R.drawable.ic_calendar,
                        text = eventDate,
                        onClick = { DatePickerDialog(
                            navController.context,
                            { _, year, month, day ->
                                eventDate = dateFormatter.format(LocalDate.of(year, month + 1, day))
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show() }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        IconText(
                            icon = R.drawable.ic_repeat,
                            text = eventRepeat,
                            onClick = { expandedDropdown = !expandedDropdown}
                        )
                        if (eventRepeat != "Does not repeat") {
                            IconText(
                                icon = R.drawable.ic_calendar,
                                text = eventEndDate,
                                onClick = { DatePickerDialog(
                                    navController.context,
                                    { _, year, month, day ->
                                        eventEndDate = dateFormatter.format(LocalDate.of(year, month + 1, day))
                                    },
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
                                    text = { Text(option) },
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
                        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                        val targetDateCheck = LocalDate.parse(eventDate, formatter)
                        val endDateCheck =
                            if (eventRepeat != "Does not repeat") LocalDate.parse(eventEndDate, formatter)
                            else null

                        if (targetDateCheck.isBefore(LocalDate.now()) || endDateCheck?.isBefore(LocalDate.now()) == true) {
                            Toast.makeText(context, "Please select a future date", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (endDateCheck?.isBefore(targetDateCheck) == true) {
                            Toast.makeText(context, "Please select an end date after target date", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (eventDesc.isBlank() || eventDate.isBlank() ||
                            (eventRepeat != "Does not repeat" && eventEndDate.isBlank())) {
                            Toast.makeText(context, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        // Create an countdown item and add
                        val countdown = CountdownEntity(
                            id = eventIdArg ?: UUID.randomUUID().toString(),
                            targetDate = eventDate,
                            description = eventDesc,
                            repeatOption = eventRepeat,
                            endDate = if (eventRepeat != "Does not repeat") eventEndDate else null
                        )

                        viewModel.apply {
                            viewModelScope.launch {
                                if (eventRepeat == "Does not repeat") {
                                    if (eventIdArg == null) viewModel.insertCountdown(countdown)
                                    else viewModel.updateCountdown(countdown)
                                } else {
                                    val dates = generateRepeatingDates(countdown)
                                    dates.forEach { date ->
                                        Log.d("Repeating dates", "$date")
                                    }

                                    val repeatingCountdowns = dates.map { date ->
                                        countdown.copy(
                                            targetDate = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                                        )
                                    }

                                    val jobs = repeatingCountdowns.map { repeatingCountdown ->
                                        launch {
                                            val existingCountdown =
                                                viewModel.getCountdownByIdAndDate(repeatingCountdown.id,
                                                    repeatingCountdown.targetDate)
                                            if (existingCountdown == null)
                                                viewModel.insertCountdown(repeatingCountdown)
                                            else
                                                viewModel.updateCountdown(repeatingCountdown)
                                        }
                                    }
                                    // ensure repeating events are generated before navigating back
                                    jobs.forEach { it.join() }
                                }
                            }
                        }
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MidPink),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
            }
        }

        // suggestion title
        Text(
            text = "SUGGESTIONS",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 30.dp),
            color = DeepPink
        )

        // suggestion list
        suggestions.forEach { suggestion ->
            SuggestionItem(
                text = suggestion,
                onRemove = { suggestions.remove(suggestion) },
                onClick = { eventDesc = suggestion }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// clickable icon & text for calendar and repeat options
@Composable
fun IconText(icon: Int, text: String, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton( onClick = onClick ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, color = Color.Black,
            modifier = Modifier.clickable { onClick() })
    }
}

@Composable
fun SuggestionItem(text: String, onRemove: () -> Unit, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 25.dp),
        colors = CardDefaults.cardColors(containerColor = MidPink),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, color = Color.White)
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Default.Close,
                    contentDescription = "Remove",
                    tint = Color.White
                )
            }
        }
    }
}

fun generateRepeatingDates(countdown: CountdownEntity): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
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