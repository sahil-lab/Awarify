package com.example.awarify.data.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val priority: TodoPriority = TodoPriority.MEDIUM,
    val dueDate: Date? = null,
    val reminderTime: String? = null,
    val category: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

enum class TodoPriority {
    LOW, MEDIUM, HIGH
} 