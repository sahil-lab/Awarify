package com.example.awarify.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = Hobby::class,
            parentColumns = ["id"],
            childColumns = ["hobbyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hobbyId: Long,
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val estimatedDurationMinutes: Int? = null,
    val scheduledDate: Date? = null,
    val scheduledStartTime: String? = null, // Format: "HH:mm"
    val scheduledEndTime: String? = null, // Format: "HH:mm"
    val isRecurring: Boolean = false,
    val recurringType: RecurringType? = null,
    val completedAt: Date? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

enum class TaskPriority {
    LOW, MEDIUM, HIGH
}

enum class RecurringType {
    DAILY, WEEKLY, MONTHLY
} 