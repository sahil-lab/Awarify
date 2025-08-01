package com.example.awarify.ui.screens.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.awarify.ui.components.TimePickerField
import com.example.awarify.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

data class TimeSlot(
    val time: String,
    val hobbyName: String?,
    val duration: String?,
    val colorHex: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(navController: NavController) {
    var selectedDate by remember { mutableStateOf(Date()) }
    val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
    
    // State for managing time slots
    var timeSlots by remember {
        mutableStateOf(
            (6..22).map { hour ->
                TimeSlot(
                    time = String.format("%02d:00", hour),
                    hobbyName = null,
                    duration = null,
                    colorHex = null
                )
            }
        )
    }
    
    // Dialog state
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var selectedTimeSlotIndex by remember { mutableStateOf(-1) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Timetable",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1B1F) // Force dark text
            )
            
            FloatingActionButton(
                onClick = { /* Add time slot */ },
                modifier = Modifier.size(48.dp),
                containerColor = GradientStart,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Time Slot")
            }
        }
        
        // Date selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    val calendar = Calendar.getInstance()
                    calendar.time = selectedDate
                    calendar.add(Calendar.DAY_OF_MONTH, -1)
                    selectedDate = calendar.time
                }) {
                    Icon(
                        Icons.Default.ChevronLeft, 
                        contentDescription = "Previous Day",
                        tint = Color(0xFF1C1B1F)
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dateFormat.format(selectedDate),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C1B1F) // Force dark text
                    )
                    
                    val isToday = Calendar.getInstance().let { today ->
                        val selected = Calendar.getInstance().apply { time = selectedDate }
                        today.get(Calendar.DAY_OF_YEAR) == selected.get(Calendar.DAY_OF_YEAR) &&
                        today.get(Calendar.YEAR) == selected.get(Calendar.YEAR)
                    }
                    
                    if (isToday) {
                        Text(
                            text = "Today",
                            style = MaterialTheme.typography.bodySmall,
                            color = GradientStart // Use app's primary color
                        )
                    }
                }
                
                IconButton(onClick = {
                    val calendar = Calendar.getInstance()
                    calendar.time = selectedDate
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                    selectedDate = calendar.time
                }) {
                    Icon(
                        Icons.Default.ChevronRight, 
                        contentDescription = "Next Day",
                        tint = Color(0xFF1C1B1F)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Quick stats
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("0", "Scheduled", GradientStart)
                StatItem("0m", "Total Time", AccentBlue)
                StatItem("0%", "Completed", AccentGreen)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Time slots
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(timeSlots) { timeSlot ->
                TimeSlotItem(
                    timeSlot = timeSlot,
                    onAddTask = { slot ->
                        selectedTimeSlotIndex = timeSlots.indexOf(slot)
                        showAddTaskDialog = true
                    }
                )
            }
        }
    }
    
    // Add Task Dialog
    if (showAddTaskDialog) {
        AddTaskDialog(
            initialTime = if (selectedTimeSlotIndex >= 0) timeSlots[selectedTimeSlotIndex].time else "12:00",
            onDismiss = { showAddTaskDialog = false },
            onAddTask = { taskName, selectedTime, duration ->
                if (selectedTimeSlotIndex >= 0) {
                    val updatedSlots = timeSlots.toMutableList()
                    updatedSlots[selectedTimeSlotIndex] = TimeSlot(
                        time = selectedTime,
                        hobbyName = taskName,
                        duration = duration,
                        colorHex = "#6366F1" // Default color
                    )
                    timeSlots = updatedSlots
                }
                showAddTaskDialog = false
            }
        )
    }
}

@Composable
private fun StatItem(value: String, label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF49454F) // Force visible text
        )
    }
}

@Composable
private fun TimeSlotItem(
    timeSlot: TimeSlot,
    onAddTask: (TimeSlot) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = if (timeSlot.hobbyName != null) {
            CardDefaults.cardColors(
                containerColor = Color(0xFFF5F5F5) // Light grey background for tasks
            )
        } else {
            CardDefaults.cardColors(containerColor = Color.White)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time
            Text(
                text = timeSlot.time,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF000000), // Pure black for maximum visibility
                modifier = Modifier.width(60.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            if (timeSlot.hobbyName != null) {
                // Color indicator
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            Color(android.graphics.Color.parseColor(timeSlot.colorHex ?: "#000000")),
                            RoundedCornerShape(2.dp)
                        )
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Hobby details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = timeSlot.hobbyName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF000000) // Pure black for maximum visibility
                    )
                    
                    if (timeSlot.duration != null) {
                        Text(
                            text = timeSlot.duration,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF424242), // Darker grey for better visibility
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                // Action button
                IconButton(
                    onClick = { /* Edit or start session */ }
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Start Session",
                        tint = GradientStart
                    )
                }
            } else {
                // Empty slot
                Text(
                    text = "Free time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666), // Darker grey for better visibility
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                
                IconButton(
                    onClick = { onAddTask(timeSlot) }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Task",
                        tint = GradientStart
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    initialTime: String,
    onDismiss: () -> Unit,
    onAddTask: (taskName: String, time: String, duration: String) -> Unit
) {
    var taskName by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf(initialTime) }
    var duration by remember { mutableStateOf("30 minutes") }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                GradientStart.copy(alpha = 0.05f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Text(
                    text = "Add Task to Timetable",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1B1F)
                )
                
                // Task Name Input
                OutlinedTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text("Task Name") },
                    placeholder = { Text("Enter task or activity") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = hobbyTrackerTextFieldColors(),
                    singleLine = true
                )
                
                // Time Selection
                TimePickerField(
                    value = selectedTime,
                    onValueChange = { selectedTime = it },
                    label = "Start Time",
                    placeholder = "12:00",
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Duration Selection
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duration") },
                    placeholder = { Text("30 minutes") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = hobbyTrackerTextFieldColors(),
                    singleLine = true
                )
                
                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF1C1B1F)
                        )
                    ) {
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = {
                            if (taskName.isNotBlank()) {
                                onAddTask(taskName, selectedTime, duration)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GradientStart,
                            contentColor = Color.White
                        ),
                        enabled = taskName.isNotBlank()
                    ) {
                        Text("Add Task")
                    }
                }
            }
        }
    }
} 