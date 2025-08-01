package com.example.awarify.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "hobby_notifications",
    foreignKeys = [
        ForeignKey(
            entity = Hobby::class,
            parentColumns = ["id"],
            childColumns = ["hobbyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HobbyNotification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hobbyId: Long,
    val title: String,
    val message: String,
    val time: String, // Format: "HH:mm"
    val isEnabled: Boolean = true,
    val notificationType: NotificationType = NotificationType.DAILY,
    val startDate: Date = Date(),
    val endDate: Date? = null,
    val daysOfWeek: String? = null, // Comma-separated: "1,2,3,4,5" for Mon-Fri
    val dayOfMonth: Int? = null, // For monthly notifications
    val lastTriggered: Date? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

enum class NotificationType {
    DAILY,
    WEEKLY,
    MONTHLY,
    CUSTOM_RANGE,
    ONE_TIME
} 