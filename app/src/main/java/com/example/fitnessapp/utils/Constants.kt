package com.example.fitnessapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

object Constants {
    val BottomNavItems = listOf(
        BottomNavItem("Home", "home", Icons.Filled.Home),
        BottomNavItem("New", "weight_entry", Icons.Filled.Add),
        BottomNavItem("History", "history", Icons.Filled.History),
        BottomNavItem("Account", "account", Icons.Filled.Person)
    )
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

fun Double.formatWeight(): String {
    return if (this % 1.0 == 0.0) {
        this.toInt().toString() // Convert to Int if .0
    } else {
        this.toString() // Keep original if there's a decimal part
    }
}