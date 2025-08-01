package com.example.awarify.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import com.example.awarify.data.repository.HobbyRepository
import com.example.awarify.data.repository.TaskRepository
import com.example.awarify.data.repository.NotificationRepository
import com.example.awarify.data.repository.HobbySessionRepository
import com.example.awarify.data.repository.TodoRepository
import com.example.awarify.ui.screens.home.HomeViewModel
import com.example.awarify.ui.screens.hobbies.HobbiesViewModel
import com.example.awarify.ui.screens.hobbies.AddHobbyViewModel

class ViewModelFactory(
    private val hobbyRepository: HobbyRepository,
    private val taskRepository: TaskRepository,
    private val notificationRepository: NotificationRepository,
    private val hobbySessionRepository: HobbySessionRepository,
    private val todoRepository: TodoRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(hobbyRepository, taskRepository, hobbySessionRepository, todoRepository) as T
            }
            modelClass.isAssignableFrom(HobbiesViewModel::class.java) -> {
                HobbiesViewModel(hobbyRepository, taskRepository, notificationRepository, hobbySessionRepository) as T
            }
            modelClass.isAssignableFrom(AddHobbyViewModel::class.java) -> {
                AddHobbyViewModel(hobbyRepository, notificationRepository, context) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 