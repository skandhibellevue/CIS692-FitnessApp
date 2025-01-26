package com.example.fitnessapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fitnessapp.R
import com.example.fitnessapp.components.BottomNavigationBar
import com.example.fitnessapp.ui.theme.LightGray
import com.example.fitnessapp.ui.theme.SmokeGray

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AccountScreen(navController: NavHostController) {
    val gender = remember { mutableStateOf("Male") }
    val goalWeight = remember { mutableStateOf("150") } // State for Goal Weight
    val goalDate = remember { mutableStateOf("mm/dd/yyyy") } // State for Goal Date
    val heightFeet = remember { mutableStateOf("6") } // State for Height (feet)
    val heightInches = remember { mutableStateOf("2") } // State for Height (inches)

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Enable vertical scrolling
                .padding(16.dp)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.Start
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

            // Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "[Name]",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
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
                InputField(
                    label = "Goal Weight",
                    value = goalWeight.value,
                    onValueChange = { goalWeight.value = it },
                    unit = "lbs",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                InputField(
                    label = "Goal Date",
                    value = goalDate.value,
                    onValueChange = { goalDate.value = it },
                    unit = "",
                    modifier = Modifier.weight(1f)
                )
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
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                InputField(
                    label = "",
                    value = heightInches.value,
                    onValueChange = { heightInches.value = it },
                    unit = "in",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = { /* Handle Save */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
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
                .padding(vertical = 5.dp)
                .clip(shape = RoundedCornerShape(15))
                .background(SmokeGray)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 16.dp)
                    .padding(start = 10.dp)
            ) {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    modifier = Modifier
                        .weight(1f),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
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