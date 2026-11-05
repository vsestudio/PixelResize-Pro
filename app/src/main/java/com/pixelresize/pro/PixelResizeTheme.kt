
package com.pixelresize.pro

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Background = Color(0xFF0D111B)
private val Surface = Color(0xFF171F2E)
private val SurfaceVariant = Color(0xFF202B3D)

private val Primary = Color(0xFF438CFF)
private val Secondary = Color(0xFF8762FF)
private val Success = Color(0xFF31D583)
private val Warning = Color(0xFFFFBD59)
private val Error = Color(0xFFFF6577)

private val TextPrimary = Color(0xFFF4F6FC)
private val TextSecondary = Color(0xFFA5B0C3)

private val PixelResizeColors = darkColorScheme(
    primary = Primary,
    onPrimary = Color.White,

    secondary = Secondary,
    onSecondary = Color.White,

    background = Background,
    onBackground = TextPrimary,

    surface = Surface,
    onSurface = TextPrimary,

    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,

    error = Error,
    onError = Color.White
)

@Composable
fun PixelResizeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PixelResizeColors,
        content = content
    )
}
