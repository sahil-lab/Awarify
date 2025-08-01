package com.example.awarify.data.dao

import androidx.room.*
import com.example.awarify.data.entities.Task
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TaskDao {
    
    @Query("SELECT * FROM tasks WHERE hobbyId = :hobbyId ORDER BY scheduledDate ASC, scheduledStartTime ASC")
    fun getTasksByHobby(hobbyId: Long): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE scheduledDate = :date ORDER BY scheduledStartTime ASC")
    fun getTasksByDate(date: Date): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE scheduledDate >= :startDate AND scheduledDate <= :endDate ORDER BY scheduledDate ASC, scheduledStartTime ASC")
    fun getTasksInDateRange(startDate: Date, endDate: Date): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): Task?
    
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND scheduledDate <= :date ORDER BY priority DESC, scheduledDate ASC")
    fun getPendingTasks(date: Date): Flow<List<Task>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long
    
    @Update
    suspend fun updateTask(task: Task)
    
    @Delete
    suspend fun deleteTask(task: Task)
    
    @Query("UPDATE tasks SET isCompleted = :isCompleted, completedAt = :completedAt WHERE id = :id")
    suspend fun updateTaskCompletion(id: Long, isCompleted: Boolean, completedAt: Date?)
    
    @Query("DELETE FROM tasks WHERE hobbyId = :hobbyId")
    suspend fun deleteTasksByHobby(hobbyId: Long)
} 