package com.app.foxtasks.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.foxtasks.data.local.TasksDao
import com.app.foxtasks.data.local.TasksDatabase
import com.app.foxtasks.data.repository.TasksRepositoryImpl
import com.app.foxtasks.domain.repository.TasksRepository
import com.app.foxtasks.domain.usecases.*
import com.app.foxtasks.ui.detail.TaskDetailViewModel
import com.app.foxtasks.ui.list.TaskListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<TasksDatabase> {
        Room.databaseBuilder(
                androidContext(),
                TasksDatabase::class.java,
                "tasks_database"
            ).fallbackToDestructiveMigration(false)
            .build()
    }
    
    single<TasksDao> { get<TasksDatabase>().tasksDao }
    
    single<TasksRepository> { TasksRepositoryImpl(get<TasksDao>()) }
    
    // Use Cases
    single<CreateTaskUseCase> { CreateTaskUseCase(get<TasksRepository>()) }
    single<DeleteTaskUseCase> { DeleteTaskUseCase(get<TasksRepository>()) }
    single<GetTaskByIdUseCase> { GetTaskByIdUseCase(get<TasksRepository>()) }
    single<GetAllTasksUseCase> { GetAllTasksUseCase(get<TasksRepository>()) }
    single<MarkUnmarkCompleteUseCase>{ MarkUnmarkCompleteUseCase(get<TasksRepository>()) }
    single<UpdateTaskUseCase>{ UpdateTaskUseCase(get<TasksRepository>()) }
    
    // ViewModels
    viewModel<TaskListViewModel> { TaskListViewModel(get<GetAllTasksUseCase>(), get<MarkUnmarkCompleteUseCase>(), get<DeleteTaskUseCase>()) }
    viewModel<TaskDetailViewModel> { TaskDetailViewModel(get<GetTaskByIdUseCase>(), get<CreateTaskUseCase>(), get<UpdateTaskUseCase>(), get<DeleteTaskUseCase>()) }
}
