package com.app.foxtasks.ui.list

import com.app.foxtasks.domain.model.Task

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)