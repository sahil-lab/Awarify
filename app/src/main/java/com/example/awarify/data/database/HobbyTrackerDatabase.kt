package com.example.awarify.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.awarify.data.entities.Hobby
import com.example.awarify.data.entities.Task
import com.example.awarify.data.entities.HobbyNotification
import com.example.awarify.data.entities.HobbySession
import com.example.awarify.data.entities.Todo
import com.example.awarify.data.dao.HobbyDao
import com.example.awarify.data.dao.TaskDao
import com.example.awarify.data.dao.NotificationDao
import com.example.awarify.data.dao.HobbySessionDao
import com.example.awarify.data.dao.TodoDao
import com.example.awarify.data.converters.DateConverter

@Database(
    entities = [Hobby::class, Task::class, HobbyNotification::class, HobbySession::class, Todo::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class HobbyTrackerDatabase : RoomDatabase() {
    
    abstract fun hobbyDao(): HobbyDao
    abstract fun taskDao(): TaskDao
    abstract fun notificationDao(): NotificationDao
    abstract fun hobbySessionDao(): HobbySessionDao
    abstract fun todoDao(): TodoDao
    
    companion object {
        @Volatile
        private var INSTANCE: HobbyTrackerDatabase? = null
        
        fun getDatabase(context: Context): HobbyTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HobbyTrackerDatabase::class.java,
                    "hobbytracker_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 