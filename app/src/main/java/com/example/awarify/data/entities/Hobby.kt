package com.example.awarify.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "hobbies")
data class Hobby(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val colorHex: String = "#2196F3", // Default blue color
    val iconName: String = "palette", // Material icon name
    val isActive: Boolean = true,
    val notificationEnabled: Boolean = true,
    val notificationSound: String? = null,
    val motivationMessage: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val goalDescription: String? = null,
    val dailyGoalMinutes: Int? = null,
    val dailyGoalHours: Int? = null,
    val weeklyGoalMinutes: Int? = null,
    val weeklyGoalHours: Int? = null
) 