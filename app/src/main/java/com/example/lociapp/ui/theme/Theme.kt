package com.example.lociapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    secondary = Color.White,
    tertiary = Color.White,
    background = Color.Transparent,
    surface = Color.Transparent
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF667eea),
    secondary = Color(0xFF764ba2),
    tertiary = Color(0xFFBB86FC),
    background = Color.Transparent,
    surface = Color.Transparent
)

@Composable
fun LociAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}