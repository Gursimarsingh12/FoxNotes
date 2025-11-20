package com.app.foxtasks.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.foxtasks.core.utils.Resource
import com.app.foxtasks.domain.model.Task
import com.app.foxtasks.domain.usecases.DeleteTaskUseCase
import com.app.foxtasks.domain.usecases.GetAllTasksUseCase
import com.app.foxtasks.domain.usecases.MarkUnmarkCompleteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val markUnmarkCompleteUseCase: MarkUnmarkCompleteUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TaskListUiState())
    val state: StateFlow<TaskListUiState> = _state.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            getAllTasksUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            tasks = resource.data ?: emptyList(),
                            isLoading = false,
                            error = null
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

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            markUnmarkCompleteUseCase(task).collect { resource ->
                when (resource) {
                    is Resource.Success -> loadTasks()
                    is Resource.Error -> {
                        _state.value = _state.value.copy(error = resource.message)
                    }
                    else -> {}
                }
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task).collect { resource ->
                when (resource) {
                    is Resource.Success -> loadTasks()
                    is Resource.Error -> {
                        _state.value = _state.value.copy(error = resource.message)
                    }
                    else -> {}
                }
            }
        }
    }
}
