package com.app.foxtasks.ui.detail

import com.app.foxtasks.domain.model.Task

data class TaskDetailUiState(
    val task: Task? = null,
    val title: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false
)