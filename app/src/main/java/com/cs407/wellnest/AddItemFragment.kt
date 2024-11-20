package com.cs407.wellnest

import android.app.DatePickerDialog
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
import androidx.navigation.NavController
import com.cs407.wellnest.ui.theme.DeepPink
import com.cs407.wellnest.ui.theme.LightPink
import com.cs407.wellnest.ui.theme.MidPink
import java.util.Calendar

@Composable
fun AddItemFragment(navController: NavController) {
    // input field, date picker, and repeat option default texts
    var eventName by remember { mutableStateOf("") }
    var selectedRepeatOption by remember { mutableStateOf("Does not repeat") }
    var selectedDate by remember { mutableStateOf("Today") }

    // hardcoded suggestion list
    val suggestions = remember {
        mutableStateListOf("Birthday", "Folklore 100 Final", "School Starts")
    }

    // dropdown menu
    var expandedDropdown by remember { mutableStateOf(false) }
    val repeatOptions = listOf("Does not repeat", "Daily", "Weekly", "Monthly", "Yearly")

    // date picker dialog
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        navController.context,
        { _, year, month, day ->
            selectedDate = "$month/$day/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH)
    )

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
                    value = eventName,
                    onValueChange = { eventName = it },
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
                    IconText(icon = R.drawable.ic_calendar, text = selectedDate, onClick = { datePickerDialog.show() })
                    IconText(icon = R.drawable.ic_repeat, text = selectedRepeatOption, onClick = { expandedDropdown = !expandedDropdown})
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
                                        selectedRepeatOption = option
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
                    onClick = { navController.popBackStack() },
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
                onRemove = { suggestions.remove(suggestion) }
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
fun SuggestionItem(text: String, onRemove: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 25.dp),
        colors = CardDefaults.cardColors(containerColor = MidPink)
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