package com.example.fitnessapp.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fitnessapp.components.BottomNavigationBar
import com.example.fitnessapp.ui.theme.SmokeGray
import com.example.fitnessapp.viewmodels.HomeViewModel

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
                StatCard(label = "Target Weight", value = String.format("%.0f", goalWeight), unit = "lbs")
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
            StatCard(label = "", value = viewModel.calculateBMI(currentWeight, heightFt, heightInch).first, unit = "")
            Text(
                text = viewModel.calculateBMI(currentWeight, heightFt, heightInch).second,
                color = viewModel.calculateBMI(currentWeight, heightFt, heightInch).third,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
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
