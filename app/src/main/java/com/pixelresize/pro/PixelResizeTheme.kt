package com.pixelresize.pro

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object SizeProPalette {

    // Fundos
    val Background = Color(0xFF101116)
    val Surface = Color(0xFF191B25)
    val SurfaceVariant = Color(0xFF222633)
    val SurfaceElevated = Color(0xFF1D2230)

    // Cores principais vibrantes
    val Blue = Color(0xFF010CFF)
    val Magenta = Color(0xFFD600FF)
    val Orange = Color(0xFFFFA400)
    val NeonGreen = Color(0xFF0EFF00)

    // Variações para compatibilidade com os restantes ecrãs
    val BlueLight = Color(0xFF5260FF)
    val OrangeLight = Color(0xFFFFBE40)

    val Green = NeonGreen
    val GreenLight = Color(0xFF65FF5C)

    val Pink = Magenta
    val PinkLight = Color(0xFFE45CFF)

    val Violet = Color(0xFF8B35FF)
    val VioletLight = Color(0xFFB477FF)

    // Texto e contornos
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFB9BDCA)
    val Border = Color(0xFF303849)

    // Erros
    val Error = Color(0xFFFF3B4F)
}

private val SizeProColors = darkColorScheme(

    primary = SizeProPalette.Blue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1724A8),
    onPrimaryContainer = Color.White,

    secondary = SizeProPalette.Magenta,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF650080),
    onSecondaryContainer = Color.White,

    tertiary = SizeProPalette.Orange,
    onTertiary = Color(0xFF17120A),
    tertiaryContainer = Color(0xFF704700),
    onTertiaryContainer = Color.White,

    background = SizeProPalette.Background,
    onBackground = SizeProPalette.TextPrimary,

    surface = SizeProPalette.Surface,
    onSurface = SizeProPalette.TextPrimary,

    surfaceVariant = SizeProPalette.SurfaceVariant,
    onSurfaceVariant = SizeProPalette.TextSecondary,

    error = SizeProPalette.Error,
    onError = Color.White,
    errorContainer = Color(0xFF741B2A),
    onErrorContainer = Color.White,

    outline = SizeProPalette.Blue,
    outlineVariant = SizeProPalette.Border,

    inverseSurface = Color.White,
    inverseOnSurface = Color.Black,
    inversePrimary = SizeProPalette.Blue,

    scrim = Color.Black
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
