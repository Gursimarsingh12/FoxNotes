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

class GetAllTasksUseCaseTest {

    private lateinit var repository: TasksRepository
    private lateinit var useCase: GetAllTasksUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetAllTasksUseCase(repository)
    }

    @Test
    fun `invoke should emit loading then success with tasks`() = runTest {
        val tasks = listOf(
            Task(id = 1, title = "Task 1", description = null),
            Task(id = 2, title = "Task 2", description = "Description")
        )
        
        coEvery { repository.getTasks() } returns tasks

        useCase().test {
            assertTrue(awaitItem() is Resource.Loading)
            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertEquals(tasks, (success as Resource.Success).data)
            awaitComplete()
        }

        coVerify { repository.getTasks() }
    }

    @Test
    fun `invoke should emit loading then success with empty list`() = runTest {
        coEvery { repository.getTasks() } returns emptyList()

        useCase().test {
            assertTrue(awaitItem() is Resource.Loading)
            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertEquals(emptyList<Task>(), (success as Resource.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `invoke with repository error should emit loading then error`() = runTest {
        coEvery { repository.getTasks() } throws Exception("Database error")

        useCase().test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }
}
