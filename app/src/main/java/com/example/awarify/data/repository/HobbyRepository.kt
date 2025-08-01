package com.example.awarify.data.repository

import com.example.awarify.data.dao.HobbyDao
import com.example.awarify.data.entities.Hobby
import kotlinx.coroutines.flow.Flow

class HobbyRepository(private val hobbyDao: HobbyDao) {
    
    fun getAllActiveHobbies(): Flow<List<Hobby>> = hobbyDao.getAllActiveHobbies()
    
    fun getAllHobbies(): Flow<List<Hobby>> = hobbyDao.getAllHobbies()
    
    suspend fun getHobbyById(id: Long): Hobby? = hobbyDao.getHobbyById(id)
    
    suspend fun getHobbyByName(name: String): Hobby? = hobbyDao.getHobbyByName(name)
    
    suspend fun insertHobby(hobby: Hobby): Long = hobbyDao.insertHobby(hobby)
    
    suspend fun updateHobby(hobby: Hobby) = hobbyDao.updateHobby(hobby)
    
    suspend fun deleteHobby(hobby: Hobby) = hobbyDao.deleteHobby(hobby)
    
    suspend fun updateHobbyActiveStatus(id: Long, isActive: Boolean) = 
        hobbyDao.updateHobbyActiveStatus(id, isActive)
    
    suspend fun updateNotificationEnabled(id: Long, enabled: Boolean) = 
        hobbyDao.updateNotificationEnabled(id, enabled)
} 