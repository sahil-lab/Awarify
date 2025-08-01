package com.example.awarify.ui.screens.hobbies

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.awarify.ui.theme.*
import com.example.awarify.ui.components.TimePickerField
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHobbyScreen(
    navController: NavController,
    viewModel: AddHobbyViewModel,
    hobbyId: Long? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    
    // Load hobby data for editing if hobbyId is provided
    LaunchedEffect(hobbyId) {
        hobbyId?.let { id ->
            if (id > 0) {
                viewModel.loadHobbyForEditing(id)
            }
        }
    }
    
    // Gradient animation
    val infiniteTransition = rememberInfiniteTransition(label = "backgroundAnimation")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradientAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GradientStart.copy(alpha = 0.1f),
                        GradientMiddle.copy(alpha = 0.05f),
                        Color.White
                    ),
                    startY = animatedOffset * 100
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
                            // Top App Bar
                TopAppBarSection(
                    onBack = { navController.popBackStack() },
                    isEditing = hobbyId != null && hobbyId > 0
                )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Header Section
                HeaderSection()
                
                // Basic Information Section
                BasicInfoSection(
                    name = uiState.name,
                    onNameChange = viewModel::updateName,
                    description = uiState.description,
                    onDescriptionChange = viewModel::updateDescription,
                    nameError = if (uiState.nameError) "Hobby name is required" else null
                )
                
                // Visual Customization Section
                VisualCustomizationSection(
                    selectedColor = uiState.colorHex,
                    onColorChange = viewModel::updateColor,
                    selectedIcon = uiState.iconName,
                    onIconChange = viewModel::updateIcon
                )
                
                // Goals Section
                GoalsSection(
                    goalDescription = uiState.goalDescription,
                    onGoalDescriptionChange = viewModel::updateGoalDescription,
                    dailyGoalMinutes = uiState.dailyGoalMinutes,
                    dailyGoalHours = uiState.dailyGoalHours,
                    onDailyGoalChange = viewModel::updateDailyGoal,
                    onDailyGoalHoursChange = viewModel::updateDailyGoalHours,
                    weeklyGoalMinutes = uiState.weeklyGoalMinutes,
                    weeklyGoalHours = uiState.weeklyGoalHours,
                    onWeeklyGoalChange = viewModel::updateWeeklyGoal,
                    onWeeklyGoalHoursChange = viewModel::updateWeeklyGoalHours
                )
                
                // Notifications Section
                NotificationsSection(
                    notificationEnabled = uiState.notificationEnabled,
                    onNotificationToggle = viewModel::toggleNotifications,
                    notificationTime = uiState.notificationTime,
                    onTimeChange = viewModel::updateNotificationTime,
                    notificationMessage = uiState.notificationMessage,
                    onMessageChange = viewModel::updateNotificationMessage
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Save Button
                SaveButton(
                    isEnabled = uiState.name.isNotBlank(),
                    isLoading = uiState.isLoading,
                    onSave = {
                        viewModel.saveHobby()
                        navController.popBackStack()
                    }
                )
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun TopAppBarSection(
    onBack: () -> Unit,
    isEditing: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            GradientStart.copy(alpha = 0.1f),
                            GradientEnd.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = GradientStart.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = GradientStart,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isEditing) "Edit Hobby" else "Create New Hobby",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1B1F) // Force dark text
                )
                Text(
                    text = if (isEditing) "Update your hobby details" else "Set up your new passion",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF49454F) // Force dark text
                )
            }
        }
    }
}

