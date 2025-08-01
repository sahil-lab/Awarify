package com.example.awarify.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.awarify.navigation.NavigationDestinations

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

private val bottomNavItems = listOf(
    BottomNavItem(
        route = NavigationDestinations.HOME.route,
        icon = Icons.Default.Home,
        label = "Home"
    ),
    BottomNavItem(
        route = NavigationDestinations.HOBBIES.route,
        icon = Icons.Default.Favorite,
        label = "Hobbies"
    ),
    BottomNavItem(
        route = NavigationDestinations.CALENDAR.route,
        icon = Icons.Default.DateRange,
        label = "Calendar"
    ),
    BottomNavItem(
        route = NavigationDestinations.TIMETABLE.route,
        icon = Icons.Default.Schedule,
        label = "Timetable"
    ),
    BottomNavItem(
        route = NavigationDestinations.SETTINGS.route,
        icon = Icons.Default.Settings,
        label = "Settings"
    )
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
} 