package com.app.foxtasks.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.app.foxtasks.ui.detail.TaskDetailScreen
import com.app.foxtasks.ui.list.TaskListScreen
import com.app.foxtasks.ui.splash.SplashScreen

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Splash
    ) {
        composable<Screens.Splash> {
            SplashScreen(
                onNavigateToTaskList = {
                    navController.navigate(Screens.TaskList) {
                        popUpTo(Screens.Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<Screens.TaskList>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) { backStackEntry ->
            val savedStateHandle = backStackEntry.savedStateHandle
            val shouldRefresh = savedStateHandle.get<Boolean>("refresh") ?: false

            TaskListScreen(
                onNavigateToDetail = { taskId ->
                    navController.navigate(Screens.TaskDetail(taskId))
                },
                shouldRefresh = shouldRefresh,
                onRefreshHandled = {
                    savedStateHandle["refresh"] = false
                }
            )
        }

        composable<Screens.TaskDetail>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) { backStackEntry ->
            val screen = backStackEntry.toRoute<Screens.TaskDetail>()
            TaskDetailScreen(
                taskId = if (screen.taskId == 0) null else screen.taskId,
                onNavigateBack = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("refresh", true)
                    navController.popBackStack()
                }
            )
        }
    }
}
