package com.example.awarify.ui.screens.hobbies

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awarify.data.entities.Hobby
import com.example.awarify.data.entities.HobbyNotification
import com.example.awarify.data.entities.NotificationType
import com.example.awarify.data.repository.HobbyRepository
import com.example.awarify.data.repository.NotificationRepository
import com.example.awarify.notifications.NotificationScheduler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

data class AddHobbyUiState(
    val name: String = "",
    val description: String = "",
    val colorHex: String = "#2196F3",
    val iconName: String = "🎨",
    val dailyGoalMinutes: Int = 0,
    val dailyGoalHours: Int = 0,
    val weeklyGoalMinutes: Int = 0,
    val weeklyGoalHours: Int = 0,
    val goalDescription: String = "",
    val notificationEnabled: Boolean = true,
    val notificationTime: String = "19:00",
    val notificationMessage: String = "",
    val nameError: Boolean = false,
    val isLoading: Boolean = false,
    val editingHobbyId: Long? = null
)

class AddHobbyViewModel(
    private val hobbyRepository: HobbyRepository,
    private val notificationRepository: NotificationRepository,
    private val context: Context
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddHobbyUiState())
    val uiState: StateFlow<AddHobbyUiState> = _uiState.asStateFlow()
    
    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = false
        )
    }
    
    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }
    
    fun updateColor(color: String) {
        _uiState.value = _uiState.value.copy(colorHex = color)
    }
    
    fun updateIcon(icon: String) {
        _uiState.value = _uiState.value.copy(iconName = icon)
    }
    
    fun updateDailyGoal(goal: Int) {
        _uiState.value = _uiState.value.copy(dailyGoalMinutes = goal)
    }
    
    fun updateDailyGoalHours(hours: Int) {
        _uiState.value = _uiState.value.copy(dailyGoalHours = hours)
    }
    
    fun updateWeeklyGoal(goal: Int) {
        _uiState.value = _uiState.value.copy(weeklyGoalMinutes = goal)
    }
    
    fun updateWeeklyGoalHours(hours: Int) {
        _uiState.value = _uiState.value.copy(weeklyGoalHours = hours)
    }
    
    fun updateGoalDescription(description: String) {
        _uiState.value = _uiState.value.copy(goalDescription = description)
    }
    
    fun toggleNotifications() {
        _uiState.value = _uiState.value.copy(
            notificationEnabled = !_uiState.value.notificationEnabled
        )
    }
    
    fun updateNotificationTime(time: String) {
        // Basic validation for time format
        if (time.isEmpty() || time.matches(Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"))) {
            _uiState.value = _uiState.value.copy(notificationTime = time)
        }
    }
    
    fun updateNotificationMessage(message: String) {
        _uiState.value = _uiState.value.copy(notificationMessage = message)
    }
    
    fun loadHobbyForEditing(hobbyId: Long) {
        viewModelScope.launch {
            try {
                val hobby = hobbyRepository.getHobbyById(hobbyId)
                hobby?.let { h ->
                    _uiState.value = _uiState.value.copy(
                        editingHobbyId = hobbyId,
                        name = h.name,
                        description = h.description ?: "",
                        colorHex = h.colorHex,
                        iconName = h.iconName,
                        dailyGoalMinutes = h.dailyGoalMinutes ?: 0,
                        dailyGoalHours = h.dailyGoalHours ?: 0,
                        weeklyGoalMinutes = h.weeklyGoalMinutes ?: 0,
                        weeklyGoalHours = h.weeklyGoalHours ?: 0,
                        goalDescription = h.goalDescription ?: "",
                        notificationEnabled = h.notificationEnabled,
                        // Load notification time and message from notifications
                        notificationTime = "19:00", // TODO: Load from notifications
                        notificationMessage = "" // TODO: Load from notifications
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun saveHobby() {
        saveHobby { /* Success handled in UI */ }
    }
    
    fun saveHobby(onComplete: (Boolean) -> Unit) {
        val currentState = _uiState.value
        
        // Validate input
        if (currentState.name.isBlank()) {
            _uiState.value = currentState.copy(nameError = true)
            onComplete(false)
            return
        }
        
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true)
            
            try {
                if (currentState.editingHobbyId != null) {
                    // Update existing hobby
                    val existingHobby = hobbyRepository.getHobbyById(currentState.editingHobbyId)
                    existingHobby?.let { existing ->
                        val updatedHobby = existing.copy(
                            name = currentState.name.trim(),
                            description = currentState.description.trim().takeIf { it.isNotEmpty() },
                            colorHex = currentState.colorHex,
                            iconName = currentState.iconName,
                            notificationEnabled = currentState.notificationEnabled,
                            goalDescription = currentState.goalDescription.trim().takeIf { it.isNotEmpty() },
                            dailyGoalMinutes = currentState.dailyGoalMinutes,
                            dailyGoalHours = currentState.dailyGoalHours,
                            weeklyGoalMinutes = currentState.weeklyGoalMinutes,
                            weeklyGoalHours = currentState.weeklyGoalHours,
                            updatedAt = Date()
                        )
                        hobbyRepository.updateHobby(updatedHobby)
                    }
                } else {
                    // Create new hobby
                    val hobby = Hobby(
                        name = currentState.name.trim(),
                        description = currentState.description.trim().takeIf { it.isNotEmpty() },
                        colorHex = currentState.colorHex,
                        iconName = currentState.iconName,
                        isActive = true,
                        notificationEnabled = currentState.notificationEnabled,
                        goalDescription = currentState.goalDescription.trim().takeIf { it.isNotEmpty() },
                        dailyGoalMinutes = currentState.dailyGoalMinutes,
                        dailyGoalHours = currentState.dailyGoalHours,
                        weeklyGoalMinutes = currentState.weeklyGoalMinutes,
                        weeklyGoalHours = currentState.weeklyGoalHours,
                        createdAt = Date(),
                        updatedAt = Date()
                    )
                    
                    val hobbyId = hobbyRepository.insertHobby(hobby)
                    
                    // Create default notification if enabled
                    if (currentState.notificationEnabled) {
                        createDefaultNotification(hobbyId, currentState.name, currentState.notificationTime, currentState.notificationMessage)
                    }
                }
                
                _uiState.value = currentState.copy(isLoading = false)
                onComplete(true)
                
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    nameError = true
                )
                onComplete(false)
            }
        }
    }
    
    private suspend fun createDefaultNotification(hobbyId: Long, hobbyName: String, notificationTime: String, customMessage: String) {
        try {
            // Use custom message if provided, otherwise use default
            val message = if (customMessage.isNotBlank()) {
                customMessage
            } else {
                "Don't forget to practice your $hobbyName today! 🎯"
            }
            
            val notification = HobbyNotification(
                hobbyId = hobbyId,
                title = "Time for $hobbyName!",
                message = message,
                time = notificationTime,
                isEnabled = true,
                notificationType = NotificationType.DAILY,
                startDate = Date(),
                endDate = null,
                createdAt = Date(),
                updatedAt = Date()
            )
            
            val notificationId = notificationRepository.insertNotification(notification)
            
            // Schedule the notification
            scheduleDefaultNotification(notificationId, hobbyId, hobbyName, notificationTime, message)
            
        } catch (e: Exception) {
            // Log error but don't fail the hobby creation
            e.printStackTrace()
        }
    }
    
    private fun scheduleDefaultNotification(notificationId: Long, hobbyId: Long, hobbyName: String, notificationTime: String, message: String) {
        try {
            val timeParts = notificationTime.split(":")
            if (timeParts.size != 2) return
            
            val hour = timeParts[0].toIntOrNull() ?: 19
            val minute = timeParts[1].toIntOrNull() ?: 0
            
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                
                // If it's already past the scheduled time today, schedule for tomorrow
                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            
            NotificationScheduler.scheduleNotification(
                context = context,
                notificationId = notificationId,
                title = "Time for $hobbyName!",
                message = message,
                hobbyId = hobbyId,
                triggerTime = calendar.timeInMillis
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
} 