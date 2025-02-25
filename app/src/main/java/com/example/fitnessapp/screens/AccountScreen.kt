package com.example.fitnessapp.screens

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.fitnessapp.components.BottomNavigationBar
import com.example.fitnessapp.models.AccountEntity
import com.example.fitnessapp.ui.theme.LightGray
import com.example.fitnessapp.ui.theme.SmokeGray
import com.example.fitnessapp.utils.formatWeight
import com.example.fitnessapp.viewmodels.AccountViewModel

@Composable
fun AccountScreen(navController: NavHostController, viewModel: AccountViewModel = viewModel()) {
    val account by viewModel.accountDetails.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // State for form fields
    var profilePhotoUri by remember { mutableStateOf(account?.profilePhotoUri ?: "") }
    var name by remember { mutableStateOf(account?.name ?: "Your Name") }
    var tmpName by remember { mutableStateOf(name) }
    var gender by remember { mutableStateOf(account?.gender ?: "") }
    var goalWeight by remember { mutableStateOf((account?.goalWeight?.formatWeight() ?: "").toString()) }
    var goalDate by remember { mutableStateOf(account?.goalDate ?: "mm/dd/yyyy") }
    var heightFeet by remember { mutableStateOf(account?.heightFeet?.toString() ?: "") }
    var heightInches by remember { mutableStateOf(account?.heightInches?.toString() ?: "") }
    var maxWeight by remember { mutableStateOf((account?.maxWeight?.formatWeight() ?: "").toString()) }
    var minWeight by remember { mutableStateOf((account?.minWeight?.formatWeight() ?: "").toString()) }

    // State for showing dialogs
    val showNameDialog = remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val invalidName = remember { mutableStateOf(false) }
    val saveSuccess = remember { mutableStateOf(false) }
    val invalidWeight = remember { mutableStateOf(false) }
    val invalidHeight = remember { mutableStateOf(false) }
    val invalidWeekProgressWeights = remember { mutableStateOf(false) }

    // Update fields when account updates
    LaunchedEffect(account) {
        name = account?.name ?: "Your Name"
        gender = account?.gender ?: ""
        goalWeight = (account?.goalWeight?.formatWeight() ?: "").toString()
        goalDate = account?.goalDate ?: "mm/dd/yyyy"
        heightFeet = account?.heightFeet?.toString() ?: ""
        heightInches = account?.heightInches?.toString() ?: ""
        maxWeight = (account?.maxWeight?.formatWeight() ?: "").toString()
        minWeight = (account?.minWeight?.formatWeight() ?: "").toString()
        profilePhotoUri = account?.profilePhotoUri ?: ""
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val filePath = getFilePathFromUri(context, it) ?: it.toString()
            profilePhotoUri = filePath
        }
    }

    val hasChanges by remember {
        derivedStateOf {
            profilePhotoUri != (account?.profilePhotoUri ?: "") ||
                    name != (account?.name ?: "Your Name") ||
                    gender != (account?.gender ?: "") ||
                    goalWeight != (account?.goalWeight?.formatWeight() ?: "").toString() ||
                    goalDate != (account?.goalDate ?: "mm/dd/yyyy") ||
                    heightFeet != (account?.heightFeet?.toString() ?: "") ||
                    heightInches != (account?.heightInches?.toString() ?: "") ||
                    maxWeight != (account?.maxWeight?.formatWeight() ?: "").toString() ||
                    minWeight != (account?.minWeight?.formatWeight() ?: "").toString()
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .background(Color.White),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(50.dp))

                // Profile Image Picker
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (profilePhotoUri.isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(profilePhotoUri)
                                .build(),
                            contentDescription = "Profile Photo",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Add Profile Picture",
                            tint = Color.White,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name and Edit Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            tmpName = name
                            showNameDialog.value = true },
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
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenuComponent(selectedValue = gender) { selected ->
                    gender = selected
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
                        value = goalWeight,
                        onValueChange = { goalWeight = it },
                        unit = "lbs",
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    // Goal Date Input
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .weight(1f)
                            .height(IntrinsicSize.Min)
                    ) {
                        Text(
                            text = "Goal Date",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Left,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { showDatePicker = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SmokeGray),
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
                        value = heightFeet,
                        onValueChange = { heightFeet = it },
                        unit = "ft",
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    InputField(
                        label = "",
                        value = heightInches,
                        onValueChange = { heightInches = it },
                        unit = "in",
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Week Progress
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Week Progress Chart",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Left,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        InputField(
                            label = "Max Weight",
                            value = maxWeight,
                            onValueChange = { maxWeight = it },
                            unit = "lbs",
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        InputField(
                            label = "Min Weight",
                            value = minWeight,
                            onValueChange = { minWeight = it },
                            unit = "lbs",
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Save Button
                if (hasChanges) {
                    Button(
                        onClick = {
                            val enteredWeight = goalWeight.toDoubleOrNull() ?: 0.0
                            val enteredHeightFt = heightFeet.toIntOrNull() ?: 0
                            val enteredHeightInch = heightInches.toIntOrNull() ?: 0
                            val enteredMaxWeight = maxWeight.toDoubleOrNull() ?: 0.0
                            val enteredMinWeight = minWeight.toDoubleOrNull() ?: 0.0

                            if (enteredWeight <= 0.0) {
                                invalidWeight.value = true
                            } else if (enteredHeightFt <= 0 || (heightInches.toIntOrNull() == null || enteredHeightInch < 0)) {
                                invalidHeight.value = true
                            } else if (enteredMaxWeight <= 0 || enteredMinWeight <= 0) {
                                invalidWeekProgressWeights.value = true
                            } else {
                                invalidWeight.value = false
                                invalidHeight.value = false
                                invalidWeekProgressWeights.value = false
                                val updatedAccount = AccountEntity(
                                    id = 1, // Ensure we always update the same user
                                    name = name,
                                    gender = gender,
                                    goalWeight = enteredWeight,
                                    goalDate = goalDate,
                                    heightFeet = enteredHeightFt,
                                    heightInches = enteredHeightInch,
                                    maxWeight = enteredMaxWeight,
                                    minWeight = enteredMinWeight,
                                    profilePhotoUri = profilePhotoUri
                                )
                                viewModel.saveAccountDetails(updatedAccount) // Save to DB
                                saveSuccess.value = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(
                            text = "Save",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }


    // Name Edit Popup
    if (showNameDialog.value) {
        AlertDialog(
            onDismissRequest = { showNameDialog.value = false },
            title = {
                Text(text = "Edit Name", style = MaterialTheme.typography.titleSmall)
            },
            text = {
                Column {
                    TextField(
                        value = if (tmpName == "Your Name") "" else tmpName,
                        onValueChange = { tmpName = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("Enter your name") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (tmpName.isEmpty()) {
                            invalidName.value = true
                        } else {
                            name = tmpName
                            invalidName.value = false
                            showNameDialog.value = false
                        }
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
            containerColor = Color.White
        )
    }

    // Show DatePicker when `showDatePicker` is true
    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                goalDate = date // Update the date input field
                showDatePicker = false // Hide the DatePicker
            },
            onCancel = {
                showDatePicker = false
            }
        )
    }

    // Toast when account details are saved
    LaunchedEffect(saveSuccess.value) {
        if (saveSuccess.value) {
            Toast.makeText(context, "Account details saved successfully!", Toast.LENGTH_SHORT).show()
            saveSuccess.value = false
        }
    }

    // Toast for invalid name
    LaunchedEffect(invalidName.value) {
        if (invalidName.value) {
            Toast.makeText(context, "Please enter valid name", Toast.LENGTH_SHORT).show()
            invalidName.value = false
        }
    }

    // Toast for invalid weights and height
    LaunchedEffect(invalidWeight.value || invalidHeight.value || invalidWeekProgressWeights.value) {
        if (invalidWeight.value) {
            Toast.makeText(context, "Please enter valid goal weight", Toast.LENGTH_SHORT).show()
            invalidWeight.value = false
        } else if (invalidHeight.value) {
            Toast.makeText(context, "Please enter valid height", Toast.LENGTH_SHORT).show()
            invalidHeight.value = false
        } else if (invalidWeekProgressWeights.value) {
            Toast.makeText(context, "Please enter valid week progress weights", Toast.LENGTH_SHORT).show()
            invalidWeekProgressWeights.value = false
        }
    }
}

fun getFilePathFromUri(context: Context, uri: Uri): String? {
    var filePath: String? = null
    val cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            filePath = it.getString(columnIndex)
        }
    }
    return filePath
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
            colors = ButtonDefaults.buttonColors(containerColor = LightGray)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedValue,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
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
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                    onValueChange(item)
                    expanded.value = false
                })
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
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(top = 10.dp)
                .clip(shape = RoundedCornerShape(15))
                .background(LightGray)
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
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                if (unit.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}