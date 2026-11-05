
package com.pixelresize.pro

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.pixelresize.pro.image.ImageFormat
import com.pixelresize.pro.image.ImageProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ResizeScreen(
    imageUri: Uri,
    onBack: () -> Unit,
    onImageProcessed: (Uri, Int, Int, ImageFormat, Long) -> Unit
) {
    val context = LocalContext.current

    var sourceWidth by remember { mutableStateOf(0) }
    var sourceHeight by remember { mutableStateOf(0) }

    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    var keepAspectRatio by remember { mutableStateOf(true) }

    var format by remember { mutableStateOf(ImageFormat.JPEG) }
    var quality by remember { mutableStateOf(90f) }

    var isLoading by remember { mutableStateOf(true) }
    var isProcessing by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(imageUri) {
        try {
            val info = ImageProcessor.inspectImage(
                context,
                imageUri
            )

            sourceWidth = info.width
            sourceHeight = info.height

            width = info.width.toString()
            height = info.height.toString()

            errorMessage = null
        } catch (error: Exception) {
            errorMessage = error.message ?: "Não foi possível ler a imagem."
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = onBack) {
                Text("Voltar")
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Editor",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (isLoading) {
            CircularProgressIndicator()
        } else {

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error
                )
            } else {

                ImagePreview(imageUri)

                Text(
                    text = "Original: $sourceWidth × $sourceHeight px",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                ImageSettings(
                    sourceWidth = sourceWidth,
                    sourceHeight = sourceHeight,
                    width = width,
                    height = height,
                    onWidthChange = { newWidth ->
                        width = newWidth

                        if (keepAspectRatio) {
                            val value = newWidth.toIntOrNull()

                            if (value != null && sourceWidth > 0) {
                                height = (
                                    value.toDouble() *
                                        sourceHeight / sourceWidth
                                    ).toInt().coerceAtLeast(1).toString()
                            }
                        }
                    },
                    onHeightChange = { newHeight ->
                        height = newHeight

                        if (keepAspectRatio) {
                            val value = newHeight.toIntOrNull()

                            if (value != null && sourceHeight > 0) {
                                width = (
                                    value.toDouble() *
                                        sourceWidth / sourceHeight
                                    ).toInt().coerceAtLeast(1).toString()
                            }
                        }
                    },
                    keepAspectRatio = keepAspectRatio,
                    onKeepAspectRatioChange = {
                        keepAspectRatio = it
                    },
                    format = format,
                    onFormatChange = { format = it },
                    quality = quality,
                    onQualityChange = { quality = it }
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = {
                        val targetWidth = width.toIntOrNull()
                        val targetHeight = height.toIntOrNull()

                        val valid =
                            targetWidth != null &&
                            targetHeight != null &&
                            targetWidth in 1..ImageProcessor.MAX_DIMENSION &&
                            targetHeight in 1..ImageProcessor.MAX_DIMENSION &&
                            targetWidth.toLong() * targetHeight <=
                                ImageProcessor.MAX_PIXELS

                        if (!valid) {
                            errorMessage =
                                "Dimensões inválidas. Verifique os limites permitidos."
                            return@Button
                        }

                        isProcessing = true
                        errorMessage = null

                        // O processamento e a exportação serão ligados
                        // na próxima etapa.
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    enabled = !isProcessing && !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Redimensionar imagem")
                }

                if (isProcessing) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
