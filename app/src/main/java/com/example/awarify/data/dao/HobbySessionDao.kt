package com.example.awarify.data.dao

import androidx.room.*
import com.example.awarify.data.entities.HobbySession
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface HobbySessionDao {
    
    @Query("SELECT * FROM hobby_sessions WHERE hobbyId = :hobbyId ORDER BY startTime DESC")
    fun getSessionsByHobby(hobbyId: Long): Flow<List<HobbySession>>
    
    @Query("SELECT * FROM hobby_sessions WHERE isActive = 1")
    fun getActiveSessions(): Flow<List<HobbySession>>
    
    @Query("SELECT * FROM hobby_sessions WHERE hobbyId = :hobbyId AND isActive = 1 LIMIT 1")
    suspend fun getActiveSessionForHobby(hobbyId: Long): HobbySession?
    
    @Query("SELECT * FROM hobby_sessions WHERE startTime >= :startDate AND startTime <= :endDate ORDER BY startTime ASC")
    fun getSessionsInDateRange(startDate: Date, endDate: Date): Flow<List<HobbySession>>
    
    @Query("SELECT * FROM hobby_sessions WHERE hobbyId = :hobbyId AND startTime >= :startDate AND startTime <= :endDate ORDER BY startTime ASC")
    fun getHobbySessionsInDateRange(hobbyId: Long, startDate: Date, endDate: Date): Flow<List<HobbySession>>
    
    @Query("SELECT SUM(durationMinutes) FROM hobby_sessions WHERE hobbyId = :hobbyId AND startTime >= :startDate AND startTime <= :endDate")
    suspend fun getTotalMinutesForHobbyInRange(hobbyId: Long, startDate: Date, endDate: Date): Int?
    
    @Query("SELECT SUM(durationMinutes) FROM hobby_sessions WHERE startTime >= :startDate AND startTime <= :endDate")
    suspend fun getTotalMinutesInRange(startDate: Date, endDate: Date): Int?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: HobbySession): Long
    
    @Update
    suspend fun updateSession(session: HobbySession)
    
    @Delete
    suspend fun deleteSession(session: HobbySession)
    
    @Query("UPDATE hobby_sessions SET endTime = :endTime, durationMinutes = :durationMinutes, isActive = 0 WHERE id = :sessionId")
    suspend fun endSession(sessionId: Long, endTime: Date, durationMinutes: Int)
} 