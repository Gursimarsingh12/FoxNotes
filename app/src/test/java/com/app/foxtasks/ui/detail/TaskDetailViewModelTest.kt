package com.app.foxtasks.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.foxtasks.core.utils.Resource
import com.app.foxtasks.domain.model.Task
import com.app.foxtasks.domain.usecases.CreateTaskUseCase
import com.app.foxtasks.domain.usecases.DeleteTaskUseCase
import com.app.foxtasks.domain.usecases.GetTaskByIdUseCase
import com.app.foxtasks.domain.usecases.UpdateTaskUseCase
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var viewModel: TaskDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getTaskByIdUseCase = mockk()
        createTaskUseCase = mockk()
        updateTaskUseCase = mockk()
        deleteTaskUseCase = mockk()
        viewModel = TaskDetailViewModel(
            getTaskByIdUseCase,
            createTaskUseCase,
            updateTaskUseCase,
            deleteTaskUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadTask should load task successfully`() = runTest {
        val task = Task(id = 1, title = "Test Task", description = "Test Description")
        
        coEvery { getTaskByIdUseCase(1) } returns flowOf(
            Resource.Loading,
            Resource.Success(task)
        )

        viewModel.loadTask(1)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(task, state.task)
        assertEquals("Test Task", state.title)
        assertEquals("Test Description", state.description)
        assertNull(state.error)
    }

    @Test
    fun `loadTask should handle error`() = runTest {
        val errorMessage = "Task not found"
        
        coEvery { getTaskByIdUseCase(1) } returns flowOf(
            Resource.Loading,
            Resource.Error(errorMessage)
        )

        viewModel.loadTask(1)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `updateTitle should update state`() = runTest {
        viewModel.updateTitle("New Title")

        val state = viewModel.state.value
        assertEquals("New Title", state.title)
    }

    @Test
    fun `updateDescription should update state`() = runTest {
        viewModel.updateDescription("New Description")

        val state = viewModel.state.value
        assertEquals("New Description", state.description)
    }

    @Test
    fun `saveTask should create new task successfully`() = runTest {
        viewModel.updateTitle("New Task")
        viewModel.updateDescription("New Description")

        coEvery { createTaskUseCase("New Task", "New Description") } returns flowOf(
            Resource.Loading,
            Resource.Success(Unit)
        )

        viewModel.saveTask()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.isSaved)
        assertNull(state.error)
    }

    @Test
    fun `saveTask should update existing task successfully`() = runTest {
        val task = Task(id = 1, title = "Old Title", description = "Old Description")
        
        coEvery { getTaskByIdUseCase(1) } returns flowOf(
            Resource.Loading,
            Resource.Success(task)
        )

        viewModel.loadTask(1)
        advanceUntilIdle()

        viewModel.updateTitle("Updated Title")
        viewModel.updateDescription("Updated Description")

        coEvery { updateTaskUseCase(any()) } returns flowOf(
            Resource.Loading,
            Resource.Success(Unit)
        )

        viewModel.saveTask()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.isSaved)
        assertNull(state.error)
    }

    @Test
    fun `saveTask with blank title should show error`() = runTest {
        viewModel.updateTitle("")
        viewModel.updateDescription("Description")

        viewModel.saveTask()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Title cannot be empty", state.error)
        assertFalse(state.isSaved)
    }

    @Test
    fun `deleteTask should delete task successfully`() = runTest {
        val task = Task(id = 1, title = "Task to Delete", description = null)
        
        coEvery { getTaskByIdUseCase(1) } returns flowOf(
            Resource.Loading,
            Resource.Success(task)
        )

        viewModel.loadTask(1)
        advanceUntilIdle()

        coEvery { deleteTaskUseCase(task) } returns flowOf(
            Resource.Loading,
            Resource.Success(Unit)
        )

        viewModel.deleteTask()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.isDeleted)
        assertNull(state.error)
    }

    @Test
    fun `deleteTask with no task should do nothing`() = runTest {
        viewModel.deleteTask()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isDeleted)
    }

    @Test
    fun `resetSavedState should reset isSaved flag`() = runTest {
        viewModel.updateTitle("New Task")
        viewModel.updateDescription("")

        coEvery { createTaskUseCase("New Task", null) } returns flowOf(
            Resource.Loading,
            Resource.Success(Unit)
        )

        viewModel.saveTask()
        advanceUntilIdle()

        val stateAfterSave = viewModel.state.value
        assertTrue(stateAfterSave.isSaved)

        viewModel.resetSavedState()
        advanceUntilIdle()

        val stateAfterReset = viewModel.state.value
        assertFalse(stateAfterReset.isSaved)
    }
}
