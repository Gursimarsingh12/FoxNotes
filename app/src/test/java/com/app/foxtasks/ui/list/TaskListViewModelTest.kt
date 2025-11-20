package com.app.foxtasks.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.foxtasks.core.utils.Resource
import com.app.foxtasks.domain.model.Task
import com.app.foxtasks.domain.usecases.DeleteTaskUseCase
import com.app.foxtasks.domain.usecases.GetAllTasksUseCase
import com.app.foxtasks.domain.usecases.MarkUnmarkCompleteUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getAllTasksUseCase: GetAllTasksUseCase
    private lateinit var markUnmarkCompleteUseCase: MarkUnmarkCompleteUseCase
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var viewModel: TaskListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getAllTasksUseCase = mockk()
        markUnmarkCompleteUseCase = mockk()
        deleteTaskUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load tasks successfully`() = runTest {
        val tasks = listOf(
            Task(id = 1, title = "Task 1", description = null),
            Task(id = 2, title = "Task 2", description = "Description")
        )
        
        coEvery { getAllTasksUseCase() } returns flowOf(
            Resource.Loading,
            Resource.Success(tasks)
        )

        viewModel = TaskListViewModel(getAllTasksUseCase, markUnmarkCompleteUseCase, deleteTaskUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(tasks, state.tasks)
        assertEquals(null, state.error)
    }

    @Test
    fun `init should handle error when loading tasks`() = runTest {
        val errorMessage = "Database error"
        
        coEvery { getAllTasksUseCase() } returns flowOf(
            Resource.Loading,
            Resource.Error(errorMessage)
        )

        viewModel = TaskListViewModel(getAllTasksUseCase, markUnmarkCompleteUseCase, deleteTaskUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.tasks.isEmpty())
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `toggleTaskCompletion should update task and reload list`() = runTest {
        val task = Task(id = 1, title = "Task 1", description = null, isCompleted = false)
        val tasks = listOf(task)
        
        coEvery { getAllTasksUseCase() } returns flowOf(
            Resource.Loading,
            Resource.Success(tasks)
        )
        coEvery { markUnmarkCompleteUseCase(task) } returns flowOf(Resource.Success(Unit))

        viewModel = TaskListViewModel(getAllTasksUseCase, markUnmarkCompleteUseCase, deleteTaskUseCase)
        advanceUntilIdle()

        viewModel.toggleTaskCompletion(task)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
    }

    @Test
    fun `deleteTask should remove task and reload list`() = runTest {
        val task = Task(id = 1, title = "Task 1", description = null)
        val tasks = listOf(task)
        
        coEvery { getAllTasksUseCase() } returns flowOf(
            Resource.Loading,
            Resource.Success(tasks)
        )
        coEvery { deleteTaskUseCase(task) } returns flowOf(Resource.Success(Unit))

        viewModel = TaskListViewModel(getAllTasksUseCase, markUnmarkCompleteUseCase, deleteTaskUseCase)
        advanceUntilIdle()

        viewModel.deleteTask(task)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
    }
}
