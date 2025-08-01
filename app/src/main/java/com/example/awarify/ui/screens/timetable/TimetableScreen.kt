package com.example.awarify.ui.screens.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
    
    // State for managing time slots by date
    var timetablesByDate by remember { 
        mutableStateOf(mapOf<String, List<TimeSlot>>()) 
    }
    
    // Get current date as key
    val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate)
    
    // Get time slots for current date or create default empty slots
    val timeSlots = timetablesByDate[dateKey] ?: (6..22).map { hour ->
        TimeSlot(
            time = String.format("%02d:00", hour),
            hobbyName = null,
            duration = null,
            colorHex = null
        )
    }
    
    // Dialog state
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var selectedTimeSlotIndex by remember { mutableStateOf(-1) }
    var showMultiDayDialog by remember { mutableStateOf(false) }
    
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
                onClick = { showMultiDayDialog = true },
                modifier = Modifier.size(48.dp),
                containerColor = GradientStart,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Multi-Day Schedule")
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
                    },
                    onEditTask = { slot ->
                        // Allow editing existing tasks
                        selectedTimeSlotIndex = timeSlots.indexOf(slot)
                        showAddTaskDialog = true
                    },
                    onDeleteTask = { slot ->
                        // Clear the task from this slot
                        val slotIndex = timeSlots.indexOf(slot)
                        if (slotIndex >= 0) {
                            val updatedSlots = timeSlots.toMutableList()
                            updatedSlots[slotIndex] = TimeSlot(
                                time = slot.time,
                                hobbyName = null,
                                duration = null,
                                colorHex = null
                            )
                            timetablesByDate = timetablesByDate + (dateKey to updatedSlots)
                        }
                    }
                )
            }
        }
    }
    
    // Add Task Dialog
    if (showAddTaskDialog) {
        AddTaskDialog(
            initialTime = if (selectedTimeSlotIndex >= 0) timeSlots[selectedTimeSlotIndex].time else "12:00",
            initialTaskName = if (selectedTimeSlotIndex >= 0) timeSlots[selectedTimeSlotIndex].hobbyName ?: "" else "",
            initialDuration = if (selectedTimeSlotIndex >= 0) timeSlots[selectedTimeSlotIndex].duration ?: "30 minutes" else "30 minutes",
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
                    // Update the timetable for the current date
                    timetablesByDate = timetablesByDate + (dateKey to updatedSlots)
                }
                showAddTaskDialog = false
            }
        )
    }
    
    // Multi-Day Scheduling Dialog
    if (showMultiDayDialog) {
        MultiDayScheduleDialog(
            currentTimeSlots = timeSlots,
            onDismiss = { showMultiDayDialog = false },
            onDatesSelected = { selectedDates ->
                // Apply current timetable to all selected dates
                val updatedTimetables = timetablesByDate.toMutableMap()
                selectedDates.forEach { date ->
                    val key = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
                    updatedTimetables[key] = timeSlots
                }
                timetablesByDate = updatedTimetables
                showMultiDayDialog = false
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
    onAddTask: (TimeSlot) -> Unit,
    onEditTask: (TimeSlot) -> Unit = {},
    onDeleteTask: (TimeSlot) -> Unit = {}
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
                
                // Action buttons
                Row {
                    IconButton(
                        onClick = { onEditTask(timeSlot) }
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Task",
                            tint = GradientStart
                        )
                    }
                    IconButton(
                        onClick = { onDeleteTask(timeSlot) }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Task",
                            tint = AccentRed
                        )
                    }
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
fun MultiDayScheduleDialog(
    currentTimeSlots: List<TimeSlot>,
    onDismiss: () -> Unit,
    onDatesSelected: (List<Date>) -> Unit
) {
    var selectedDates by remember { mutableStateOf(setOf<Date>()) }
    val calendar = Calendar.getInstance()
    val currentMonth = remember { mutableStateOf(Calendar.getInstance()) }
    
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
                    text = "Apply Schedule to Multiple Days",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1B1F)
                )
                
                Text(
                    text = "Select the dates you want to apply this timetable to:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF49454F)
                )
                
                // Quick Selection Options
                Text(
                    text = "Quick Select:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1C1B1F)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            selectedDates = getWeekdaysInMonth(currentMonth.value)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Weekdays", style = MaterialTheme.typography.bodySmall)
                    }
                    
                    Button(
                        onClick = {
                            selectedDates = getWeekendsInMonth(currentMonth.value)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Weekends", style = MaterialTheme.typography.bodySmall)
                    }
                    
                    Button(
                        onClick = {
                            selectedDates = getAllDatesInMonth(currentMonth.value)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentOrange,
                            contentColor = Color.White
                        )
                    ) {
                        Text("All Days", style = MaterialTheme.typography.bodySmall)
                                         }
                 }
                 
                 // Custom Date Input
                 var customDateText by remember { mutableStateOf("") }
                 Row(
                     modifier = Modifier.fillMaxWidth(),
                     verticalAlignment = Alignment.CenterVertically,
                     horizontalArrangement = Arrangement.spacedBy(8.dp)
                 ) {
                     OutlinedTextField(
                         value = customDateText,
                         onValueChange = { customDateText = it },
                         label = { Text("Custom Date") },
                         placeholder = { Text("YYYY-MM-DD") },
                         modifier = Modifier.weight(1f),
                         shape = RoundedCornerShape(12.dp),
                         colors = hobbyTrackerTextFieldColors(),
                         singleLine = true
                     )
                     
                     Button(
                         onClick = {
                             try {
                                 val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                 val date = dateFormat.parse(customDateText)
                                 if (date != null) {
                                     selectedDates = selectedDates + date
                                     customDateText = ""
                                 }
                             } catch (e: Exception) {
                                 // Invalid date format, ignore
                             }
                         },
                         shape = RoundedCornerShape(12.dp),
                         colors = ButtonDefaults.buttonColors(
                             containerColor = GradientStart,
                             contentColor = Color.White
                         ),
                         enabled = customDateText.isNotEmpty()
                     ) {
                         Text("Add")
                     }
                 }
                 
                 // Clear Selection Button
                 if (selectedDates.isNotEmpty()) {
                     Button(
                         onClick = { selectedDates = emptySet() },
                         modifier = Modifier.fillMaxWidth(),
                         shape = RoundedCornerShape(12.dp),
                         colors = ButtonDefaults.outlinedButtonColors(
                             contentColor = AccentRed
                         )
                     ) {
                         Text("Clear All Selections")
                     }
                 }
                 
                 // Month Navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        currentMonth.value.add(Calendar.MONTH, -1)
                        currentMonth.value = currentMonth.value.clone() as Calendar
                    }) {
                        Icon(
                            Icons.Default.ChevronLeft,
                            contentDescription = "Previous Month",
                            tint = GradientStart
                        )
                    }
                    
                    Text(
                        text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentMonth.value.time),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C1B1F)
                    )
                    
                    IconButton(onClick = {
                        currentMonth.value.add(Calendar.MONTH, 1)
                        currentMonth.value = currentMonth.value.clone() as Calendar
                    }) {
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = "Next Month",
                            tint = GradientStart
                        )
                    }
                }
                
                // Mini Calendar Grid
                CalendarGrid(
                    currentMonth = currentMonth.value,
                    selectedDates = selectedDates,
                    onDateToggle = { date ->
                        selectedDates = if (selectedDates.contains(date)) {
                            selectedDates - date
                        } else {
                            selectedDates + date
                        }
                    }
                )
                
                // Selected dates summary
                if (selectedDates.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = GradientStart.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${selectedDates.size} days selected",
                                style = MaterialTheme.typography.bodyMedium,
                                color = GradientStart,
                                fontWeight = FontWeight.SemiBold
                            )
                            
                            if (selectedDates.size <= 5) {
                                // Show individual dates if 5 or fewer
                                val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
                                Text(
                                    text = selectedDates.sortedBy { it }.joinToString(", ") { 
                                        dateFormat.format(it) 
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF49454F)
                                )
                            } else {
                                // Show date range for more than 5 dates
                                val sortedDates = selectedDates.sorted()
                                val firstDate = sortedDates.first()
                                val lastDate = sortedDates.last()
                                val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
                                Text(
                                    text = "From ${dateFormat.format(firstDate)} to ${dateFormat.format(lastDate)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF49454F)
                                )
                            }
                        }
                    }
                }
                
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
                            if (selectedDates.isNotEmpty()) {
                                onDatesSelected(selectedDates.toList())
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GradientStart,
                            contentColor = Color.White
                        ),
                        enabled = selectedDates.isNotEmpty()
                    ) {
                        Text("Apply Schedule")
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarGrid(
    currentMonth: Calendar,
    selectedDates: Set<Date>,
    onDateToggle: (Date) -> Unit
) {
    val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfMonth = Calendar.getInstance().apply {
        time = currentMonth.time
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val firstDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1
    
    Column {
        // Day headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF49454F),
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Calendar dates
        val weeks = (daysInMonth + firstDayOfWeek + 6) / 7
        repeat(weeks) { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(7) { dayOfWeek ->
                    val dayNumber = week * 7 + dayOfWeek - firstDayOfWeek + 1
                    
                    if (dayNumber in 1..daysInMonth) {
                        val date = Calendar.getInstance().apply {
                            time = currentMonth.time
                            set(Calendar.DAY_OF_MONTH, dayNumber)
                        }.time
                        
                        val isSelected = selectedDates.contains(date)
                        val isToday = Calendar.getInstance().let { today ->
                            val cal = Calendar.getInstance().apply { time = date }
                            today.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR) &&
                            today.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
                        }
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .background(
                                    color = when {
                                        isSelected -> GradientStart
                                        isToday -> GradientStart.copy(alpha = 0.2f)
                                        else -> Color.Transparent
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { onDateToggle(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayNumber.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = when {
                                    isSelected -> Color.White
                                    isToday -> GradientStart
                                    else -> Color(0xFF1C1B1F)
                                },
                                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    initialTime: String,
    initialTaskName: String = "",
    initialDuration: String = "30 minutes",
    onDismiss: () -> Unit,
    onAddTask: (taskName: String, time: String, duration: String) -> Unit
) {
    var taskName by remember { mutableStateOf(initialTaskName) }
    var selectedTime by remember { mutableStateOf(initialTime) }
    var duration by remember { mutableStateOf(initialDuration) }
    
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
                    text = if (initialTaskName.isNotEmpty()) "Edit Task" else "Add Task to Timetable",
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
                        Text(if (initialTaskName.isNotEmpty()) "Update Task" else "Add Task")
                    }
                }
            }
        }
    }
}

// Helper functions for quick date selection
fun getWeekdaysInMonth(month: Calendar): Set<Date> {
    val weekdays = mutableSetOf<Date>()
    val calendar = Calendar.getInstance().apply {
        time = month.time
        set(Calendar.DAY_OF_MONTH, 1)
    }
    
    val daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH)
    
    for (day in 1..daysInMonth) {
        calendar.set(Calendar.DAY_OF_MONTH, day)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        
        // Monday (2) through Friday (6)
        if (dayOfWeek in Calendar.MONDAY..Calendar.FRIDAY) {
            weekdays.add(Date(calendar.timeInMillis))
        }
    }
    
    return weekdays
}

fun getWeekendsInMonth(month: Calendar): Set<Date> {
    val weekends = mutableSetOf<Date>()
    val calendar = Calendar.getInstance().apply {
        time = month.time
        set(Calendar.DAY_OF_MONTH, 1)
    }
    
    val daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH)
    
    for (day in 1..daysInMonth) {
        calendar.set(Calendar.DAY_OF_MONTH, day)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        
        // Saturday (7) and Sunday (1)
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            weekends.add(Date(calendar.timeInMillis))
        }
    }
    
    return weekends
}

fun getAllDatesInMonth(month: Calendar): Set<Date> {
    val allDates = mutableSetOf<Date>()
    val calendar = Calendar.getInstance().apply {
        time = month.time
        set(Calendar.DAY_OF_MONTH, 1)
    }
    
    val daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH)
    
    for (day in 1..daysInMonth) {
        calendar.set(Calendar.DAY_OF_MONTH, day)
        allDates.add(Date(calendar.timeInMillis))
    }
    
    return allDates
} 