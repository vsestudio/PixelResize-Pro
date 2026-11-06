package com.pixelresize.pro

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Paleta visual oficial do Size Pro.
 * Estas cores também podem ser utilizadas
 * individualmente nos componentes da aplicação.
 */
object SizeProPalette {

    val Background = Color(0xFF111116)
    val Surface = Color(0xFF1B1B23)
    val SurfaceVariant = Color(0xFF282832)
    val SurfaceElevated = Color(0xFF22222D)

    val Orange = Color(0xFFFF956B)
    val OrangeLight = Color(0xFFFFB078)

    val Blue = Color(0xFF65B9FF)
    val BlueLight = Color(0xFF91D0FF)

    val Green = Color(0xFF55D6A5)
    val GreenLight = Color(0xFF85E8BD)

    val Pink = Color(0xFFFF83B5)
    val PinkLight = Color(0xFFFFA5CB)

    val Violet = Color(0xFFB69AFF)
    val VioletLight = Color(0xFFD0B9FF)

    val TextPrimary = Color(0xFFF8F5FA)
    val TextSecondary = Color(0xFFB5B1C0)

    val Border = Color(0xFF41404D)
    val Error = Color(0xFFFF747E)
}

private val SizeProColors = darkColorScheme(

    // Cor principal: laranja coral
    primary = SizeProPalette.Orange,
    onPrimary = Color(0xFF32180F),
    primaryContainer = Color(0xFF593022),
    onPrimaryContainer = Color(0xFFFFD7C7),

    // Cor secundária: azul
    secondary = SizeProPalette.Blue,
    onSecondary = Color(0xFF10243A),
    secondaryContainer = Color(0xFF233D58),
    onSecondaryContainer = Color(0xFFD2E9FF),

    // Cor terciária: violeta
    tertiary = SizeProPalette.Violet,
    onTertiary = Color(0xFF291A45),
    tertiaryContainer = Color(0xFF40305E),
    onTertiaryContainer = Color(0xFFE8DBFF),

    // Fundo e superfícies
    background = SizeProPalette.Background,
    onBackground = SizeProPalette.TextPrimary,

    surface = SizeProPalette.Surface,
    onSurface = SizeProPalette.TextPrimary,

    surfaceVariant = SizeProPalette.SurfaceVariant,
    onSurfaceVariant = SizeProPalette.TextSecondary,

    // Estados e detalhes
    error = SizeProPalette.Error,
    onError = Color(0xFF3B1018),
    errorContainer = Color(0xFF5C252D),
    onErrorContainer = Color(0xFFFFD9DC),

    outline = SizeProPalette.Border,
    outlineVariant = Color(0xFF353440),

    inverseSurface = Color(0xFFE9E4EF),
    inverseOnSurface = Color(0xFF292631),
    inversePrimary = Color(0xFFAD4E35),

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
