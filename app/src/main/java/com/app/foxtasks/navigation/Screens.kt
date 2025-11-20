package com.app.foxtasks.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screens() {
    @Serializable
    data object Splash : Screens()
    @Serializable
    data object TaskList : Screens()
    @Serializable
    data class TaskDetail(val taskId: Int?) : Screens()
}