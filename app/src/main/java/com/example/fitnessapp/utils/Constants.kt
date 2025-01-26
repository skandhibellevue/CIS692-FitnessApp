package com.example.fitnessapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
