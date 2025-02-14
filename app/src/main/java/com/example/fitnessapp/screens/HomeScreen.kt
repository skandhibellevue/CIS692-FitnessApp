package com.example.fitnessapp.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fitnessapp.components.BottomNavigationBar
import com.example.fitnessapp.ui.theme.SmokeGray
import com.example.fitnessapp.ui.theme.blue
import com.example.fitnessapp.utils.formatWeight
import com.example.fitnessapp.viewmodels.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "DefaultLocale")
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = viewModel()) {
    val name by viewModel.name.collectAsStateWithLifecycle()
    val currentWeight by viewModel.currentWeight.collectAsStateWithLifecycle()
    val goalWeight by viewModel.goalWeight.collectAsStateWithLifecycle()
    val goalDate by viewModel.goalDate.collectAsStateWithLifecycle()
    val heightFt by viewModel.heightFt.collectAsStateWithLifecycle()
    val heightInch by viewModel.heightInch.collectAsStateWithLifecycle()
    val weekWeights by viewModel.weekWeights.collectAsStateWithLifecycle()

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
                text = "Hi $name",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 16.dp, top = 40.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            StatCard(label = "Current Weight", value = String.format("%.0f", currentWeight), unit = "lbs")
            Spacer(modifier = Modifier.height(16.dp))

            // Target Weight, Days Left
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                StatCard(label = "Target Weight", value = goalWeight.formatWeight(), unit = "lbs")
                Spacer(modifier = Modifier.width(16.dp))
                StatCard(label = "Days Left", value = viewModel.getDaysLeft(goalDate).toString(), unit = "")
            }

            // BMI
            Text(
                text = "Your BMI",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 20.dp)
            ) {
                StatCard(
                    label = "",
                    value = viewModel.calculateBMI(currentWeight, heightFt, heightInch).first,
                    unit = ""
                )
                Text(
                    text = viewModel.calculateBMI(currentWeight, heightFt, heightInch).second,
                    color = viewModel.calculateBMI(currentWeight, heightFt, heightInch).third,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            // Week Progress Chart
            WeekProgressChart(weekWeights, goalWeight)
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

@Composable
fun WeekProgressChart(weekWeights: Map<String, Double>, targetWeight: Double) {
    val daysOfWeekLabels = listOf("M", "T", "W", "Th", "F", "S", "Su")
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    // Generate list of dates for the current week (Monday to Sunday)
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    val daysOfWeek = List(7) {
        val date = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_WEEK, 1)
        date
    }

    val maxWeight = 200.0
    val minWeight = 100.0

    val scaleFactor = 200 / (maxWeight - minWeight) // Scale for the bars

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Week Progress",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(end = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column (
                        modifier = Modifier.fillMaxHeight()
                            .padding(top = 30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = maxWeight.formatWeight() + " lbs",
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(160.dp))
                        Text(
                            text = minWeight.formatWeight() + " lbs",
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    for (i in 0 until daysOfWeek.size) {
                        val weight = weekWeights[daysOfWeek[i]]
                        val isFilled = weight != null
                        val heightPercentage = if (isFilled) ((weight
                            ?: minWeight) - minWeight) / (maxWeight - minWeight) else 0.0

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = (weight?.formatWeight() ?: "").toString(),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                color = Color.DarkGray,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(25))
                                    .border(2.dp, Color.Gray, RoundedCornerShape(25))
                                    .background(SmokeGray),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(30.dp)
                                        .fillMaxHeight(heightPercentage.toFloat())
                                        .clip(RoundedCornerShape(25))
                                        .background(blue)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = daysOfWeekLabels[i],
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
}
