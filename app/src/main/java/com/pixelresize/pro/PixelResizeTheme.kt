package com.pixelresize.pro

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ColorAzul = Color(0xFF010CFF)
val ColorRosa = Color(0xFFD600FF)
val ColorLaranja = Color(0xFFFFA400)
val ColorVerde = Color(0xFF0EFF00)
val ColorFundo = Color(0xFF0A0F1D)
val ColorSuperficie = Color(0xFF131A2E)

private val DarkColorScheme = darkColorScheme(
    primary = ColorRosa,
    secondary = ColorAzul,
    tertiary = ColorLaranja,
    background = ColorFundo,
    surface = ColorSuperficie,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun PixelResizeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
