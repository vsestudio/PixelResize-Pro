
package com.pixelresize.pro

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.pixelresize.pro.image.ImageExporter
import com.pixelresize.pro.image.ImageFormat
import com.pixelresize.pro.image.ImageProcessor
import kotlinx.coroutines.launch

@Composable
fun ResizeScreen(
    imageUri: Uri,
    onBack: () -> Unit,
    onImageProcessed: (Uri, Int, Int, ImageFormat, Long) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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

    // Selecionar outra fotografia sem sair da tela de edição.
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { newUri ->
        if (newUri != null) {
            selectedImageUri = newUri
        }
    }

    // Criar o ficheiro onde a imagem será guardada.
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

    // Analisar a imagem atual, incluindo qualquer nova fotografia escolhida.
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Cabeçalho.
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "SP",
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (isLoading) {

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = "A analisar a imagem...",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        } else if (sourceWidth > 0 && sourceHeight > 0) {

            // Pré-visualização da fotografia atual.
            ImagePreview(selectedImageUri)

            // Dimensões originais e botão Change.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Original: $sourceWidth × $sourceHeight px",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )

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

            // Configurações de redimensionamento.
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

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // Processar e guardar.
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

                    pendingWidth = targetWidth
                    pendingHeight = targetHeight
                    pendingFormat = format
                    pendingQuality = quality.toInt()
                    pendingImageUri = selectedImageUri

                    val filename =
                        "SizePro_${targetWidth}x${targetHeight}." +
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
                    .height(52.dp),
                enabled = !isProcessing && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "Resize Image",
                    fontWeight = FontWeight.Bold
                )
            }

            if (isProcessing) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "A processar e guardar a imagem...",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
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
}
