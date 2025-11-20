package com.app.foxtasks.domain.repository

import com.app.foxtasks.domain.model.Task

interface TasksRepository {
    suspend fun getTasks(): List<Task>
    suspend fun insertTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun getTaskById(id: Int): Task?
}