package com.app.foxtasks.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    onPrimary = Color.White,
    primaryContainer = PrimaryPurpleLight,
    onPrimaryContainer = TextPrimary,
    
    secondary = TextSecondary,
    onSecondary = Color.White,
    
    background = BackgroundWhite,
    onBackground = TextPrimary,
    
    surface = CardBackground,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceGray,
    onSurfaceVariant = TextSecondary,
    
    outline = BorderGray,
    outlineVariant = DividerGray,
    
    error = ErrorRed,
    onError = Color.White,
)

@Composable
fun FoxTasksTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}