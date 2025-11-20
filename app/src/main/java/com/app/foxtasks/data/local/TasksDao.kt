package com.app.foxtasks.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    suspend fun getTasks(): List<TaskEntity>
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Int): TaskEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(note: TaskEntity)
    @Update
    suspend fun updateTask(note: TaskEntity)
    @Delete
    suspend fun deleteTask(note: TaskEntity)
}