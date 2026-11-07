package com.pixelresize.pro

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumericKeypad(
    onDigit: (String) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit
) {
    val keys = listOf(
        listOf("7", "8", "9", "⌫"),
        listOf("4", "5", "6", "C"),
        listOf("1", "2", "3", "0")
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        keys.forEach { rowKeys ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                rowKeys.forEach { key ->

                    val isBackspace = key == "⌫"
                    val isClear = key == "C"

                    val backgroundColor = when {
                        isBackspace ->
                            MaterialTheme.colorScheme.secondaryContainer

                        isClear ->
                            MaterialTheme.colorScheme.errorContainer

                        else ->
                            MaterialTheme.colorScheme.surfaceVariant
                    }

                    val contentColor = when {
                        isBackspace ->
                            MaterialTheme.colorScheme.onSecondaryContainer

                        isClear ->
                            MaterialTheme.colorScheme.onErrorContainer

                        else ->
                            MaterialTheme.colorScheme.onSurface
                    }

                    val accessibilityLabel = when {
                        isBackspace -> "Apagar último dígito"
                        isClear -> "Limpar valor"
                        else -> "Número $key"
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(backgroundColor)
                            .semantics {
                                contentDescription = accessibilityLabel
                            }
                            .clickable(role = Role.Button) {
                                when (key) {
                                    "⌫" -> onBackspace()
                                    "C" -> onClear()
                                    else -> onDigit(key)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = key,
                            color = contentColor,
                            fontSize = if (key.length == 1) 19.sp else 21.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
