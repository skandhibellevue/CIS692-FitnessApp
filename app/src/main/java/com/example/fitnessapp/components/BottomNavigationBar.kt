package com.example.fitnessapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fitnessapp.ui.theme.*
import com.example.fitnessapp.utils.Constants.BottomNavItems

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation(
        modifier = Modifier
            .height(70.dp)
            .border(width = 0.5.dp, color = SmokeGray),
        backgroundColor = Color.White
    ) {
        val currentRoute = navController.currentBackStackEntryAsState()?.value?.destination?.route
        BottomNavItems.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label, fontWeight = FontWeight.Bold) },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                selectedContentColor = primaryGreen,
                unselectedContentColor = Color.Gray
            )
        }
    }
}
