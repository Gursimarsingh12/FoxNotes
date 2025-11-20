package com.app.foxtasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.app.foxtasks.navigation.AppNavGraph
import com.app.foxtasks.ui.theme.FoxTasksTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoxTasksTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}