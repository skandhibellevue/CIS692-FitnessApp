package com.example.fitnessapp

import android.app.Application
import androidx.room.Room
import com.example.fitnessapp.data.AppDatabase

class FitnessApp : Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "fitness_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
