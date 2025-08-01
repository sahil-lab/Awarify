package com.example.awarify.data.repository

import com.example.awarify.data.dao.HobbySessionDao
import com.example.awarify.data.entities.HobbySession
import kotlinx.coroutines.flow.Flow
import java.util.Date

class HobbySessionRepository(private val hobbySessionDao: HobbySessionDao) {
    
    fun getSessionsByHobby(hobbyId: Long): Flow<List<HobbySession>> = 
        hobbySessionDao.getSessionsByHobby(hobbyId)
    
    fun getActiveSessions(): Flow<List<HobbySession>> = 
        hobbySessionDao.getActiveSessions()
    
    suspend fun getActiveSessionForHobby(hobbyId: Long): HobbySession? = 
        hobbySessionDao.getActiveSessionForHobby(hobbyId)
    
    fun getSessionsInDateRange(startDate: Date, endDate: Date): Flow<List<HobbySession>> = 
        hobbySessionDao.getSessionsInDateRange(startDate, endDate)
    
    fun getHobbySessionsInDateRange(hobbyId: Long, startDate: Date, endDate: Date): Flow<List<HobbySession>> = 
        hobbySessionDao.getHobbySessionsInDateRange(hobbyId, startDate, endDate)
    
    suspend fun getTotalMinutesForHobbyInRange(hobbyId: Long, startDate: Date, endDate: Date): Int = 
        hobbySessionDao.getTotalMinutesForHobbyInRange(hobbyId, startDate, endDate) ?: 0
    
    suspend fun getTotalMinutesInRange(startDate: Date, endDate: Date): Int = 
        hobbySessionDao.getTotalMinutesInRange(startDate, endDate) ?: 0
    
    suspend fun insertSession(session: HobbySession): Long = 
        hobbySessionDao.insertSession(session)
    
    suspend fun updateSession(session: HobbySession) = 
        hobbySessionDao.updateSession(session)
    
    suspend fun deleteSession(session: HobbySession) = 
        hobbySessionDao.deleteSession(session)
    
    suspend fun endSession(sessionId: Long, endTime: Date, durationMinutes: Int) = 
        hobbySessionDao.endSession(sessionId, endTime, durationMinutes)
} 