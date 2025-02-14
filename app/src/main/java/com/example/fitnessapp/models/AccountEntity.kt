package com.example.fitnessapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_details")
data class AccountEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val gender: String,
    val goalWeight: Double,
    val goalDate: String,
    val heightFeet: Int,
    val heightInches: Int,
    val profilePhotoUri: String? = null
)
