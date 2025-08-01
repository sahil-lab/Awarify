package com.example.awarify.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.awarify.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    var weeklyReports by remember { mutableStateOf(false) }
    var darkMode by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // App Logo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "HobbyTracker Logo",
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp),
                contentScale = ContentScale.Fit
            )
        }
        
        // Header
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Notifications Section
            item {
                SettingsSection(title = "Notifications") {
                    SettingItem(
                        icon = Icons.Default.Notifications,
                        title = "Enable Notifications",
                        subtitle = "Get reminders for your hobbies",
                        trailing = {
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { notificationsEnabled = it }
                            )
                        }
                    )
                    
                    SettingItem(
                        icon = Icons.Default.Vibration,
                        title = "Vibration",
                        subtitle = "Vibrate when notifications arrive",
                        trailing = {
                            Switch(
                                checked = vibrationEnabled,
                                onCheckedChange = { vibrationEnabled = it }
                            )
                        }
                    )
                }
            }
            
            // Data & Privacy Section
            item {
                SettingsSection(title = "Data & Privacy") {
                    SettingItem(
                        icon = Icons.Default.DataUsage,
                        title = "Export Data",
                        subtitle = "Export your hobby data to CSV",
                        onClick = { /* TODO: Implement export */ }
                    )
                    
                    SettingItem(
                        icon = Icons.Default.Backup,
                        title = "Backup Data",
                        subtitle = "Backup data to device storage",
                        onClick = { /* TODO: Implement backup */ }
                    )
                    
                    SettingItem(
                        icon = Icons.Default.DeleteSweep,
                        title = "Clear All Data",
                        subtitle = "Delete all hobbies and sessions",
                        onClick = { /* TODO: Implement clear data */ },
                        textColor = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            // Reports Section
            item {
                SettingsSection(title = "Reports") {
                    SettingItem(
                        icon = Icons.Default.Assessment,
                        title = "Weekly Reports",
                        subtitle = "Get weekly progress summaries",
                        trailing = {
                            Switch(
                                checked = weeklyReports,
                                onCheckedChange = { weeklyReports = it }
                            )
                        }
                    )
                    
                    SettingItem(
                        icon = Icons.Default.TrendingUp,
                        title = "View Statistics",
                        subtitle = "Detailed analytics and insights",
                        onClick = { /* TODO: Navigate to stats */ }
                    )
                }
            }
            
            // Appearance Section
            item {
                SettingsSection(title = "Appearance") {
                    SettingItem(
                        icon = Icons.Default.DarkMode,
                        title = "Dark Mode",
                        subtitle = "Use dark theme",
                        trailing = {
                            Switch(
                                checked = darkMode,
                                onCheckedChange = { darkMode = it }
                            )
                        }
                    )
                }
            }
            
            // About Section
            item {
                SettingsSection(title = "About") {
                    SettingItem(
                        icon = Icons.Default.Info,
                        title = "App Version",
                        subtitle = "HobbyTracker v1.0.0"
                    )
                    
                    SettingItem(
                        icon = Icons.Default.Help,
                        title = "Help & Support",
                        subtitle = "Get help using the app",
                        onClick = { /* TODO: Show help */ }
                    )
                    
                    SettingItem(
                        icon = Icons.Default.Star,
                        title = "Rate App",
                        subtitle = "Leave a review on the store",
                        onClick = { /* TODO: Open store */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
private fun SettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    textColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    val modifier = if (onClick != null) {
        Modifier.fillMaxWidth()
    } else {
        Modifier.fillMaxWidth()
    }
    
    Surface(
        modifier = modifier,
        onClick = onClick ?: {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            trailing?.invoke()
        }
    }
} 