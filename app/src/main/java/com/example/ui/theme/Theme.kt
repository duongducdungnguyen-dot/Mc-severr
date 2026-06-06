package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = AccentBlue,
    secondary = AccentGreen,
    tertiary = AccentPurple,
    background = DarkBg,
    surface = DarkSurface,
    surfaceVariant = BorderDark,
    onPrimary = Color(0xFF003062),
    onSecondary = Color(0xFF00391C),
    onBackground = TextLight,
    onSurface = TextLight,
    onSurfaceVariant = TextSecondary,
    outline = BorderDark,
    error = Color(0xFFF87171)
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force dark theme
  dynamicColor: Boolean = false, // Disable dynamic colors for custom look
  content: @Composable () -> Unit,
) {
  MaterialTheme(
      colorScheme = DarkColorScheme, 
      typography = Typography, 
      content = content
  )
}
