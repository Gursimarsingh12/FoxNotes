package com.app.foxtasks.data.repository

import com.app.foxtasks.data.local.TasksDao
import com.app.foxtasks.data.mappers.toDomain
import com.app.foxtasks.data.mappers.toEntity
import com.app.foxtasks.domain.model.Task
import com.app.foxtasks.domain.repository.TasksRepository

class TasksRepositoryImpl(
    private val tasksDao: TasksDao
): TasksRepository {
    override suspend fun getTasks(): List<Task> = tasksDao.getTasks().map { it.toDomain() }

    override suspend fun insertTask(task: Task) = tasksDao.insertTask(note = task.toEntity())

    override suspend fun deleteTask(task: Task) = tasksDao.deleteTask(note = task.toEntity())

    override suspend fun updateTask(task: Task) = tasksDao.updateTask(note = task.toEntity())

    override suspend fun getTaskById(id: Int): Task? = tasksDao.getTaskById(id)?.toDomain()

}