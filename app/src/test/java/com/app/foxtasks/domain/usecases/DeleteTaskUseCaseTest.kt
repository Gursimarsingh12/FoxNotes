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

class DeleteTaskUseCaseTest {

    private lateinit var repository: TasksRepository
    private lateinit var useCase: DeleteTaskUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = DeleteTaskUseCase(repository)
    }

    @Test
    fun `invoke should emit loading then success`() = runTest {
        val task = Task(id = 1, title = "Test Task", description = null)
        
        coEvery { repository.deleteTask(task) } returns Unit

        useCase(task).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Success)
            awaitComplete()
        }

        coVerify { repository.deleteTask(task) }
    }

    @Test
    fun `invoke with repository error should emit loading then error`() = runTest {
        val task = Task(id = 1, title = "Test Task", description = null)
        
        coEvery { repository.deleteTask(task) } throws Exception("Database error")

        useCase(task).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }
}
