package com.example.awarify.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awarify.data.entities.Task
import com.example.awarify.data.repository.HobbyRepository
import com.example.awarify.data.repository.TaskRepository
import com.example.awarify.data.repository.HobbySessionRepository
import com.example.awarify.data.repository.TodoRepository
import com.example.awarify.data.entities.Todo
import com.example.awarify.ui.components.DayProgress
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.*

data class HomeUiState(
    val completedTasks: Int = 0,
    val totalTasks: Int = 0,
    val activeHobbies: Int = 0,
    val tasks: List<Task> = emptyList(),
    val todayTodos: List<Todo> = emptyList(),
    val todayTasks: List<TaskUIModel> = emptyList(),
    val hobbies: List<com.example.awarify.data.entities.Hobby> = emptyList(),
    val weeklyProgress: List<DayProgress> = emptyList(),
    val isLoading: Boolean = false
)

data class TaskUIModel(
    val id: Long,
    val title: String,
    val hobbyName: String,
    val scheduledTime: String,
    val isCompleted: Boolean,
    val priority: String
)

class HomeViewModel(
    private val hobbyRepository: HobbyRepository,
    private val taskRepository: TaskRepository,
    private val hobbySessionRepository: HobbySessionRepository,
    private val todoRepository: TodoRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadHomeData()
    }
    
    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time
            
            combine(
                hobbyRepository.getAllActiveHobbies(),
                taskRepository.getTasksByDate(today),
                todoRepository.getTodayTodos()
            ) { hobbies, tasks, todos ->
                val hobbyMap = hobbies.associateBy { it.id }
                val taskUIModels = tasks.map { task ->
                    TaskUIModel(
                        id = task.id,
                        title = task.title,
                        hobbyName = hobbyMap[task.hobbyId]?.name ?: "",
                        scheduledTime = formatScheduledTime(task.scheduledStartTime, task.scheduledEndTime),
                        isCompleted = task.isCompleted,
                        priority = task.priority.name
                    )
                }
                
                // Load weekly progress data
                val weeklyProgress = loadWeeklyProgress()
                
                HomeUiState(
                    completedTasks = tasks.count { it.isCompleted } + todos.count { it.isCompleted },
                    totalTasks = tasks.size + todos.size,
                    activeHobbies = hobbies.size,
                    tasks = tasks,
                    todayTodos = todos,
                    todayTasks = taskUIModels,
                    hobbies = hobbies,
                    weeklyProgress = weeklyProgress,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
    
    private suspend fun loadWeeklyProgress(): List<DayProgress> {
        val calendar = Calendar.getInstance()
        val weeklyData = mutableListOf<DayProgress>()
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        
        // Get last 7 days
        for (i in 6 downTo 0) {
            val dayCalendar = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, -i)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            
            val startDate = dayCalendar.time
            val endDate = Calendar.getInstance().apply {
                time = startDate
                add(Calendar.DAY_OF_MONTH, 1)
            }.time
            
            // Get completed tasks for this day (representing goal completion)
            val tasksForDay = try {
                taskRepository.getTasksInDateRange(startDate, endDate).first()
            } catch (e: Exception) {
                emptyList()
            }
            
            val completedGoals = tasksForDay.count { it.isCompleted }
            val totalGoals = tasksForDay.size
            val goalCompletionPercentage = if (totalGoals > 0) {
                (completedGoals * 100) / totalGoals
            } else {
                0
            }
            
            val dayName = dayFormat.format(startDate)
            
            weeklyData.add(
                DayProgress(
                    dayName = dayName,
                    completionPercentage = goalCompletionPercentage.toFloat()
                )
            )
        }
        
        return weeklyData
    }
    
    fun toggleTaskCompletion(taskId: Long) {
        viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId)
            task?.let {
                val completedAt = if (!it.isCompleted) Date() else null
                taskRepository.updateTaskCompletion(taskId, !it.isCompleted, completedAt)
            }
        }
    }
    
    private fun formatScheduledTime(startTime: String?, endTime: String?): String {
        return when {
            startTime != null && endTime != null -> "$startTime - $endTime"
            startTime != null -> "at $startTime"
            else -> ""
        }
    }
    
    fun addTodo(title: String) {
        if (title.isBlank()) return
        
        viewModelScope.launch {
            val todo = Todo(
                title = title.trim(),
                createdAt = Date(),
                updatedAt = Date()
            )
            todoRepository.insertTodo(todo)
        }
    }
    
    fun toggleTodoCompletion(todoId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            todoRepository.toggleTodoCompletion(todoId, isCompleted)
        }
    }
    
    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
        }
    }
} 