@Composable
private fun HeaderSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = GradientStart.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            GradientStart.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                GradientStart.copy(alpha = 0.2f),
                                GradientStart.copy(alpha = 0.1f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = GradientStart,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Text(
                text = "Let's Get Started!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "Fill in the details below to create your personalized hobby tracker",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun BasicInfoSection(
    name: String,
    onNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    nameError: String?
) {
    SectionCard(title = "Basic Information", icon = Icons.Default.Info) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Hobby Name *") },
                placeholder = { Text("e.g., Guitar Playing, Reading") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GradientStart,
                    focusedLabelColor = GradientStart,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = Color(0xFF1C1B1F), // Force dark text
                    unfocusedTextColor = Color(0xFF1C1B1F), // Force dark text
                    focusedPlaceholderColor = Color(0xFF49454F),
                    unfocusedPlaceholderColor = Color(0xFF49454F),
                    cursorColor = GradientStart,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                isError = nameError != null,
                supportingText = nameError?.let { { Text(it, color = AccentRed) } }
            )
            
            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Description") },
                placeholder = { Text("Tell us about your hobby...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GradientStart,
                    focusedLabelColor = GradientStart,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = Color(0xFF1C1B1F), // Force dark text
                    unfocusedTextColor = Color(0xFF1C1B1F), // Force dark text
                    focusedPlaceholderColor = Color(0xFF49454F),
                    unfocusedPlaceholderColor = Color(0xFF49454F),
                    cursorColor = GradientStart,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                minLines = 3,
                maxLines = 5
            )
        }
    }
}

