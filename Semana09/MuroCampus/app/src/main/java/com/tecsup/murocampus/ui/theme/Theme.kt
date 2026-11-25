package com.tecsup.murocampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CampusBlue = Color(0xFF1769AA)
private val CampusInk = Color(0xFF172B4D)
private val CampusBackground = Color(0xFFF4F7FC)

private val Colors = lightColorScheme(
    primary = CampusBlue,
    onPrimary = Color.White,
    secondary = Color(0xFF4E83B4),
    background = CampusBackground,
    surface = Color.White,
    onSurface = CampusInk,
    error = Color(0xFFB3261E),
)

@Composable
fun MuroCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Colors, content = content)
}
