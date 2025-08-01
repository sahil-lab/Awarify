package com.example.awarify.data.repository

import com.example.awarify.data.dao.NotificationDao
import com.example.awarify.data.entities.HobbyNotification
import kotlinx.coroutines.flow.Flow
import java.util.Date

class NotificationRepository(private val notificationDao: NotificationDao) {
    
    fun getNotificationsByHobby(hobbyId: Long): Flow<List<HobbyNotification>> = 
        notificationDao.getNotificationsByHobby(hobbyId)
    
    fun getAllEnabledNotifications(): Flow<List<HobbyNotification>> = 
        notificationDao.getAllEnabledNotifications()
    
    suspend fun getNotificationById(id: Long): HobbyNotification? = 
        notificationDao.getNotificationById(id)
    
    suspend fun insertNotification(notification: HobbyNotification): Long = 
        notificationDao.insertNotification(notification)
    
    suspend fun updateNotification(notification: HobbyNotification) = 
        notificationDao.updateNotification(notification)
    
    suspend fun deleteNotification(notification: HobbyNotification) = 
        notificationDao.deleteNotification(notification)
    
    suspend fun updateNotificationEnabled(id: Long, isEnabled: Boolean) = 
        notificationDao.updateNotificationEnabled(id, isEnabled)
    
    suspend fun updateLastTriggered(id: Long, lastTriggered: Date) = 
        notificationDao.updateLastTriggered(id, lastTriggered)
    
    suspend fun deleteNotificationsByHobby(hobbyId: Long) = 
        notificationDao.deleteNotificationsByHobby(hobbyId)
    
    suspend fun getActiveNotifications(currentDate: Date): List<HobbyNotification> = 
        notificationDao.getActiveNotifications(currentDate)
} 