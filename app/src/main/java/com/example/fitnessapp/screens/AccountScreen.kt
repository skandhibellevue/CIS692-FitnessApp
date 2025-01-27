package com.example.fitnessapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fitnessapp.components.BottomNavigationBar
import com.example.fitnessapp.ui.theme.LightGray
import com.example.fitnessapp.ui.theme.SmokeGray
import com.example.fitnessapp.utils.SharedManager

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState", "DefaultLocale")
@Composable
fun AccountScreen(navController: NavHostController) {
    // Initial values from SharedManager
    var initialGender by remember { mutableStateOf(SharedManager.getGender()) }
    var initialGoalWeight by remember { mutableStateOf(String.format("%.0f", SharedManager.getGoalWeight())) }
    var initialGoalDate by remember { mutableStateOf(SharedManager.getGoalDate()) }
    var initialHeightFeet by remember { mutableStateOf(SharedManager.getHeightFeet().toString()) }
    var initialHeightInches by remember { mutableStateOf(SharedManager.getHeightInches().toString()) }
    var initialName by remember { mutableStateOf(SharedManager.getName()) }

    // State for current values
    val gender = remember { mutableStateOf(initialGender) }
    val goalWeight = remember { mutableStateOf(initialGoalWeight) }
    var goalDate by remember { mutableStateOf(initialGoalDate) }
    val heightFeet = remember { mutableStateOf(initialHeightFeet) }
    val heightInches = remember { mutableStateOf(initialHeightInches) }
    val name = remember { mutableStateOf(initialName) }

    // State for showing dialogs
    val showNameDialog = remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Derived state to check if any field has changed
    val hasChanges by derivedStateOf {
        gender.value != initialGender ||
                goalWeight.value != initialGoalWeight ||
                goalDate != initialGoalDate ||
                heightFeet.value != initialHeightFeet ||
                heightInches.value != initialHeightInches ||
                name.value != initialName
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // Profile Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Arrow Down Icon",
                    modifier = Modifier.size(150.dp),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name and Edit Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = name.value,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { showNameDialog.value = true },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Icon",
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Gender Dropdown
            Text(
                text = "Gender",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenuComponent(selectedValue = gender.value) { selected ->
                gender.value = selected
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Goal Weight and Date Row
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                // Goal Weight Input
                InputField(
                    label = "Goal Weight",
                    value = goalWeight.value,
                    onValueChange = { goalWeight.value = it },
                    unit = "lbs",
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f) // Equal width
                )
                Spacer(modifier = Modifier.width(16.dp))

                // Goal Date Input
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .weight(1f) // Equal width
                        .height(IntrinsicSize.Min)
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
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = goalDate,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Left,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Height Row
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                InputField(
                    label = "Height",
                    value = heightFeet.value,
                    onValueChange = { heightFeet.value = it },
                    unit = "ft",
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                InputField(
                    label = "",
                    value = heightInches.value,
                    onValueChange = { heightInches.value = it },
                    unit = "in",
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button (only shown if there are changes)
            if (hasChanges) {
                Button(
                    onClick = {
                        // Save data to SharedManager
                        SharedManager.setName(name.value)
                        SharedManager.setGender(gender.value)
                        SharedManager.setGoalWeight(goalWeight.value.toDoubleOrNull() ?: 0.0)
                        SharedManager.setGoalDate(goalDate)
                        SharedManager.setHeightFeet(heightFeet.value.toIntOrNull() ?: 0)
                        SharedManager.setHeightInches(heightInches.value.toIntOrNull() ?: 0)

                        // Update initial values to the new values
                        initialName = name.value
                        initialGender = gender.value
                        initialGoalWeight = goalWeight.value
                        initialGoalDate = goalDate
                        initialHeightFeet = heightFeet.value
                        initialHeightInches = heightInches.value
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text(
                        text = "Save",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    // Name Edit Popup
    if (showNameDialog.value) {
        AlertDialog(
            onDismissRequest = { showNameDialog.value = false },
            title = {
                Text(text = "Edit Name", style = MaterialTheme.typography.h6)
            },
            text = {
                Column {
                    TextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("Enter your name") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showNameDialog.value = false
                    }
                ) {
                    Text("Confirm", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showNameDialog.value = false
                    }
                ) {
                    Text("Cancel", color = Color.Black)
                }
            },
            shape = RoundedCornerShape(12.dp),
            backgroundColor = Color.White
        )
    }

    // Show DatePicker when `showDatePicker` is true
    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                goalDate = date // Update the date input field
                showDatePicker = false // Hide the DatePicker
            },
            onDismiss = { showDatePicker = false } // Hide the DatePicker on dismiss
        )
    }
}

@Composable
fun DropdownMenuComponent(selectedValue: String, onValueChange: (String) -> Unit) {
    val expanded = remember { mutableStateOf(false) }
    val items = listOf("Male", "Female", "Other")

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { expanded.value = !expanded.value },
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(vertical = 10.dp),
            shape = RoundedCornerShape(15),
            colors = ButtonDefaults.buttonColors(backgroundColor = LightGray)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedValue,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Arrow Down Icon",
                    tint = Color.Black
                )
            }
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onValueChange(item)
                    expanded.value = false
                }) {
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    unit: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(top = 10.dp)
                .clip(shape = RoundedCornerShape(15))
                .background(SmokeGray)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 4.dp)
                    .padding(start = 10.dp)
            ) {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    modifier = Modifier
                        .weight(1f),
                    keyboardOptions = keyboardOptions,
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                if (unit.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}