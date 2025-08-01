package com.example.awarify.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "hobby_sessions",
    foreignKeys = [
        ForeignKey(
            entity = Hobby::class,
            parentColumns = ["id"],
            childColumns = ["hobbyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HobbySession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hobbyId: Long,
    val startTime: Date,
    val endTime: Date? = null,
    val durationMinutes: Int = 0,
    val notes: String? = null,
    val isActive: Boolean = true, // True while session is running
    val createdAt: Date = Date()
) 