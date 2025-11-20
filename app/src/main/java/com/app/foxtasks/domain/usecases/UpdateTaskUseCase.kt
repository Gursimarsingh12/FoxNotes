package com.app.foxtasks.domain.usecases

import com.app.foxtasks.core.utils.Resource
import com.app.foxtasks.core.utils.safeFlow
import com.app.foxtasks.domain.model.Task
import com.app.foxtasks.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow

class UpdateTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(task: Task): Flow<Resource<Unit>> = safeFlow {
        if (task.title.isBlank()){
            throw IllegalArgumentException("Title cannot be empty")
        }
        tasksRepository.updateTask(task)
    }
}