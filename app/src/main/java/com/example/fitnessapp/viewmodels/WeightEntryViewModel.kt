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

    // Update or save a new weight entry to the database
    fun saveWeightEntry(date: String, weight: Double, notes: String, progressPhotoUri: String) {
        viewModelScope.launch {
            val existingEntry = weightEntryDao.getEntryByDate(date) // Check if entry exists
            if (existingEntry != null) {
                // Update existing entry
                val updatedEntry = existingEntry.copy(
                    weight = weight,
                    notes = notes,
                    progressPhotoUri = progressPhotoUri
                )
                weightEntryDao.updateEntry(updatedEntry)
            } else {
                // Insert new entry
                val newEntry = WeightEntry(
                    date = date,
                    weight = weight,
                    notes = notes,
                    progressPhotoUri = progressPhotoUri
                )
                weightEntryDao.insertEntry(newEntry)
            }
        }
    }

    // Fetch an weight entry by date from the database
    fun getEntryByDate(date: String, onResult: (WeightEntry?) -> Unit) {
        viewModelScope.launch {
            val entry = weightEntryDao.getEntryByDate(date)
            onResult(entry)
        }
    }

    // Fetch all weight entries from the database
    private fun fetchWeightEntries() {
        viewModelScope.launch {
            val entries = weightEntryDao.getAllEntries()
            weightEntries.clear()
            weightEntries.addAll(entries.sortedByDescending { it.date })
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


