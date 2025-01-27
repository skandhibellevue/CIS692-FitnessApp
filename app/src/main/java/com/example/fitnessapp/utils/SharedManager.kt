package com.example.fitnessapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fitnessapp.models.NewWeightEntry
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


object SharedManager {
    // Account-related data
    private var name = "Name"
    private var gender: String = "Select Gender"
    private var goalWeight: Double = 0.0
    private var goalDate: String = "mm/dd/yyyy"
    private var heightFeet: Int = 0
    private var heightInches: Int = 0
    private val weightEntries = mutableListOf<NewWeightEntry>()
    private var currentWeight = 0.0

    // Setters
    fun setName(newName: String) {
        name = newName
    }

    fun setGender(newGender: String) {
        gender = newGender
    }

    fun setGoalWeight(newGoalWeight: Double) {
        goalWeight = newGoalWeight
    }

    fun setGoalDate(newGoalDate: String) {
        goalDate = newGoalDate
    }

    fun setHeightFeet(newHeightFeet: Int) {
        heightFeet = newHeightFeet
    }

    fun setHeightInches(newHeightInches: Int) {
        heightInches = newHeightInches
    }

    // Add a new weight entry to the list
    fun addWeightEntry(entry: NewWeightEntry) {
        weightEntries.add(entry)
        currentWeight = entry.weight
    }

    // Getters
    fun getName(): String = name
    fun getGender(): String = gender
    fun getGoalWeight(): Double = goalWeight
    fun getGoalDate(): String = goalDate
    fun getHeightFeet(): Int = heightFeet
    fun getHeightInches(): Int = heightInches
    fun getWeightEntries(): List<NewWeightEntry> = weightEntries.toList()
    fun getCurrentWeight(): Double = currentWeight

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDaysLeftFromGoalDate(): Int {
        if (goalDate == "mm/dd/yyyy") { return 0 }
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val currentDate = LocalDate.now()
        val endDate = LocalDate.parse(goalDate, formatter) ?: return  0
        val daysBetween = ChronoUnit.DAYS.between(currentDate, endDate)
        return daysBetween.toInt()
    }
}