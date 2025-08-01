package com.example.awarify.data.repository

import com.example.awarify.data.dao.TodoDao
import com.example.awarify.data.entities.Todo
import kotlinx.coroutines.flow.Flow
import java.util.*

class TodoRepository(private val todoDao: TodoDao) {
    
    fun getAllTodos(): Flow<List<Todo>> = todoDao.getAllTodos()
    
    fun getTodayTodos(): Flow<List<Todo>> = todoDao.getTodayTodos()
    
    fun getActiveTodos(): Flow<List<Todo>> = todoDao.getActiveTodos()
    
    fun getCompletedTodos(): Flow<List<Todo>> = todoDao.getCompletedTodos()
    
    suspend fun getTodoById(id: Long): Todo? = todoDao.getTodoById(id)
    
    suspend fun insertTodo(todo: Todo): Long = todoDao.insertTodo(todo)
    
    suspend fun updateTodo(todo: Todo) = todoDao.updateTodo(todo)
    
    suspend fun deleteTodo(todo: Todo) = todoDao.deleteTodo(todo)
    
    suspend fun toggleTodoCompletion(id: Long, isCompleted: Boolean) {
        todoDao.updateTodoCompletion(id, isCompleted, Date())
    }
    
    suspend fun deleteCompletedTodos() = todoDao.deleteCompletedTodos()
} 