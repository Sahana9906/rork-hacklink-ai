package com.rork.hacklinkai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Canvas = Color(0xFFF7F8FC)
private val Ink = Color(0xFF15152A)
private val Slate = Color(0xFF62677A)
private val Indigo = Color(0xFF5B4CF6)
private val Blue = Color(0xFF2563EB)
private val PaleIndigo = Color(0xFFEDEBFF)
private val Mint = Color(0xFFE7F8F1)
private val Green = Color(0xFF119A69)
private val Amber = Color(0xFFF59E0B)

private val LightColors = lightColorScheme(
    primary = Indigo,
    onPrimary = Color.White,
    primaryContainer = PaleIndigo,
    onPrimaryContainer = Color(0xFF2E247A),
    secondary = Blue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE6F0FF),
    onSecondaryContainer = Color(0xFF123A7A),
    tertiary = Green,
    onTertiary = Color.White,
    tertiaryContainer = Mint,
    onTertiaryContainer = Color(0xFF075C3E),
    background = Canvas,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Color(0xFFF0F1F6),
    onSurfaceVariant = Slate,
    outline = Color(0xFFD9DBE6),
    error = Color(0xFFB3261E),
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFBDB5FF),
    secondary = Color(0xFFA9C7FF),
    tertiary = Color(0xFF8BE7BE),
    background = Color(0xFF10101A),
    surface = Color(0xFF181824),
    onBackground = Color(0xFFF4F2FF),
    onSurface = Color(0xFFF4F2FF)
)

private val AppTypography = Typography()

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}
