package com.example.fitnessapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitnessapp.models.WeightEntry

@Dao
interface WeightEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightEntry(weightEntry: WeightEntry): Long

    @Query("SELECT * FROM weight_entries")
    suspend fun getAllEntries(): List<WeightEntry>

    @Query("SELECT * FROM weight_entries ORDER BY date DESC LIMIT 1")
    suspend fun getLatestEntry(): WeightEntry?

    @Delete
    suspend fun delete(weightEntry: WeightEntry): Int

    @Query("SELECT * FROM weight_entries WHERE date >= :startDate AND date <= :endDate ORDER BY date ASC")
    suspend fun getWeekWeightEntries(startDate: String, endDate: String): List<WeightEntry>
}

