package com.app.foxtasks.domain.usecases

import app.cash.turbine.test
import com.app.foxtasks.core.utils.Resource
import com.app.foxtasks.domain.model.Task
import com.app.foxtasks.domain.repository.TasksRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MarkUnmarkCompleteUseCaseTest {

    private lateinit var repository: TasksRepository
    private lateinit var useCase: MarkUnmarkCompleteUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = MarkUnmarkCompleteUseCase(repository)
    }

    @Test
    fun `invoke with incomplete task should mark as complete`() = runTest {
        val task = Task(id = 1, title = "Test Task", description = null, isCompleted = false)
        val completedTask = task.copy(isCompleted = true)
        
        coEvery { repository.updateTask(completedTask) } returns Unit

        useCase(task).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Success)
            awaitComplete()
        }

        coVerify { repository.updateTask(completedTask) }
    }

    @Test
    fun `invoke with complete task should mark as incomplete`() = runTest {
        val task = Task(id = 1, title = "Test Task", description = null, isCompleted = true)
        val incompleteTask = task.copy(isCompleted = false)
        
        coEvery { repository.updateTask(incompleteTask) } returns Unit

        useCase(task).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Success)
            awaitComplete()
        }

        coVerify { repository.updateTask(incompleteTask) }
    }
}
