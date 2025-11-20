package com.app.foxtasks.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.foxtasks.R
import com.app.foxtasks.ui.theme.BackgroundWhite
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToTaskList: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onNavigateToTaskList()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = "FoxTasks Logo",
            modifier = Modifier.size(200.dp)
        )
    }
}
