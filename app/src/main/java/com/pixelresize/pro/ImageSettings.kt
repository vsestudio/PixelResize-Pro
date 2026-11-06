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
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {

        // DIMENSÕES
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Dimensions",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))

            Text(
                "Original: ${sourceWidth} × ${sourceHeight}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

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
                label = { Text("Width (px)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.width(8.dp))

            OutlinedTextField(
                value = height,
                onValueChange = {},
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        activeDimension = ActiveDimension.HEIGHT
                    },
                readOnly = true,
                label = { Text("Height (px)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // DIMENSÃO ATIVA E PROPORÇÃO
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (activeDimension == ActiveDimension.WIDTH) {
                    "Editing: Width"
                } else {
                    "Editing: Height"
                },
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.weight(1f))

            Text(
                "Keep ratio",
                style = MaterialTheme.typography.labelMedium
            )

            Switch(
                checked = keepAspectRatio,
                onCheckedChange = onKeepAspectRatioChange
            )
        }

        // TECLADO NUMÉRICO
        NumericKeypad(
            onDigit = { digit ->
                val current = if (
                    activeDimension == ActiveDimension.WIDTH
                ) width else height

                if (current.length < 5) {
                    val updated = current + digit

                    if (activeDimension == ActiveDimension.WIDTH) {
                        onWidthChange(updated)
                    } else {
                        onHeightChange(updated)
                    }
                }
            },
            onBackspace = {
                val current = if (
                    activeDimension == ActiveDimension.WIDTH
                ) width else height

                val updated = current.dropLast(1)

                if (activeDimension == ActiveDimension.WIDTH) {
                    onWidthChange(updated)
                } else {
                    onHeightChange(updated)
                }
            },
            onClear = {
                if (activeDimension == ActiveDimension.WIDTH) {
                    onWidthChange("")
                } else {
                    onHeightChange("")
                }
            }
        )

        // FORMATO
        Text(
            "Output format",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ImageFormat.entries.forEach { imageFormat ->
                FilterChip(
                    selected = format == imageFormat,
                    onClick = { onFormatChange(imageFormat) },
                    label = {
                        Text(
                            imageFormat.displayName,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // QUALIDADE
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Quality",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.weight(1f))

            Text(
                if (format == ImageFormat.PNG) {
                    "Lossless"
                } else {
                    "${quality.toInt()}%"
                },
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Slider(
            value = quality,
            onValueChange = onQualityChange,
            valueRange = 1f..100f,
            enabled = format != ImageFormat.PNG
        )
    }
}
