package com.app.foxtasks.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.foxtasks.core.utils.Resource
import com.app.foxtasks.domain.usecases.CreateTaskUseCase
import com.app.foxtasks.domain.usecases.GetTaskByIdUseCase
import com.app.foxtasks.domain.usecases.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskDetailViewModel(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: com.app.foxtasks.domain.usecases.DeleteTaskUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TaskDetailUiState())
    val state: StateFlow<TaskDetailUiState> = _state.asStateFlow()

    fun loadTask(taskId: Int) {
        viewModelScope.launch {
            getTaskByIdUseCase(taskId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                    }
                    is Resource.Success -> {
                        resource.data?.let { task ->
                            _state.value = _state.value.copy(
                                task = task,
                                title = task.title,
                                description = task.description ?: "",
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = resource.message
                        )
                    }
                    is Resource.Idle -> {}
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _state.value = _state.value.copy(title = title)
    }

    fun updateDescription(description: String) {
        _state.value = _state.value.copy(description = description)
    }

    fun saveTask() {
        val currentState = _state.value
        
        if (currentState.title.isBlank()) {
            _state.value = currentState.copy(error = "Title cannot be empty")
            return
        }

        viewModelScope.launch {
            val flow = if (currentState.task != null) {
                // Update existing task
                updateTaskUseCase(
                    currentState.task.copy(
                        title = currentState.title,
                        description = currentState.description.ifBlank { null }
                    )
                )
            } else {
                // Create new task
                createTaskUseCase(
                    title = currentState.title,
                    description = currentState.description.ifBlank { null }
                )
            }

            flow.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = null,
                            isSaved = true
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = resource.message
                        )
                    }
                    is Resource.Idle -> {}
                }
            }
        }
    }

    fun resetSavedState() {
        _state.value = _state.value.copy(isSaved = false)
    }

    fun deleteTask() {
        val currentTask = _state.value.task ?: return
        
        viewModelScope.launch {
            deleteTaskUseCase(currentTask).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = null,
                            isDeleted = true
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = resource.message
                        )
                    }
                    is Resource.Idle -> {}
                }
            }
        }
    }
}
