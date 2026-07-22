package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color.Black,
    primaryContainer = ElectricIndigo,
    onPrimaryContainer = Color.White,
    secondary = NeonPurple,
    onSecondary = Color.White,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = TextPrimary,
    tertiary = TikTokCyan,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkSurfaceBorder,
    error = ErrorRed
  )

private val LightColorScheme =
  lightColorScheme(
    primary = ElectricIndigo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8E0FF),
    onPrimaryContainer = ElectricIndigo,
    secondary = NeonPurple,
    onSecondary = Color.White,
    tertiary = TikTokRed,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF64748B),
    outline = Color(0xFFE2E8F0)
  )

@Composable
fun VidSaveTheme(
  darkTheme: Boolean = true, // Default to dark mode for sleek look
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

