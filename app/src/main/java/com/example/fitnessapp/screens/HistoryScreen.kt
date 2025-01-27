package com.example.fitnessapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.fitnessapp.components.BottomNavigationBar
import com.example.fitnessapp.ui.theme.SmokeGray
import com.example.fitnessapp.utils.SharedManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "DefaultLocale")
@Composable
fun HistoryScreen(navController: NavHostController) {
    var selectedDate by remember { mutableStateOf("") }
    var selectedWeight by remember { mutableStateOf("No data") }
    var selectedNotes by remember { mutableStateOf("No data") }

    // Fetch data for the selected date when the screen is recomposed or the date changes
    LaunchedEffect(selectedDate) {
        if (selectedDate.isNotEmpty()) {
            val entry = SharedManager.getWeightEntries().find { it.date == selectedDate }
            if (entry != null) {
                selectedWeight = String.format("%.0f", entry.weight)
                selectedNotes = entry.notes
            } else {
                selectedWeight = "No data"
                selectedNotes = "No data"
            }
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
        ) {
            // Header
            Text(
                text = "History",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 16.dp, top = 40.dp)
            )

            // Calendar View
            CalendarView(onDateSelected = { date ->
                selectedDate = date
            })

            Spacer(modifier = Modifier.height(32.dp))

            // Weight Display
            Text(
                text = "Weight (lbs)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(SmokeGray, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = selectedWeight, // Display the selected weight
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Notes Display
            Text(
                text = "Notes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(top = 8.dp)
                    .background(SmokeGray, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = selectedNotes,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CalendarView(onDateSelected: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SmokeGray, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { context ->
                android.widget.CalendarView(context).apply {
                    // Set a listener for date changes
                    setOnDateChangeListener { _, year, month, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, dayOfMonth)
                        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                        val formattedDate = dateFormat.format(calendar.time)
                        onDateSelected(formattedDate)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun PreviewHistoryScreen() {
    HistoryScreen(navController = NavHostController(LocalContext.current))
}