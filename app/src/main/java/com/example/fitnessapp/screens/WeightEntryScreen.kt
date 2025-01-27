package com.example.fitnessapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fitnessapp.components.BottomNavigationBar
import com.example.fitnessapp.models.NewWeightEntry
import com.example.fitnessapp.ui.theme.LightGray
import com.example.fitnessapp.ui.theme.SmokeGray
import com.example.fitnessapp.utils.SharedManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WeightEntryScreen(navController: NavHostController) {
    var dateInput by remember { mutableStateOf("mm/dd/yyyy") }
    var weightInput by remember { mutableStateOf("") }
    var notesInput by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Screen Title
            Text(
                text = "Weight Entry",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 16.dp, top = 40.dp)
            )

            // Date Input
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text = "Date",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Left,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        showDatePicker = true
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = SmokeGray),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = dateInput,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Left,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weight Input
            InputField(
                label = "Weight (lbs)",
                value = weightInput,
                onValueChange = { weightInput = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Notes Input
            NotesField(
                label = "Notes",
                value = notesInput,
                onValueChange = { notesInput = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Save and Clear Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val weight = weightInput.toDoubleOrNull() ?: 0.0

                        // Save entry to SharedManager
                        val newEntry = NewWeightEntry(
                            date = dateInput,
                            weight = weight,
                            notes = notesInput
                        )
                        SharedManager.addWeightEntry(newEntry)

                        // Reset fields
                        dateInput = "mm/dd/yyyy"
                        weightInput = ""
                        notesInput = ""
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Save",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Button(
                    onClick = {
                        // Clear fields
                        dateInput = "mm/dd/yyyy"
                        weightInput = ""
                        notesInput = ""
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = LightGray),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .padding(start = 8.dp),
                    shape = RoundedCornerShape(25)
                ) {
                    Text(
                        text = "Clear",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }

    // Show DatePicker when `showDatePicker` is true
    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                dateInput = date
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
fun DatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = formatDate(selectedYear, selectedMonth, selectedDay)
            onDateSelected(selectedDate) // Pass the selected date back
        },
        year,
        month,
        day
    )

    // Show the DatePicker dialog
    LaunchedEffect(Unit) {
        datePickerDialog.show()
    }

    // Dismiss the DatePicker when the composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            datePickerDialog.dismiss()
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onClick: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            textAlign = TextAlign.Left,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(15))
                .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(15))
                .background(SmokeGray, shape = RoundedCornerShape(8.dp)),
            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun NotesField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            textAlign = TextAlign.Left,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(SmokeGray, shape = RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

// Helper function to format the selected date
fun formatDate(year: Int, month: Int, day: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return dateFormat.format(calendar.time)
}