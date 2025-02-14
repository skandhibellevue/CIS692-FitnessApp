package com.example.fitnessapp.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.FitnessApp
import com.example.fitnessapp.models.WeightEntry
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WeightEntryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = FitnessApp.database
    private val weightEntryDao = database.weightEntryDao()

    val weightEntries = mutableStateListOf<WeightEntry>()

    init {
        fetchWeightEntries()
    }

    // Save a new weight entry to the database
    fun saveWeightEntry(date: String, weight: Double, notes: String, progressPhotoUri: String) {
        viewModelScope.launch {
            val entry = WeightEntry(date = date, weight = weight, notes = notes, progressPhotoUri = progressPhotoUri)
            weightEntryDao.insertWeightEntry(entry)
            fetchWeightEntries()
        }
    }

    // Fetch all weight entries from the database
    private fun fetchWeightEntries() {
        viewModelScope.launch {
            val entries = weightEntryDao.getAllEntries()
            weightEntries.clear()
            weightEntries.addAll(entries)
        }
    }

    // Delete a weight entry from the database
    fun deleteWeightEntry(entry: WeightEntry) {
        viewModelScope.launch {
            weightEntryDao.delete(entry)
            fetchWeightEntries()
        }
    }

    // Helper function to format the selected date
    fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}


