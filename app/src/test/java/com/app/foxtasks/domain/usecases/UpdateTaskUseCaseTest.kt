package com.app.foxtasks.domain.usecases

import app.cash.turbine.test
import com.app.foxtasks.core.utils.Resource
import com.app.foxtasks.domain.model.Task
import com.app.foxtasks.domain.repository.TasksRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateTaskUseCaseTest {

    private lateinit var repository: TasksRepository
    private lateinit var useCase: UpdateTaskUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpdateTaskUseCase(repository)
    }

    @Test
    fun `invoke with valid task should emit loading then success`() = runTest {
        val task = Task(id = 1, title = "Updated Task", description = "Updated Description")
        
        coEvery { repository.updateTask(task) } returns Unit

        useCase(task).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Success)
            awaitComplete()
        }

        coVerify { repository.updateTask(task) }
    }

    @Test
    fun `invoke with blank title should emit loading then error`() = runTest {
        val task = Task(id = 1, title = "", description = "Description")

        useCase(task).test {
            assertTrue(awaitItem() is Resource.Loading)
            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals("Title cannot be empty", (error as Resource.Error).message)
            awaitComplete()
        }

        coVerify(exactly = 0) { repository.updateTask(any()) }
    }
}
