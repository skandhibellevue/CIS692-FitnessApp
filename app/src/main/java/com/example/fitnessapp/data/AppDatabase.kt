package com.example.fitnessapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fitnessapp.models.AccountEntity
import com.example.fitnessapp.models.WeightEntry

@Database(entities = [WeightEntry::class, AccountEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weightEntryDao(): WeightEntryDao
    abstract fun accountDao(): AccountDao
}
