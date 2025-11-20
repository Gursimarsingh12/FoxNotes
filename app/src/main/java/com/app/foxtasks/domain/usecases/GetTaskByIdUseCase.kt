package com.app.foxtasks.domain.usecases

import com.app.foxtasks.core.utils.Resource
import com.app.foxtasks.core.utils.safeFlow
import com.app.foxtasks.domain.model.Task
import com.app.foxtasks.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow

class GetTaskByIdUseCase(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke(id: Int): Flow<Resource<Task?>> = safeFlow {
        tasksRepository.getTaskById(id)
    }
}