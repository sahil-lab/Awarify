package com.example.awarify.data.repository

import com.example.awarify.data.dao.TaskDao
import com.example.awarify.data.entities.Task
import kotlinx.coroutines.flow.Flow
import java.util.Date

class TaskRepository(private val taskDao: TaskDao) {
    
    fun getTasksByHobby(hobbyId: Long): Flow<List<Task>> = taskDao.getTasksByHobby(hobbyId)
    
    fun getTasksByDate(date: Date): Flow<List<Task>> = taskDao.getTasksByDate(date)
    
    fun getTasksInDateRange(startDate: Date, endDate: Date): Flow<List<Task>> = 
        taskDao.getTasksInDateRange(startDate, endDate)
    
    suspend fun getTaskById(id: Long): Task? = taskDao.getTaskById(id)
    
    fun getPendingTasks(date: Date): Flow<List<Task>> = taskDao.getPendingTasks(date)
    
    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)
    
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
    
    suspend fun updateTaskCompletion(id: Long, isCompleted: Boolean, completedAt: Date?) = 
        taskDao.updateTaskCompletion(id, isCompleted, completedAt)
    
    suspend fun deleteTasksByHobby(hobbyId: Long) = taskDao.deleteTasksByHobby(hobbyId)
} 