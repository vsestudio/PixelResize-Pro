package com.pixelresize.pro

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Background = Color(0xFF171719)
private val Surface = Color(0xFF222225)
private val SurfaceVariant = Color(0xFF303035)

private val Primary = Color(0xFFF07861)
private val Secondary = Color(0xFFE7A45F)
private val Tertiary = Color(0xFFD77CA5)

private val Success = Color(0xFF55C49A)
private val Error = Color(0xFFFF7777)

private val TextPrimary = Color(0xFFF8F4F1)
private val TextSecondary = Color(0xFFB9B1AE)

private val SizeProColors = darkColorScheme(
    primary = Primary,
    onPrimary = Color(0xFF271512),

    secondary = Secondary,
    onSecondary = Color(0xFF291B0D),

    tertiary = Tertiary,
    onTertiary = Color.White,

    background = Background,
    onBackground = TextPrimary,

    surface = Surface,
    onSurface = TextPrimary,

    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,

    error = Error,
    onError = Color.White,

    outline = Color(0xFF625B59)
)

@Composable
fun PixelResizeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SizeProColors,
        content = content
    )
}