@Composable
private fun VisualCustomizationSection(
    selectedColor: String,
    onColorChange: (String) -> Unit,
    selectedIcon: String,
    onIconChange: (String) -> Unit
) {
    SectionCard(title = "Visual Style", icon = Icons.Default.Palette) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            // Color Selection
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Choose Color",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(HobbyColors) { color ->
                        ColorOption(
                            color = color,
                            isSelected = selectedColor == String.format("#%06X", 0xFFFFFF and color.hashCode()),
                            onSelect = { 
                                onColorChange(String.format("#%06X", 0xFFFFFF and color.hashCode()))
                            }
                        )
                    }
                }
            }
            
            // Icon Selection
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Choose Icon",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                val icons = listOf(
                    "🎨" to "Art",
                    "🎵" to "Music", 
                    "📚" to "Reading",
                    "💪" to "Fitness",
                    "🧑‍💻" to "Coding",
                    "🍳" to "Cooking",
                    "📸" to "Photography",
                    "✍️" to "Writing"
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(icons) { (emoji, name) ->
                        IconOption(
                            emoji = emoji,
                            name = name,
                            isSelected = selectedIcon == emoji,
                            onSelect = { onIconChange(emoji) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalsSection(
    goalDescription: String,
    onGoalDescriptionChange: (String) -> Unit,
    dailyGoalMinutes: Int,
    dailyGoalHours: Int,
    onDailyGoalChange: (Int) -> Unit,
    onDailyGoalHoursChange: (Int) -> Unit,
    weeklyGoalMinutes: Int,
    weeklyGoalHours: Int,
    onWeeklyGoalChange: (Int) -> Unit,
    onWeeklyGoalHoursChange: (Int) -> Unit
) {
    SectionCard(title = "Goals & Targets", icon = Icons.Default.EmojiEvents) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Goal Description
            OutlinedTextField(
                value = goalDescription,
                onValueChange = onGoalDescriptionChange,
                label = { Text("Goal Description") },
                placeholder = { Text("What do you want to achieve?") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GradientStart,
                    focusedLabelColor = GradientStart,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = Color(0xFF1C1B1F), // Force dark text
                    unfocusedTextColor = Color(0xFF1C1B1F), // Force dark text
                    focusedPlaceholderColor = Color(0xFF49454F),
                    unfocusedPlaceholderColor = Color(0xFF49454F),
                    cursorColor = GradientStart,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                minLines = 2
            )
            
            // Daily Goals
            Text(
                text = "Daily Goal",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = if (dailyGoalHours > 0) dailyGoalHours.toString() else "",
                    onValueChange = { 
                        val hours = it.toIntOrNull() ?: 0
                        onDailyGoalHoursChange(hours)
                    },
                    label = { Text("Hours") },
                    placeholder = { Text("2") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientStart,
                        focusedLabelColor = GradientStart,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedTextColor = Color(0xFF1C1B1F), // Force dark text
                        unfocusedTextColor = Color(0xFF1C1B1F), // Force dark text
                        focusedPlaceholderColor = Color(0xFF49454F),
                        unfocusedPlaceholderColor = Color(0xFF49454F),
                        cursorColor = GradientStart,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                
                OutlinedTextField(
                    value = if (dailyGoalMinutes > 0) dailyGoalMinutes.toString() else "",
                    onValueChange = { 
                        val minutes = it.toIntOrNull() ?: 0
                        onDailyGoalChange(minutes)
                    },
                    label = { Text("Minutes") },
                    placeholder = { Text("30") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientStart,
                        focusedLabelColor = GradientStart,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedTextColor = Color(0xFF1C1B1F), // Force dark text
                        unfocusedTextColor = Color(0xFF1C1B1F), // Force dark text
                        focusedPlaceholderColor = Color(0xFF49454F),
                        unfocusedPlaceholderColor = Color(0xFF49454F),
                        cursorColor = GradientStart,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            
            // Weekly Goals
            Text(
                text = "Weekly Goal",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = if (weeklyGoalHours > 0) weeklyGoalHours.toString() else "",
                    onValueChange = { 
                        val hours = it.toIntOrNull() ?: 0
                        onWeeklyGoalHoursChange(hours)
                    },
                    label = { Text("Hours") },
                    placeholder = { Text("14") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientStart,
                        focusedLabelColor = GradientStart,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedTextColor = Color(0xFF1C1B1F), // Force dark text
                        unfocusedTextColor = Color(0xFF1C1B1F), // Force dark text
                        focusedPlaceholderColor = Color(0xFF49454F),
                        unfocusedPlaceholderColor = Color(0xFF49454F),
                        cursorColor = GradientStart,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                
                OutlinedTextField(
                    value = if (weeklyGoalMinutes > 0) weeklyGoalMinutes.toString() else "",
                    onValueChange = { 
                        val minutes = it.toIntOrNull() ?: 0
                        onWeeklyGoalChange(minutes)
                    },
                    label = { Text("Minutes") },
                    placeholder = { Text("210") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientStart,
                        focusedLabelColor = GradientStart,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedTextColor = Color(0xFF1C1B1F), // Force dark text
                        unfocusedTextColor = Color(0xFF1C1B1F), // Force dark text
                        focusedPlaceholderColor = Color(0xFF49454F),
                        unfocusedPlaceholderColor = Color(0xFF49454F),
                        cursorColor = GradientStart,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}

@Composable
private fun NotificationsSection(
    notificationEnabled: Boolean,
    onNotificationToggle: () -> Unit,
    notificationTime: String,
    onTimeChange: (String) -> Unit,
    notificationMessage: String,
    onMessageChange: (String) -> Unit
) {
    SectionCard(title = "Reminders", icon = Icons.Default.Notifications) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Notification Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Daily Reminders",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Get notified to practice your hobby",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Switch(
                    checked = notificationEnabled,
                    onCheckedChange = { onNotificationToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = GradientStart,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
                    )
                )
            }
            
            // Time Selection and Message (only if notifications enabled)
            AnimatedVisibility(
                visible = notificationEnabled,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    TimePickerField(
                        value = notificationTime,
                        onValueChange = onTimeChange,
                        label = "Reminder Time",
                        placeholder = "18:00",
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = notificationMessage,
                        onValueChange = onMessageChange,
                        label = { Text("Custom Reminder Message") },
                        placeholder = { Text("Don't forget to practice your hobby today! 🎯") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = hobbyTrackerTextFieldColors(),
                        maxLines = 3,
                        supportingText = {
                            Text(
                                text = "Leave blank to use default message",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF49454F) // Force visible dark text
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SaveButton(
    isEnabled: Boolean,
    isLoading: Boolean,
    onSave: () -> Unit
) {
    Button(
        onClick = onSave,
        enabled = isEnabled && !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = GradientStart.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = GradientStart,
            contentColor = Color.White,
            disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Create Hobby",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = GradientStart.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = GradientStart.copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = GradientStart,
                        modifier = Modifier.size(18.dp)
                    )
                }
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            content()
        }
    }
}

@Composable
private fun ColorOption(
    color: Color,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) Color.White else Color.Transparent,
                shape = CircleShape
            )
            .clickable { onSelect() }
            .padding(if (isSelected) 4.dp else 0.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun IconOption(
    emoji: String,
    name: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onSelect() }
            .shadow(
                elevation = if (isSelected) 6.dp else 2.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = if (isSelected) GradientStart.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                GradientStart.copy(alpha = 0.1f) 
            else 
                Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) GradientStart else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
} 