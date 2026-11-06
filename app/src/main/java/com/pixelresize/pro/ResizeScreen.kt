package com.pixelresize.pro

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import com.pixelresize.pro.image.ImageExporter
import com.pixelresize.pro.image.ImageFormat
import com.pixelresize.pro.image.ImageProcessor
import kotlinx.coroutines.launch
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

    var format by remember { mutableStateOf(ImageFormat.JPEG) }
    var quality by remember { mutableStateOf(90f) }

    var isLoading by remember { mutableStateOf(true) }
    var isProcessing by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var pendingWidth by remember { mutableStateOf(0) }
    var pendingHeight by remember { mutableStateOf(0) }
    var pendingFormat by remember { mutableStateOf(ImageFormat.JPEG) }
    var pendingQuality by remember { mutableStateOf(90) }
    var pendingImageUri by remember { mutableStateOf<Uri?>(null) }

    // Escolher outra imagem sem sair da edição.
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { newUri ->
        if (newUri != null) {
            selectedImageUri = newUri
        }
    }

    // Guardar e processar a imagem no destino escolhido.
    val saveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->

        val destination = activityResult.data?.data
        val sourceToExport = pendingImageUri
        pendingImageUri = null

        if (
            activityResult.resultCode == Activity.RESULT_OK &&
            destination != null &&
            sourceToExport != null
        ) {
            scope.launch {
                var bitmap: Bitmap? = null

                isProcessing = true
                errorMessage = null

                try {
                    bitmap = ImageProcessor.decodeImage(
                        context = context,
                        uri = sourceToExport,
                        targetWidth = pendingWidth,
                        targetHeight = pendingHeight
                    )

                    val fileSize = ImageExporter.exportImage(
                        context = context,
                        bitmap = bitmap,
                        destination = destination,
                        format = pendingFormat,
                        quality = pendingQuality
                    )

                    onImageProcessed(
                        destination,
                        pendingWidth,
                        pendingHeight,
                        pendingFormat,
                        fileSize
                    )

                } catch (error: Exception) {
                    errorMessage = error.message
                        ?: "Ocorreu um erro ao processar a imagem."
                } finally {
                    bitmap?.recycle()
                    isProcessing = false
                }
            }
        }
    }

    // Analisar a imagem selecionada.
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
        // Em ecrãs normais evitamos a rolagem.
        // Em ecrãs muito pequenos ou com texto ampliado,
        // mantemos uma alternativa para não cortar os controlos.
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

            // Cabeçalho.
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
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "A analisar a imagem...",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            } else if (sourceWidth > 0 && sourceHeight > 0) {

                // Pré-visualização compacta.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(previewHeight),
                    contentAlignment = Alignment.Center
                ) {
                    ImagePreview(selectedImageUri)
                }

                // Informação da imagem e substituição.
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
                        Text("Change")
                    }
                }

                // Configurações e teclado numérico.
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
                    onFormatChange = {
                        format = it
                    },

                    quality = quality,
                    onQualityChange = {
                        quality = it
                    }
                )

                // Estimativa indicativa do tamanho final.
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

                // Botão principal.
                Button(
                    onClick = {
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

                        pendingWidth = finalWidth
                        pendingHeight = finalHeight
                        pendingFormat = format
                        pendingQuality = quality.toInt()
                        pendingImageUri = selectedImageUri

                        val filename =
                            "SizePro_${finalWidth}x${finalHeight}." +
                                format.extension

                        val intent = Intent(
                            Intent.ACTION_CREATE_DOCUMENT
                        ).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = format.mimeType
                            putExtra(Intent.EXTRA_TITLE, filename)
                        }

                        try {
                            saveLauncher.launch(intent)
                        } catch (error: Exception) {
                            pendingImageUri = null
                            errorMessage =
                                "Não foi possível abrir o seletor de ficheiros."
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
                    if (isProcessing) {
                        CircularProgressIndicator(
                            modifier = Modifier.height(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "A processar...",
                            modifier = Modifier.padding(start = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "Resize Image",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (errorMessage != null && sourceWidth == 0 && !isLoading) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error
                )
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
        ImageFormat.JPEG -> 0.18 * (quality / 90f).coerceIn(0.1f, 1.2f)
        ImageFormat.PNG -> 1.0
        ImageFormat.WEBP -> 0.12 * (quality / 90f).coerceIn(0.1f, 1.2f)
    }

    return (pixels.toDouble() * bytesPerPixel)
        .roundToLong()
        .coerceAtLeast(1L)
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes >= 1024L * 1024L ->
            String.format("%.2f MB", bytes / (1024.0 * 1024.0))

        bytes >= 1024L ->
            String.format("%.0f KB", bytes / 1024.0)

        else -> "$bytes B"
    }
}
