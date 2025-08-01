package com.example.awarify.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.awarify.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "18:00",
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(value) }
    
    // Sync with external value
    LaunchedEffect(value) {
        textFieldValue = value
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            // Allow manual input with basic validation
            if (newValue.length <= 5) { // HH:MM format
                textFieldValue = newValue
                if (isValidTimeFormat(newValue)) {
                    onValueChange(newValue)
                }
            }
        },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        modifier = modifier.clickable { showDialog = true },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GradientStart,
            focusedLabelColor = GradientStart,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedTextColor = Color(0xFF1C1B1F),
            unfocusedTextColor = Color(0xFF1C1B1F),
            focusedPlaceholderColor = Color(0xFF49454F),
            unfocusedPlaceholderColor = Color(0xFF49454F),
            cursorColor = GradientStart,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        trailingIcon = {
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Select Time",
                    tint = GradientStart
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        readOnly = false
    )

    if (showDialog) {
        TimePickerDialog(
            initialTime = textFieldValue.ifEmpty { "18:00" },
            onTimeSelected = { time ->
                textFieldValue = time
                onValueChange(time)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialTime: String,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header
                Text(
                    text = "Select Time",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Parse initial time
                val (initialHour, initialMinute) = parseTime(initialTime)
                
                // Time Picker
                val timePickerState = rememberTimePickerState(
                    initialHour = initialHour,
                    initialMinute = initialMinute,
                    is24Hour = true
                )

                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Color.White,
                        clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        selectorColor = GradientStart,
                        containerColor = Color.White,
                        periodSelectorBorderColor = GradientStart,
                        periodSelectorSelectedContainerColor = GradientStart,
                        periodSelectorUnselectedContainerColor = Color.Transparent,
                        periodSelectorSelectedContentColor = Color.White,
                        periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        timeSelectorSelectedContainerColor = GradientStart,
                        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        timeSelectorSelectedContentColor = Color.White,
                        timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface
                    )
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
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val selectedTime = String.format(
                                "%02d:%02d",
                                timePickerState.hour,
                                timePickerState.minute
                            )
                            onTimeSelected(selectedTime)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GradientStart,
                            contentColor = Color.White
                        )
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

private fun parseTime(timeString: String): Pair<Int, Int> {
    return try {
        val parts = timeString.split(":")
        if (parts.size == 2) {
            val hour = parts[0].toIntOrNull()?.coerceIn(0, 23) ?: 18
            val minute = parts[1].toIntOrNull()?.coerceIn(0, 59) ?: 0
            hour to minute
        } else {
            18 to 0 // Default time
        }
    } catch (e: Exception) {
        18 to 0 // Default time
    }
}

private fun isValidTimeFormat(time: String): Boolean {
    if (time.isEmpty()) return true
    
    val timeRegex = Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
    return timeRegex.matches(time)
} 