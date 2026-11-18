package com.rau.campusgps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CampusCyan,
    onPrimary = Color(0xFF003542),
    secondary = CampusMint,
    onSecondary = Color(0xFF092F24),
    background = Color(0xFF101D24),
    onBackground = Color(0xFFE5EFF2),
    surface = Color(0xFF172A34),
    onSurface = Color(0xFFE5EFF2),
    surfaceVariant = Color(0xFF263D48),
    onSurfaceVariant = Color(0xFFB7C8CE),
    primaryContainer = Color(0xFF154858),
    onPrimaryContainer = Color(0xFFC8F4FF),
    secondaryContainer = Color(0xFF21463A),
    onSecondaryContainer = Color(0xFFCCF3E1),
    errorContainer = Color(0xFF5B2D33),
    onErrorContainer = Color(0xFFFFDAD9),
    outline = Color(0xFF536B76)
)

private val LightColorScheme = lightColorScheme(
    primary = CampusBlue,
    onPrimary = Color.White,
    secondary = CampusGreen,
    onSecondary = Color.White,
    background = CampusCanvas,
    onBackground = CampusInk,
    surface = Color.White,
    onSurface = CampusInk,
    surfaceVariant = Color(0xFFE8F0F3),
    onSurfaceVariant = CampusMuted,
    primaryContainer = Color(0xFFD5EFF5),
    onPrimaryContainer = Color(0xFF103B4B),
    secondaryContainer = Color(0xFFDDF2E8),
    onSecondaryContainer = Color(0xFF164B35),
    errorContainer = Color(0xFFFCE8E7),
    onErrorContainer = Color(0xFF762B2A),
    outline = CampusOutline
)

@Composable
fun CampusGpsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
