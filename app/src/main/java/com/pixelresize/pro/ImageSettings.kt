
package com.pixelresize.pro

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pixelresize.pro.image.ImageFormat

private enum class ActiveDimension {
    WIDTH,
    HEIGHT
}

@Composable
fun ImageSettings(
    sourceWidth: Int,
    sourceHeight: Int,
    width: String,
    height: String,
    onWidthChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    keepAspectRatio: Boolean,
    onKeepAspectRatioChange: (Boolean) -> Unit,
    format: ImageFormat,
    onFormatChange: (ImageFormat) -> Unit,
    quality: Float,
    onQualityChange: (Float) -> Unit
) {
    var activeDimension by remember {
        mutableStateOf(ActiveDimension.WIDTH)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Text(
            text = "Dimensões da imagem",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = width,
                onValueChange = {},
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        activeDimension = ActiveDimension.WIDTH
                    },
                readOnly = true,
                label = { Text("Largura (px)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            OutlinedTextField(
                value = height,
                onValueChange = {},
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        activeDimension = ActiveDimension.HEIGHT
                    },
                readOnly = true,
                label = { Text("Altura (px)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                shape = RoundedCornerShape(14.dp)
            )
        }

        Text(
            text = when (activeDimension) {
                ActiveDimension.WIDTH -> "A editar: largura"
                ActiveDimension.HEIGHT -> "A editar: altura"
            },
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )

        // TECLADO NUMÉRICO INTEGRADO

        NumericKeypad(
            onDigit = { digit ->
                val current = when (activeDimension) {
                    ActiveDimension.WIDTH -> width
                    ActiveDimension.HEIGHT -> height
                }

                if (current.length < 5) {
                    val updated = current + digit

                    when (activeDimension) {
                        ActiveDimension.WIDTH -> onWidthChange(updated)
                        ActiveDimension.HEIGHT -> onHeightChange(updated)
                    }
                }
            },
            onBackspace = {
                val current = when (activeDimension) {
                    ActiveDimension.WIDTH -> width
                    ActiveDimension.HEIGHT -> height
                }

                val updated = current.dropLast(1)

                when (activeDimension) {
                    ActiveDimension.WIDTH -> onWidthChange(updated)
                    ActiveDimension.HEIGHT -> onHeightChange(updated)
                }
            },
            onClear = {
                when (activeDimension) {
                    ActiveDimension.WIDTH -> onWidthChange("")
                    ActiveDimension.HEIGHT -> onHeightChange("")
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Manter proporção",
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Ajustar automaticamente a outra dimensão",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Switch(
                checked = keepAspectRatio,
                onCheckedChange = onKeepAspectRatioChange
            )
        }

        Text(
            text = "Formato de saída",
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ImageFormat.entries.forEach { imageFormat ->
                FilterChip(
                    selected = format == imageFormat,
                    onClick = {
                        onFormatChange(imageFormat)
                    },
                    label = {
                        Text(imageFormat.displayName)
                    }
                )
            }
        }

        Text(
            text = "Qualidade: ${quality.toInt()}%",
            fontWeight = FontWeight.Bold
        )

        Slider(
            value = quality,
            onValueChange = onQualityChange,
            valueRange = 1f..100f,
            enabled = format != ImageFormat.PNG
        )

        Text(
            text = when (format) {
                ImageFormat.JPEG ->
                    "JPG: qualidade ajustável, sem transparência."

                ImageFormat.PNG ->
                    "PNG: compressão sem perdas."

                ImageFormat.WEBP ->
                    "WebP: formato moderno com qualidade ajustável."
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Imagem original: $sourceWidth × $sourceHeight px",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
