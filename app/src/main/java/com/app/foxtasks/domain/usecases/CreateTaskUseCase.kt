package com.app.foxtasks.domain.usecases

import com.app.foxtasks.core.utils.Resource
import com.app.foxtasks.core.utils.safeFlow
import com.app.foxtasks.domain.model.Task
import com.app.foxtasks.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow

class CreateTaskUseCase(
    private val tasksRepository: TasksRepository
){
    operator fun invoke(title: String, description: String?): Flow<Resource<Unit>> = safeFlow {
        if (title.isBlank()) {
            throw IllegalArgumentException("Title cannot be blank")
        }
        tasksRepository.insertTask(Task(title = title, description = description))
    }
}

