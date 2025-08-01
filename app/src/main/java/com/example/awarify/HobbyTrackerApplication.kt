package com.example.awarify

import android.app.Application
import androidx.room.Room
import com.example.awarify.data.database.HobbyTrackerDatabase
import com.example.awarify.data.repository.HobbyRepository
import com.example.awarify.data.repository.TaskRepository
import com.example.awarify.data.repository.NotificationRepository
import com.example.awarify.data.repository.HobbySessionRepository
import com.example.awarify.data.repository.TodoRepository

class HobbyTrackerApplication : Application() {
    
    // Database instance
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            HobbyTrackerDatabase::class.java,
            "hobbytracker_database"
        ).fallbackToDestructiveMigration() // For development - remove in production
        .build()
    }
    
    // Repositories
    val hobbyRepository by lazy { HobbyRepository(database.hobbyDao()) }
    val taskRepository by lazy { TaskRepository(database.taskDao()) }
    val notificationRepository by lazy { NotificationRepository(database.notificationDao()) }
    val hobbySessionRepository by lazy { HobbySessionRepository(database.hobbySessionDao()) }
    val todoRepository by lazy { TodoRepository(database.todoDao()) }
} 