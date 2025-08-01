package com.example.awarify.data.dao

import androidx.room.*
import com.example.awarify.data.entities.HobbyNotification
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface NotificationDao {
    
    @Query("SELECT * FROM hobby_notifications WHERE hobbyId = :hobbyId ORDER BY time ASC")
    fun getNotificationsByHobby(hobbyId: Long): Flow<List<HobbyNotification>>
    
    @Query("SELECT * FROM hobby_notifications WHERE isEnabled = 1 ORDER BY time ASC")
    fun getAllEnabledNotifications(): Flow<List<HobbyNotification>>
    
    @Query("SELECT * FROM hobby_notifications WHERE id = :id")
    suspend fun getNotificationById(id: Long): HobbyNotification?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: HobbyNotification): Long
    
    @Update
    suspend fun updateNotification(notification: HobbyNotification)
    
    @Delete
    suspend fun deleteNotification(notification: HobbyNotification)
    
    @Query("UPDATE hobby_notifications SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun updateNotificationEnabled(id: Long, isEnabled: Boolean)
    
    @Query("UPDATE hobby_notifications SET lastTriggered = :lastTriggered WHERE id = :id")
    suspend fun updateLastTriggered(id: Long, lastTriggered: Date)
    
    @Query("DELETE FROM hobby_notifications WHERE hobbyId = :hobbyId")
    suspend fun deleteNotificationsByHobby(hobbyId: Long)
    
    @Query("SELECT * FROM hobby_notifications WHERE isEnabled = 1 AND (endDate IS NULL OR endDate >= :currentDate)")
    suspend fun getActiveNotifications(currentDate: Date): List<HobbyNotification>
} 