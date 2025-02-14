package com.example.fitnessapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_entries")
data class WeightEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val weight: Double,
    val notes: String,
    val progressPhotoUri: String
)