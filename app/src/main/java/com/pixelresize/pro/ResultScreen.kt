package com.pixelresize.pro

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import com.pixelresize.pro.data.ImageResult

@Composable
fun ResultScreen(
    result: ImageResult,
    onShare: () -> Unit,
    onResizeAgain: () -> Unit,
    onUploadAnother: () -> Unit
) {
    val configuration = LocalConfiguration.current

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val needsScroll =
            maxHeight < 700.dp ||
            configuration.fontScale > 1.15f

        val previewHeight = (maxHeight * 0.24f)
            .coerceIn(145.dp, 190.dp)

        val scrollState = rememberScrollState()

        val contentModifier = if (needsScroll) {
            Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 18.dp, vertical = 12.dp)
        } else {
            Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 12.dp)
        }

        Column(
            modifier = contentModifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Cabeçalho de sucesso.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Sucesso",
                    tint = SizeProPalette.Green,
                    modifier = Modifier
                        .width(34.dp)
                        .height(34.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Imagem guardada!",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "A tua imagem foi processada com sucesso.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "SP",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp,
                    color = SizeProPalette.Orange
                )
            }

            // Pré-visualização da imagem exportada.
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(previewHeight),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SizeProPalette.SurfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ImagePreview(
                        imageUri = result.uri
                    )
                }
            }

            // Localização do ficheiro.
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SizeProPalette.SurfaceElevated
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        tint = SizeProPalette.Orange
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "Localização do ficheiro",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "Downloads / Size Pro",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Informações reais do ficheiro.
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SizeProPalette.Surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    Text(
                        text = "Detalhes do resultado",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    ResultDetail(
                        label = "Resolução",
                        value = result.resolution
                    )

                    ResultDetail(
                        label = "Tamanho do ficheiro",
                        value = result.formattedFileSize
                    )

                    ResultDetail(
                        label = "Formato",
                        value = result.format.displayName
                    )
                }
            }

            // Espaço flexível apenas quando não existe rolagem.
            if (needsScroll) {
                Spacer(modifier = Modifier.height(4.dp))
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Partilhar a imagem exportada.
            Button(
                onClick = onShare,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(9.dp))

                Text(
                    text = "Partilhar imagem",
                    fontWeight = FontWeight.Bold
                )
            }

            // Reutilizar a imagem no editor.
            OutlinedButton(
                onClick = onResizeAgain,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text("Redimensionar novamente")
            }

            // Começar com outra imagem.
            OutlinedButton(
                onClick = onUploadAnother,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text("Escolher outra imagem")
            }
        }
    }
}

@Composable
private fun ResultDetail(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = value,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End
        )
    }
}
