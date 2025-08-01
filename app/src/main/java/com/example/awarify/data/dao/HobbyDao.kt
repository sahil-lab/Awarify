package com.example.awarify.data.dao

import androidx.room.*
import com.example.awarify.data.entities.Hobby
import kotlinx.coroutines.flow.Flow

@Dao
interface HobbyDao {
    
    @Query("SELECT * FROM hobbies WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveHobbies(): Flow<List<Hobby>>
    
    @Query("SELECT * FROM hobbies ORDER BY name ASC")
    fun getAllHobbies(): Flow<List<Hobby>>
    
    @Query("SELECT * FROM hobbies WHERE id = :id")
    suspend fun getHobbyById(id: Long): Hobby?
    
    @Query("SELECT * FROM hobbies WHERE name = :name")
    suspend fun getHobbyByName(name: String): Hobby?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHobby(hobby: Hobby): Long
    
    @Update
    suspend fun updateHobby(hobby: Hobby)
    
    @Delete
    suspend fun deleteHobby(hobby: Hobby)
    
    @Query("UPDATE hobbies SET isActive = :isActive WHERE id = :id")
    suspend fun updateHobbyActiveStatus(id: Long, isActive: Boolean)
    
    @Query("UPDATE hobbies SET notificationEnabled = :enabled WHERE id = :id")
    suspend fun updateNotificationEnabled(id: Long, enabled: Boolean)
} 