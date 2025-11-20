package com.app.foxtasks.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TasksDatabase: RoomDatabase() {
    abstract val tasksDao: TasksDao
}