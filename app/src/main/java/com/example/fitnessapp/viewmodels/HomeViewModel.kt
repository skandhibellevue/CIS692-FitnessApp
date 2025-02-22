package com.example.fitnessapp.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.FitnessApp
import com.example.fitnessapp.ui.theme.orange
import com.example.fitnessapp.ui.theme.primaryGreen
import com.example.fitnessapp.ui.theme.yellow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = FitnessApp.database
    private val accountDao = database.accountDao()
    private val weightEntryDao = database.weightEntryDao()

    private val _name = MutableStateFlow("User")
    val name: StateFlow<String> = _name

    private val _goalWeight = MutableStateFlow(0.0)
    val goalWeight: StateFlow<Double> = _goalWeight

    private val _goalDate = MutableStateFlow("mm/dd/yyyy")
    val goalDate: StateFlow<String> = _goalDate

    private val _currentWeight = MutableStateFlow(0.0)
    val currentWeight: StateFlow<Double> = _currentWeight

    private val _heightFt = MutableStateFlow(0)
    val heightFt: StateFlow<Int> = _heightFt

    private val _heightInch = MutableStateFlow(0)
    val heightInch: StateFlow<Int> = _heightInch

    private val _maxWeight = MutableStateFlow(0.0)
    val maxWeight: StateFlow<Double> = _maxWeight

    private val _minWeight = MutableStateFlow(0.0)
    val minWeight: StateFlow<Double> = _minWeight

    private val _weekWeights = MutableStateFlow<Map<String, Double>>(emptyMap())
    val weekWeights: StateFlow<Map<String, Double>> = _weekWeights

    private val _targetWeight = MutableStateFlow(0.0)
    val targetWeight: StateFlow<Double> = _targetWeight

    init {
        fetchAccountDetails()
        fetchLatestWeightEntry()
        fetchWeekWeightEntries()
    }

    private fun fetchAccountDetails() {
        viewModelScope.launch {
            val account = accountDao.getAccountDetails()
            if (account != null) {
                _name.value = account.name
                _goalWeight.value = account.goalWeight
                _goalDate.value = account.goalDate
                _heightFt.value = account.heightFeet
                _heightInch.value = account.heightInches
                _maxWeight.value = account.maxWeight
                _minWeight.value = account.minWeight
            }
        }
    }

    private fun fetchLatestWeightEntry() {
        viewModelScope.launch {
            val latestEntry = weightEntryDao.getLatestEntry()
            if (latestEntry != null) {
                _currentWeight.value = latestEntry.weight
            }
        }
    }

    @SuppressLint("NewApi")
    fun getDaysLeft(goalDate: String): Int {
        return try {
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            val goalLocalDate = LocalDate.parse(goalDate, formatter)
            val today = LocalDate.now()
            maxOf(ChronoUnit.DAYS.between(today, goalLocalDate).toInt(), 0)
        } catch (e: Exception) {
            0 // If parsing fails, return 0 days left
        }
    }

    @SuppressLint("DefaultLocale")
    fun calculateBMI(weightLbs: Double, heightFeet: Int, heightInches: Int): Triple<String, String, Color> {
        if (weightLbs <= 0 || (heightFeet == 0 && heightInches == 0)) return Triple("", "", Color.Black)

        val weightKg = weightLbs * 0.453592 // Convert lbs to kg
        val heightMeters = (heightFeet * 0.3048) + (heightInches * 0.0254) // Convert feet & inches to meters

        val bmi = weightKg / (heightMeters * heightMeters)

        // Determine BMI Category
        val status = when {
            bmi < 18.5 -> "Bad (Underweight)"
            bmi in 18.5..24.9 -> "Good (Healthy)"
            bmi in 25.0..29.9 -> "Normal (Overweight)"
            bmi in 30.0..34.9 -> "Bad (Obese)"
            else -> "Bad (Severely Obese)"
        }

        val statusColor = when {
            bmi < 18.5 -> Color.DarkGray
            bmi in 18.5..24.9 -> primaryGreen
            bmi in 25.0..29.9 -> yellow
            bmi in 30.0..34.9 -> orange
            else -> Color.Red
        }

        return Triple(String.format("%.2f", bmi), status, statusColor)
    }

    private fun fetchWeekWeightEntries() {
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            val startDate = dateFormat.format(calendar.time)

            calendar.add(Calendar.DAY_OF_WEEK, 6)
            val endDate = dateFormat.format(calendar.time)

            val weightEntries = weightEntryDao.getWeekWeightEntries(startDate, endDate)
            _weekWeights.value = weightEntries.associate { it.date to it.weight }
        }
    }
}
