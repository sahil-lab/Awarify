package com.example.awarify.ui.screens.hobbies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awarify.data.repository.HobbyRepository
import com.example.awarify.data.repository.TaskRepository
import com.example.awarify.data.repository.NotificationRepository
import com.example.awarify.data.repository.HobbySessionRepository
import com.example.awarify.data.entities.HobbySession
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

data class HobbiesUiState(
    val hobbies: List<com.example.awarify.data.entities.Hobby> = emptyList(),
    val activeSessions: Map<Long, HobbySession> = emptyMap(),
    val isLoading: Boolean = false
)

data class HobbyUIModel(
    val id: Long,
    val name: String,
    val description: String,
    val colorHex: String,
    val iconName: String,
    val isActive: Boolean,
    val notificationEnabled: Boolean,
    val taskCount: Int,
    val notificationCount: Int,
    val isSessionActive: Boolean = false,
    val currentSessionTime: Long = 0L
)

class HobbiesViewModel(
    private val hobbyRepository: HobbyRepository,
    private val taskRepository: TaskRepository,
    private val notificationRepository: NotificationRepository,
    private val hobbySessionRepository: HobbySessionRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HobbiesUiState())
    val uiState: StateFlow<HobbiesUiState> = _uiState.asStateFlow()
    
    init {
        loadHobbies()
    }
    
    private fun loadHobbies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            combine(
                hobbyRepository.getAllHobbies(),
                hobbySessionRepository.getActiveSessions()
            ) { hobbies, activeSessions ->
                val activeSessionMap = activeSessions.associateBy { it.hobbyId }
                
                HobbiesUiState(
                    hobbies = hobbies,
                    activeSessions = activeSessionMap,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
    
    fun toggleNotifications(hobbyId: Long) {
        viewModelScope.launch {
            val hobby = hobbyRepository.getHobbyById(hobbyId)
            hobby?.let {
                hobbyRepository.updateNotificationEnabled(hobbyId, !it.notificationEnabled)
            }
        }
    }
    
    fun toggleActive(hobbyId: Long) {
        viewModelScope.launch {
            val hobby = hobbyRepository.getHobbyById(hobbyId)
            hobby?.let {
                hobbyRepository.updateHobbyActiveStatus(hobbyId, !it.isActive)
            }
        }
    }
    
    fun startHobbySession(hobbyId: Long) {
        viewModelScope.launch {
            try {
                // Check if there's already an active session
                val existingSession = hobbySessionRepository.getActiveSessionForHobby(hobbyId)
                if (existingSession == null) {
                    // Create new session
                    val session = HobbySession(
                        hobbyId = hobbyId,
                        startTime = Date(),
                        isActive = true
                    )
                    hobbySessionRepository.insertSession(session)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun pauseHobbySession(hobbyId: Long) {
        viewModelScope.launch {
            try {
                val activeSession = hobbySessionRepository.getActiveSessionForHobby(hobbyId)
                activeSession?.let { session ->
                    val currentTime = Date()
                    val durationMinutes = ((currentTime.time - session.startTime.time) / (1000 * 60)).toInt()
                    
                    hobbySessionRepository.endSession(
                        sessionId = session.id,
                        endTime = currentTime,
                        durationMinutes = durationMinutes
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun stopHobbySession(hobbyId: Long) {
        viewModelScope.launch {
            try {
                val activeSession = hobbySessionRepository.getActiveSessionForHobby(hobbyId)
                activeSession?.let { session ->
                    val currentTime = Date()
                    val durationMinutes = ((currentTime.time - session.startTime.time) / (1000 * 60)).toInt()
                    
                    hobbySessionRepository.endSession(
                        sessionId = session.id,
                        endTime = currentTime,
                        durationMinutes = durationMinutes
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun deleteHobby(hobbyId: Long) {
        viewModelScope.launch {
            try {
                val hobby = hobbyRepository.getHobbyById(hobbyId)
                hobby?.let {
                    hobbyRepository.deleteHobby(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
} 