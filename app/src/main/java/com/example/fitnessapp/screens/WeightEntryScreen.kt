package com.example.fitnessapp.screens

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DeleteOutline
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.fitnessapp.R
import com.example.fitnessapp.components.BottomNavigationBar
import com.example.fitnessapp.data.WeightEntryViewModelFactory
import com.example.fitnessapp.ui.theme.LightGray
import com.example.fitnessapp.ui.theme.SmokeGray
import com.example.fitnessapp.utils.formatWeight
import com.example.fitnessapp.viewmodels.WeightEntryViewModel
import java.lang.Double
import java.util.Calendar
import kotlin.String
import kotlin.Unit
import kotlin.let

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UsingMaterialAndMaterial3Libraries")
@Composable
fun WeightEntryScreen(navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: WeightEntryViewModel = viewModel(factory = WeightEntryViewModelFactory(application))

    var dateInput by remember { mutableStateOf("mm/dd/yyyy") }
    var weightInput by remember { mutableStateOf("") }
    var notesInput by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var progressPhotoUri by remember { mutableStateOf("") }

    val saveSuccess = remember { mutableStateOf(false) }
    val invalidInput = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val filePath = getFilePathFromUri(context, it) ?: it.toString()
            progressPhotoUri = filePath
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Weight Entry",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp, top = 40.dp)
            )

            // Date Input
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
            ) {
                Text(
                    text = "Choose Date",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { showDatePicker = true },
                    colors = ButtonDefaults.buttonColors(backgroundColor = SmokeGray),
                    modifier = Modifier.fillMaxWidth().height(60.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = dateInput,
                            textAlign = TextAlign.Start,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weight Input
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
            ) {
                Text(
                    text = "Weight (lbs)",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = weightInput,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { weightInput = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(fontSize = 18.sp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Capture Your Progress Button and Delete Icon Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Capture Your Progress Button
                Button(
                    onClick = {
                        launcher.launch("image/*")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(75.dp)
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults
                        .buttonColors(backgroundColor = Color.Black)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera Icon",
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Capture Your Progress",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                // Trash Icon Button (visible only when an image is selected)
                if (progressPhotoUri.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            progressPhotoUri = "" // Clear the image URI
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Icon",
                            tint = Color.Black,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }

            // Display the selected image
            if (progressPhotoUri.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(progressPhotoUri)
                        .build(),
                    contentDescription = "Progress Photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

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
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val weight = if (weightInput.isEmpty()) 0.0 else Double.parseDouble(weightInput).formatWeight().toDoubleOrNull() ?: 0.0
                        if (weight == 0.0 || dateInput == "mm/dd/yyyy") {
                            invalidInput.value = true
                        } else {
                            invalidInput.value = false
                            viewModel.saveWeightEntry(
                                dateInput,
                                weight,
                                notesInput,
                                progressPhotoUri
                            )

                            saveSuccess.value = true
                            dateInput = "mm/dd/yyyy"
                            weightInput = ""
                            notesInput = ""
                            progressPhotoUri = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                    modifier = Modifier.weight(1f).height(50.dp).padding(end = 8.dp),
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
                        dateInput = "mm/dd/yyyy"
                        weightInput = ""
                        notesInput = ""
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = LightGray),
                    modifier = Modifier.weight(1f).height(50.dp).padding(start = 8.dp),
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

            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                dateInput = date
                viewModel.getEntryByDate(date) { existingEntry ->
                    if (existingEntry != null) {
                        weightInput = existingEntry.weight.formatWeight()
                        notesInput = existingEntry.notes
                        progressPhotoUri = existingEntry.progressPhotoUri ?: ""
                    } else {
                        weightInput = ""
                        notesInput = ""
                        progressPhotoUri = ""
                    }
                }
                showDatePicker = false
            },
            onCancel = {
                showDatePicker = false
            }
        )
    }


    // Toast message on success
    LaunchedEffect(saveSuccess.value) {
        if (saveSuccess.value) {
            Toast.makeText(context, "Entry is saved successfully!", Toast.LENGTH_SHORT).show()
            saveSuccess.value = false
        }
    }

    // Toast when there is an error in input
    LaunchedEffect(invalidInput.value) {
        if (invalidInput.value) {
            Toast.makeText(context, "Please " + (if (dateInput == "mm/dd/yyyy") "select Date" else "enter Weight"), Toast.LENGTH_SHORT).show()
            invalidInput.value = false
        }
    }
}


@Composable
fun DatePickerDialog(
    onDateSelected: (String) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: WeightEntryViewModel = viewModel(factory = WeightEntryViewModelFactory(application))
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        R.style.CustomDatePickerDialogTheme, // Apply the custom theme
        { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = viewModel.formatDate(selectedYear, selectedMonth, selectedDay)
            onDateSelected(selectedDate) // Pass the selected date back
        },
        year,
        month,
        day
    )

    datePickerDialog.setOnCancelListener {
        onCancel()
    }

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