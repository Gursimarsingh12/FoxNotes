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

class CreateTaskUseCaseTest {

    private lateinit var repository: TasksRepository
    private lateinit var useCase: CreateTaskUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = CreateTaskUseCase(repository)
    }

    @Test
    fun `invoke with valid title should emit loading then success`() = runTest {
        val title = "Test Task"
        val description = "Test Description"
        
        coEvery { repository.insertTask(any()) } returns Unit

        useCase(title, description).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Success)
            awaitComplete()
        }

        coVerify { repository.insertTask(any()) }
    }

    @Test
    fun `invoke with blank title should emit loading then error`() = runTest {
        val title = ""
        val description = "Test Description"

        useCase(title, description).test {
            assertTrue(awaitItem() is Resource.Loading)
            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals("Title cannot be blank", (error as Resource.Error).message)
            awaitComplete()
        }

        coVerify(exactly = 0) { repository.insertTask(any()) }
    }

    @Test
    fun `invoke with null description should work`() = runTest {
        val title = "Test Task"
        val description = null
        
        coEvery { repository.insertTask(any()) } returns Unit

        useCase(title, description).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Success)
            awaitComplete()
        }

        coVerify { repository.insertTask(any()) }
    }
}
