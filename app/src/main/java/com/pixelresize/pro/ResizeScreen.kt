
package com.pixelresize.pro

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pixelresize.pro.image.ImageExporter
import com.pixelresize.pro.image.ImageFormat
import com.pixelresize.pro.image.ImageProcessor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@Composable
fun ResizeScreen(
    imageUri: Uri,
    onBack: () -> Unit,
    onImageProcessed: (Uri, Int, Int, ImageFormat, Long) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current

    var selectedImageUri by remember(imageUri) {
        mutableStateOf(imageUri)
    }

    var sourceWidth by remember { mutableStateOf(0) }
    var sourceHeight by remember { mutableStateOf(0) }

    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    var keepAspectRatio by remember { mutableStateOf(true) }

    var format by remember {
        mutableStateOf(ImageFormat.JPEG)
    }

    var quality by remember { mutableStateOf(90f) }

    var isLoading by remember { mutableStateOf(true) }
    var isProcessing by remember { mutableStateOf(false) }

    var processingMessage by remember {
        mutableStateOf("A preparar imagem...")
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    BackHandler(enabled = isProcessing) {
        // Evita interromper acidentalmente a exportação.
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { newUri ->
        if (newUri != null && !isProcessing) {
            selectedImageUri = newUri
        }
    }

    LaunchedEffect(selectedImageUri) {
        isLoading = true
        errorMessage = null

        sourceWidth = 0
        sourceHeight = 0
        width = ""
        height = ""

        try {
            val info = ImageProcessor.inspectImage(
                context,
                selectedImageUri
            )

            sourceWidth = info.width
            sourceHeight = info.height

            width = info.width.toString()
            height = info.height.toString()

        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            errorMessage = error.message
                ?: "Não foi possível ler a imagem."
        } finally {
            isLoading = false
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val needsScroll =
            maxHeight < 740.dp ||
            configuration.fontScale > 1.15f

        val previewHeight = (maxHeight * 0.15f)
            .coerceIn(96.dp, 128.dp)

        val contentModifier = Modifier
            .fillMaxSize()
            .then(
                if (needsScroll) {
                    Modifier.verticalScroll(rememberScrollState())
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)

        Column(
            modifier = contentModifier,
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onBack,
                    enabled = !isProcessing
                ) {
                    Text("Voltar")
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Resize",
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "SP",
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
            }

            if (isLoading) {

                Spacer(modifier = Modifier.height(24.dp))

                CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )

                Text(
                    text = "A analisar a imagem...",
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            } else if (sourceWidth > 0 && sourceHeight > 0) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(previewHeight),
                    contentAlignment = Alignment.Center
                ) {
                    ImagePreview(selectedImageUri)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "Imagem original",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "$sourceWidth × $sourceHeight px",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            imagePickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        enabled = !isProcessing
                    ) {
                        Text("Alterar")
                    }
                }

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
                                ).toInt()
                                    .coerceAtLeast(1)
                                    .toString()
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
                                ).toInt()
                                    .coerceAtLeast(1)
                                    .toString()
                            }
                        }
                    },

                    keepAspectRatio = keepAspectRatio,
                    onKeepAspectRatioChange = {
                        keepAspectRatio = it
                    },

                    format = format,
                    onFormatChange = {
                        format = it
                    },

                    quality = quality,
                    onQualityChange = {
                        quality = it
                    }
                )

                val targetWidth = width.toIntOrNull() ?: 0
                val targetHeight = height.toIntOrNull() ?: 0

                val estimatedBytes = estimateOutputSize(
                    width = targetWidth,
                    height = targetHeight,
                    format = format,
                    quality = quality
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tamanho estimado",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = if (estimatedBytes > 0) {
                            "~ ${formatFileSize(estimatedBytes)}"
                        } else {
                            "—"
                        },
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Button(
                    onClick = {
                        if (isProcessing || isLoading) {
                            return@Button
                        }

                        val finalWidth = width.toIntOrNull()
                        val finalHeight = height.toIntOrNull()

                        val valid =
                            finalWidth != null &&
                            finalHeight != null &&
                            finalWidth in 1..ImageProcessor.MAX_DIMENSION &&
                            finalHeight in 1..ImageProcessor.MAX_DIMENSION &&
                            finalWidth.toLong() * finalHeight <=
                                ImageProcessor.MAX_PIXELS

                        if (!valid) {
                            errorMessage =
                                "Dimensões inválidas. Verifique os limites permitidos."
                            return@Button
                        }

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            errorMessage =
                                "A gravação direta em Downloads requer Android 10 ou superior."
                            return@Button
                        }

                        val outputWidth = finalWidth
                        val outputHeight = finalHeight
                        val outputFormat = format
                        val outputQuality = quality.roundToInt()
                            .coerceIn(1, 100)
                        val sourceToExport = selectedImageUri

                        isProcessing = true
                        processingMessage = "A preparar imagem..."
                        errorMessage = null

                        scope.launch {
                            try {
                                val savedResult = withContext(Dispatchers.IO) {
                                    saveImageToDownloads(
                                        context = context,
                                        sourceUri = sourceToExport,
                                        width = outputWidth,
                                        height = outputHeight,
                                        format = outputFormat,
                                        quality = outputQuality,
                                        onStage = { message ->
                                            withContext(Dispatchers.Main.immediate) {
                                                processingMessage = message
                                            }
                                        }
                                    )
                                }

                                onImageProcessed(
                                    savedResult.first,
                                    outputWidth,
                                    outputHeight,
                                    outputFormat,
                                    savedResult.second
                                )

                            } catch (error: CancellationException) {
                                throw error
                            } catch (error: Exception) {
                                errorMessage = error.message
                                    ?: "Não foi possível processar a imagem."
                            } finally {
                                isProcessing = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !isProcessing && !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Redimensionar e guardar",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (errorMessage != null && sourceWidth == 0 && !isLoading) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        if (isProcessing) {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(26.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {

                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(58.dp)
                                .width(58.dp),
                            strokeWidth = 4.dp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "A processar imagem",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = processingMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "Por favor, aguarde...",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
private suspend fun saveImageToDownloads(
    context: Context,
    sourceUri: Uri,
    width: Int,
    height: Int,
    format: ImageFormat,
    quality: Int,
    onStage: suspend (String) -> Unit
): Pair<Uri, Long> {

    val resolver = context.contentResolver

    val filename =
        "SizePro_${width}x${height}_${System.currentTimeMillis()}.${format.extension}"

    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, format.mimeType)
        put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            "${Environment.DIRECTORY_DOWNLOADS}/Size Pro"
        )
        put(MediaStore.MediaColumns.IS_PENDING, 1)
    }

    val destination = resolver.insert(
        MediaStore.Downloads.EXTERNAL_CONTENT_URI,
        values
    ) ?: throw IllegalStateException(
        "Não foi possível criar o ficheiro em Downloads."
    )

    var published = false

    try {
        onStage("A preparar e redimensionar a imagem...")

        val bitmap: Bitmap = ImageProcessor.decodeImage(
            context = context,
            uri = sourceUri,
            targetWidth = width,
            targetHeight = height
        )

        val fileSize = try {
            onStage("A comprimir a imagem em ${format.displayName}...")

            ImageExporter.exportImage(
                context = context,
                bitmap = bitmap,
                destination = destination,
                format = format,
                quality = quality.coerceIn(1, 100)
            )
        } finally {
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
        }

        onStage("A finalizar e guardar o ficheiro...")

        val publishValues = ContentValues().apply {
            put(MediaStore.MediaColumns.IS_PENDING, 0)
        }

        val updatedRows = resolver.update(
            destination,
            publishValues,
            null,
            null
        )

        if (updatedRows <= 0) {
            throw IllegalStateException(
                "Não foi possível finalizar a gravação."
            )
        }

        published = true

        onStage("Imagem guardada com sucesso!")

        return destination to fileSize

    } finally {
        if (!published) {
            runCatching {
                resolver.delete(destination, null, null)
            }
        }
    }
}

private fun estimateOutputSize(
    width: Int,
    height: Int,
    format: ImageFormat,
    quality: Float
): Long {
    if (width <= 0 || height <= 0) return 0L

    val pixels = width.toLong() * height.toLong()

    val bytesPerPixel = when (format) {
        ImageFormat.JPEG ->
            0.18 * (quality / 90f).coerceIn(0.1f, 1.2f)

        ImageFormat.PNG -> 1.0

        ImageFormat.WEBP ->
            0.12 * (quality / 90f).coerceIn(0.1f, 1.2f)
    }

    return (pixels.toDouble() * bytesPerPixel)
        .roundToLong()
        .coerceAtLeast(1L)
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes >= 1024L * 1024L ->
            String.format(
                Locale.getDefault(),
                "%.2f MB",
                bytes / (1024.0 * 1024.0)
            )

        bytes >= 1024L ->
            String.format(
                Locale.getDefault(),
                "%.0f KB",
                bytes / 1024.0
            )

        else -> "$bytes B"
    }
}
