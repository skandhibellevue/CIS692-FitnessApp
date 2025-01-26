package com.example.fitnessapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fitnessapp.components.BottomNavigationBar
import com.example.fitnessapp.ui.theme.LightGray
import com.example.fitnessapp.ui.theme.SmokeGray

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Greeting
            Text(
                text = "Hi [Name]",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 16.dp, top = 40.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            StatCard(label = "Current Weight", value = "165", unit = "lbs")
            Spacer(modifier = Modifier.height(16.dp))

            // Target Weight, Days Left
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {

                StatCard(label = "Target Weight", value = "150", unit = "lbs")
                Spacer(modifier = Modifier.width(16.dp))
                StatCard(label = "Days Left", value = "42", unit = "")
            }

            // Capture Your Progress Button
            Button(
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 40.dp),
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
            ) {
                Text(
                    text = "Capture Your Progress",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera Icon",
                    tint = Color.LightGray
                )
            }

            // Week Progress
            Text(
                text = "Week Progress",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("M", "T", "W", "Th", "F", "Sa", "Su").forEach { day ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp, 200.dp)
                                .clip(shape = RoundedCornerShape(25))
                                .background(SmokeGray)
                                .border(width = 2.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(25))
                        )
                        Text(
                            text = day,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, unit: String) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
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

            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 16.dp)
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
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
